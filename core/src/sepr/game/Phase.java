package sepr.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import sepr.game.utils.TurnPhaseType;

/**
 * base class for handling phase specific input
 */
public abstract class Phase extends Stage {
    GameScreen gameScreen;
    Player currentPlayer;
    Player previousPlayer;


    private Table table;
    private Label bottomBarRightPart;
    private TurnPhaseType turnPhase;

    private Label.LabelStyle playerNameStyle; // store style for updating player name colour with player's colour

    private Label playerNameLabel; // displays the name of the current player in their college's colour colour
    private Label reinforcementLabel; // label showing how many troops the player has to allocate in their next reinforcement phase
    private Label turnTimerLabel; // displays how much time the player has left
    private Image collegeLogo; // ui component for displaying the logo of the current players college

    private static Texture gameHUDBottomBarLeftPartTexture;

    /**
     *
     * @param gameScreen for accessing the map and additional game properties
     * @param turnPhase type of phase this is
     */
    public Phase(GameScreen gameScreen, TurnPhaseType turnPhase) {
        this.setViewport(new ScreenViewport());

        this.gameScreen = gameScreen;

        this.turnPhase = turnPhase;

        this.table = new Table();
        this.table.setFillParent(true); // make ui table fill the entire screen
        this.addActor(table);
        this.table.setDebug(false); // enable table drawing for ui debug

        gameHUDBottomBarLeftPartTexture = new Texture("uiComponents/HUD-Bottom-Bar-Left-Part.png");

        this.setupUi();
    }

    /**
     * setup UI that is consistent across all game phases
     */
    private void setupUi() {
        TextButton endPhaseButton = WidgetFactory.genEndPhaseButton();
        endPhaseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.nextPhase();
            }
        });
        bottomBarRightPart = WidgetFactory.genGameHUDBottomBarRightPart("INIT");
        Table bottomBarLeftPart = genGameHUDBottomBarLeftPart();

        table.top().center();
        table.add(WidgetFactory.genGameHUDTopBar(turnPhase, gameScreen)).colspan(2).expandX().height(60).width(910);

        table.row();
        table.add(new Table()).expand();

        Table subTable = new Table();

        subTable.bottom();
        subTable.add(bottomBarLeftPart).height(190).width(250);
        subTable.add(bottomBarRightPart).bottom().expandX().fillX().height(60);

        table.row();
        table.add(subTable).expandX().fill();
        table.bottom().right();
        table.add(endPhaseButton).fill().height(60).width(170);

        setBottomBarText(null);
    }

    /**
     * generates the UI widget to be displayed at the bottom left of the HUD
     * @return table containing the information to display in the HUD
     */
    private Table genGameHUDBottomBarLeftPart(){
        Label.LabelStyle style = new Label.LabelStyle();
        playerNameStyle = new Label.LabelStyle();

        // load fonts
        style.font = WidgetFactory.getFontSmall();

        playerNameStyle.font = WidgetFactory.getFontSmall();

        playerNameLabel = new Label("", playerNameStyle);
        reinforcementLabel = new Label("", style);
        turnTimerLabel = new Label("Timer: DISABLED", style);
        collegeLogo = new Image(WidgetFactory.genCollegeLogoDrawable(GameSetupScreen.CollegeName.UNI_OF_YORK));

        Table table = new Table();
        table.background(new TextureRegionDrawable(new TextureRegion(gameHUDBottomBarLeftPartTexture)));

        Table subTable = new Table();
        subTable.setDebug(false);
        subTable.left().add(collegeLogo).height(80).width(100).pad(0);
        subTable.right().add(playerNameLabel).pad(0);
        subTable.row();
        subTable.add(reinforcementLabel).colspan(2);
        subTable.row();
        subTable.add(turnTimerLabel).colspan(2);

        table.add(subTable);

        return table;
    }

    /**
     * sets the bar at the bottom of the HUD to the details of the sector currently hovered over
     * If no sector is being hovered then displays "Mouse over a sector to see further details"
     * @param sector the sector of details to be displayed
     */
    public void setBottomBarText(Sector sector) {
        if (sector == null) {
            this.bottomBarRightPart.setText("Mouse over a sector to see further details");
        } else {
            this.bottomBarRightPart.setText("College: " + sector.getCollege() + " - " + sector.getDisplayName() + " - " + "Owned By: " + gameScreen.getPlayerById(sector.getOwnerId()).getPlayerName() + " - " + "Grants +" + sector.getReinforcementsProvided() + " Troops");
        }
    }

    /**
     * sets up phase when a new player enters it
     *
     * @param player the new player that is entering the phase
     */
    void enterPhase(Player player) {
        this.currentPlayer = player;


        playerNameStyle.fontColor = GameSetupScreen.getCollegeColor(currentPlayer.getCollegeName()); // update colour of player name

        playerNameLabel.setText(new StringBuilder((CharSequence) currentPlayer.getPlayerName())); // change the bottom bar label to the players name
        collegeLogo.setDrawable(WidgetFactory.genCollegeLogoDrawable(player.getCollegeName()));
        updateTroopReinforcementLabel();
    }

    /**
     * updates the text of the turn timer label
     *
     * @param timeRemaining time remaining of turn in seconds
     */
    void setTimerValue(int timeRemaining) {
        turnTimerLabel.setText(new StringBuilder("Turn Timer: " + timeRemaining));
    }

    /**
     * updates the display of the number of troops the current player will have in their next reinforcement phase
     */
    void updateTroopReinforcementLabel() {
        this.reinforcementLabel.setText("Troop Allocation: " + currentPlayer.getTroopsToAllocate());
    }

    /**
     * method for tidying up phase for next player to use
     */
    public void endPhase () {
        this.currentPlayer = null;
    }

    @Override
    public void act() {
        super.act();
    }

    @Override
    public void draw() {
        phaseAct();

        gameScreen.getGameplayBatch().begin();
        visualisePhase(gameScreen.getGameplayBatch());
        gameScreen.getGameplayBatch().end();

        super.draw();
    }

    public abstract void phaseAct();

    /**
     * abstract method for writing phase specific rendering
     * @param batch
     */
    protected abstract void visualisePhase(SpriteBatch batch);

    @Override
    public String toString() {
        switch(this.turnPhase){
            case ATTACK:
                return "PHASE_ATTACK";
            case MOVEMENT:
                return "PHASE_MOVEMENT";
            case REINFORCEMENT:
                return "PHASE_REINFORCEMENT";
            default:
                return "PHASE_BLANK";
        }
    }
}

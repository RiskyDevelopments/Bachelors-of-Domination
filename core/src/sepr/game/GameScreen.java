package sepr.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public class GameScreen implements Screen, InputProcessor{
    private Main main;

    private Stage stage;
    private Table table;

    private Map map;
    private SpriteBatch gamplayBatch;
    private OrthographicCamera gameplayCamera;
    private Viewport gameplayViewport;
    private Texture mapBackground;

    private HashMap<Integer, Player> players; // player id mapping to the relevant player
    private HashMap<Integer, Boolean> keysDown; // mapping from key, (Input.Keys), to whether it has been pressed down

    private boolean turnTimerEnabled;
    private boolean turnTimerPaused;
    private int maxTurnTime;
    private int turnTimeElapsed;
    private List<Integer> turnOrder; // array of player ids in order of players' turns;
    private int currentPlayer; // index of current player in turnOrder list
    private TurnPhase currentPhase = TurnPhase.REINFORCEMENT; // first phase of game is reinforcement
    private boolean midAttack; // Stores if the attack phase is initiated
    private Sector attackingSector; // Stores the sector being used to attack in the attack phase (could store as ID and lookup object each time to save memory)
    private Sector defendingSector; // Stores the sector being attacked in the attack phase (could store as ID and lookup object each time to save memory)

    /**
     * Performs the game's initial setup
     * @param main used to change screen
     * @param players hashmap of the players in this game
     * @param turnTimerEnabled should players turn be limitted
     * @param maxTurnTime time elapsed in current turn, irrelevant if turn timer not enabled
     */
    public GameScreen(Main main, HashMap<Integer, Player> players, boolean turnTimerEnabled, int maxTurnTime) {
        this.main = main;

        this.stage = new Stage();
        this.stage.setViewport(new ScreenViewport());

        this.map = new Map();
        this.gamplayBatch = new SpriteBatch();
        this.gameplayCamera = new OrthographicCamera();
        this.gameplayViewport = new ScreenViewport(gameplayCamera);
        this.mapBackground = new Texture("ui/mapBackground.png");

        this.players = players;

        this.keysDown = new HashMap<Integer, Boolean>();
        this.keysDown.put(Input.Keys.UP, false);
        this.keysDown.put(Input.Keys.LEFT, false);
        this.keysDown.put(Input.Keys.DOWN, false);
        this.keysDown.put(Input.Keys.RIGHT, false);

        this.turnTimerEnabled = turnTimerEnabled;
        this.turnTimerPaused = false;
        this.maxTurnTime = maxTurnTime;
        this.turnTimeElapsed = 0;
        this.turnOrder = new ArrayList<Integer>(players.keySet());
        this.currentPlayer = 0;
        this.midAttack = false;

        setupUi();

        allocateSectors();
    }

    /**
     *
     */
    private void setupUi() {
        this.table = new Table();
        this.table.setFillParent(true);
        this.stage.addActor(table);
        this.table.setDebug(true);

        Texture buttons = new Texture("ui/buttons.png"); // texture sheet for buttons
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); // create style for buttons to use
        style.up = new TextureRegionDrawable(new TextureRegion(buttons, 0, 0, 400, 150)); // image for button to use in default state
        style.down = new TextureRegionDrawable(new TextureRegion(buttons, 0, 150, 400, 150)); // image for button to use when pressed down
        style.font = new BitmapFont(); // set button font to the default Bitmap Font

        final TextButton startGameBtn = new TextButton("New Game", style);
        table.bottom();
        table.left();
        table.add(startGameBtn);
    }


    /**
     * Created by Owain's Asus on 10/12/2017.
     * Allocate sectors to each player in a balanced manner.
     * Just need the finished csv file so we can calculate Total reinforcements but apart from
     * that the method is finished. The method also has an if statement to catch a divide by zero
     * error in players.size(). This won't be needed as later on when more of the game implementation is
     * introduced this method will only be called when all players have been declared after the intermediate
     * setup menu.
     */
    private void allocateSectors() {
        if (players.size() == 0) {
            throw new RuntimeException("Cannot allocate sectors to 0 players");
        }

        HashMap<Integer, Integer> playerReinforcements = new HashMap<Integer, Integer>(); // mapping of player id to amount of reinforcements they will receive currently
        // set all players to currently be receiving 0 reinforcements
        for (Integer i : players.keySet()) {
            playerReinforcements.put(i, 0);
        }

        int lowestReinforcementId = players.get(0).getId();; // id of player currently receiving the least reinforcements
        for (Integer i : map.getSectorIds()) {
            if (map.getSector(i).isDecor()) {
                continue; // skip allocating sector if it is a decor sector
            }
            map.getSector(i).setOwner(players.get(lowestReinforcementId));
            playerReinforcements.put(lowestReinforcementId, playerReinforcements.get(lowestReinforcementId) + map.getSector(i).getReinforcementsProvided()); // updates player reinforcements hashmap

            // find the new player with lowest reinforcements
            int minReinforcements = Collections.min(playerReinforcements.values()); // get lowest reinforcement amount
            for (Integer j : playerReinforcements.keySet()) {
                if (playerReinforcements.get(j) == minReinforcements) { // if this player has the reinforcements matching the min amount set them to the new lowest player
                    lowestReinforcementId = j;
                    break;
                }
            }
        }
    }

    /**
     *
     */
    private void adavancePhase() {
        switch (currentPhase) {
            case REINFORCEMENT:
                currentPhase = TurnPhase.ATTACK;
                break;
            case ATTACK:
                if (map.checkForWinner() != -1) {
                    // gameover a player has won
                    gameOver(map.checkForWinner());
                }
                currentPhase = TurnPhase.MOVEMENT;
                break;
            case MOVEMENT:
                nextPlayer();
                break;
        }
    }

    /**
     * Called when the player ends the MOVEMENT phase of their turn to advance the game to the next Player's turn
     */
    private void nextPlayer() {
        currentPhase = TurnPhase.REINFORCEMENT;
        currentPlayer++;
        if (currentPlayer == turnOrder.size()) {
            currentPlayer = 0;
        }
    }

    /**
     * Method called when map class returns a winner when checkForWinner called
     * @param winnerId id of the winning player
     */
    private void gameOver(int winnerId) {

    }

    /**
     * Writes the game state to a file
     */
    private void saveGameState() {
        // not part of this assessment
    }

    /**
     * Reads the given string and setsup the game state from this
     * @param gameState
     */
    private void loadGameState(String gameState) {
        // not part of this assessment
    }

    private void controlCamera() {
        if (this.keysDown.get(Input.Keys.UP)) {
            this.gameplayCamera.translate(0, 4, 0);
        }
        if (this.keysDown.get(Input.Keys.DOWN)) {
            this.gameplayCamera.translate(0, -4, 0);
        }
        if (this.keysDown.get(Input.Keys.LEFT)) {
            this.gameplayCamera.translate(-4, 0, 0);
        }
        if (this.keysDown.get(Input.Keys.RIGHT)) {
            this.gameplayCamera.translate(4, 0, 0);
        }

    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        /* Gameplay */
        // update gameplay
        this.controlCamera();

        //render gameplay
        gameplayCamera.update();
        gamplayBatch.setProjectionMatrix(gameplayCamera.combined);
        gamplayBatch.begin();
        gamplayBatch.draw(mapBackground, 0, 0, gameplayViewport.getScreenWidth(), gameplayViewport.getScreenHeight() );
        map.draw(gamplayBatch);
        gamplayBatch.end();

        /* UI */
        // update UI
        this.stage.act(delta);

        // render UI
        this.stage.draw();
    }


    /**
     * handles mouse clicks during the reinforcement phase
     * @param worldX
     * @param worldY
     */
    private void reinforcePhaseTouchUp(float worldX, float worldY) {

    }

    /**
     * handles mouse clicks during the attack phase
     * @param worldX
     * @param worldY
     */
    private void attackPhaseTouchUp(float worldX, float worldY) {
        int sectorid = map.detectSectorClick((int)worldX, (int)worldY);
        if (sectorid != -1) { // If selected a sector
            Sector selected = map.getSector(sectorid); // Current sector
            if (this.midAttack) { // If its the second selection in the attack phase
                if (this.attackingSector.isAdjacentTo(selected) && selected.getOwnerId() != this.currentPlayer) { // If not own sector and its adjacent
                    this.defendingSector = selected;
                    // Call to initiate attack + advance phase
                    this.midAttack = false;
                } else { // Cancel attack as not attackable
                    this.midAttack = false;
                }
            } else if (selected.getOwnerId() == this.currentPlayer && selected.getUnitsInSector() > 1) { // First selection, is owned by the player and has enough troops
                this.midAttack = true;
                this.attackingSector = selected;
            }
        }
    }

    /**
     * handles mouse clicks during the movement phase
     * @param worldX
     * @param worldY
     */
    private void movementPhaseTouchUp(float worldX, float worldY) {
        // not part of this assessment
    }


    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height);
        this.stage.getCamera().viewportWidth = width;
        this.stage.getCamera().viewportHeight = height;
        this.stage.getCamera().position.x = width/2;
        this.stage.getCamera().position.y = height/2;
        this.stage.getCamera().update();

        this.gameplayViewport.update(width, height);
        this.gameplayCamera.viewportWidth = width;
        this.gameplayCamera.viewportHeight = height;
        this.gameplayCamera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }



    /* Input Processor */

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            keysDown.put(Input.Keys.UP, true);
        }
        if (keycode == Input.Keys.DOWN) {
            keysDown.put(Input.Keys.DOWN, true);
        }
        if (keycode == Input.Keys.LEFT) {
            keysDown.put(Input.Keys.LEFT, true);
        }
        if (keycode == Input.Keys.RIGHT) {
            keysDown.put(Input.Keys.RIGHT, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            keysDown.put(Input.Keys.UP, false);
        }
        if (keycode == Input.Keys.DOWN) {
            keysDown.put(Input.Keys.DOWN, false);
        }
        if (keycode == Input.Keys.LEFT) {
            keysDown.put(Input.Keys.LEFT, false);
        }
        if (keycode == Input.Keys.RIGHT) {
            keysDown.put(Input.Keys.RIGHT, false);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float worldX = gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).x;
        float worldY = (gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).y - Gdx.graphics.getHeight()) * -1;

        switch (currentPhase) {
            case REINFORCEMENT:
                reinforcePhaseTouchUp(worldX, worldY);
                break;
            case ATTACK:
                attackPhaseTouchUp(worldX, worldY);
                break;
            case MOVEMENT:
                movementPhaseTouchUp(worldX, worldY);
                break;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
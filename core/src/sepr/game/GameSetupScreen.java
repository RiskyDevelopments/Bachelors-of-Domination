package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;


public class GameSetupScreen implements Screen{

    private Main main;
    private Stage stage;
    private Table table;

    private CheckBox turnTimerSwitch;
    final int NUMBER_OF_PLAYERS = 5;


    public enum playerType{
        NONE("NONE"),
        HUMAN("HUMAN PLAYER"),
        AI("NEUTRAL A.I.");

        private final String shortCode;

        playerType(String code){
            this.shortCode = code;
        }

        public String getPlayerType(){
            return this.shortCode;
        }
    }

    public enum collegeName{
        ALCUIN("ALCUIN"),
        DERWENT("DERWENT"),
        HALIFAX("HALIFAX"),
        HES_EAST("HESLINGTON EAST"),
        JAMES("JAMES"),
        UNI_OF_YORK("UNIVERSITY OF YORK"),
        VANBRUGH("VANBRUGH"),
        WENTWORTH("WENTWORTH");

        private final String shortCode;

        collegeName(String code){
            this.shortCode=code;
        }

        public String getCollegeName(){
            return this.shortCode;
        }
    }


    public GameSetupScreen (Main main) {

        this.main = main;

        this.stage = new Stage();
        this.stage.setViewport(new ScreenViewport());
        this.table = new Table();
        this.table.setFillParent(true); // make ui table fill the entire screen
        this.stage.addActor(table);
        this.table.setDebug(false); // enable table drawing for ui debug
        this.setupUi();
    }

    public void selectPlayerLeft(Label playerLabel){
        if (playerLabel.getText().toString().equals(playerType.NONE.getPlayerType())){
            playerLabel.setText(playerType.AI.getPlayerType());
        }else if (playerLabel.getText().toString().equals(playerType.HUMAN.getPlayerType())){
            playerLabel.setText(playerType.NONE.getPlayerType());
        }else {
            playerLabel.setText(playerType.HUMAN.getPlayerType());
        }
    }

    public void selectPlayerRight(Label playerLabel){
        if (playerLabel.getText().toString().equals(playerType.NONE.getPlayerType())){
            playerLabel.setText(playerType.HUMAN.getPlayerType());
        }else if (playerLabel.getText().toString().equals(playerType.HUMAN.getPlayerType())){
            playerLabel.setText(playerType.AI.getPlayerType());
        }else {
            playerLabel.setText(playerType.NONE.getPlayerType());
        }
    }

    public String selectCollegeLeft(Label collegeLabel){
        if (collegeLabel.getText().toString().equals(collegeName.ALCUIN.getCollegeName())){
            return collegeName.WENTWORTH.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.DERWENT.getCollegeName())){
            return collegeName.ALCUIN.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.HALIFAX.getCollegeName())){
            return collegeName.DERWENT.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.HES_EAST.getCollegeName())){
            return collegeName.HALIFAX.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.JAMES.getCollegeName())){
            return collegeName.HES_EAST.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.UNI_OF_YORK.getCollegeName())){
            return collegeName.JAMES.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.VANBRUGH.getCollegeName())){
            return collegeName.UNI_OF_YORK.getCollegeName();
        }else {
            return collegeName.VANBRUGH.getCollegeName();

        }

    }

    public String selectCollegeRight(Label collegeLabel){
        if (collegeLabel.getText().toString().equals(collegeName.ALCUIN.getCollegeName())){
            return collegeName.DERWENT.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.DERWENT.getCollegeName())){
            return collegeName.HALIFAX.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.HALIFAX.getCollegeName())){
            return collegeName.HES_EAST.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.HES_EAST.getCollegeName())){
            return collegeName.JAMES.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.JAMES.getCollegeName())){
            return collegeName.UNI_OF_YORK.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.UNI_OF_YORK.getCollegeName())){
            return collegeName.VANBRUGH.getCollegeName();
        }else if(collegeLabel.getText().toString().equals(collegeName.VANBRUGH.getCollegeName())){
            return collegeName.WENTWORTH.getCollegeName();
        }else {
            return collegeName.ALCUIN.getCollegeName();

        }

    }

    private Label[] playerLabels(int numberOfPlayers){
        Label[] labelList = new Label[numberOfPlayers];

        for (int i=0; i<numberOfPlayers; i++){
            final Label playerLabel = WidgetFactory.genPlayerLabel(playerType.NONE.getPlayerType());
            playerLabel.setAlignment(Align.center);
           
            labelList[i] = playerLabel;
            
        }
        return labelList;
    }
    
    private Button[][] playerButtons(int numberOfPlayers, final Label[] playerLabels){
        Button[][] buttonList = new Button[numberOfPlayers][2];
        for(int i =0; i<numberOfPlayers; i++){
            Button playerLeftButton = WidgetFactory.genPlayerLeftButton();
            final int finalI = i;
            playerLeftButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectPlayerLeft(playerLabels[finalI]);
                }
            });
            Button playerRightButton = WidgetFactory.genPlayerRightButton();
            playerRightButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    selectPlayerRight(playerLabels[finalI]);
                }
            });
            
            buttonList[i][0] = playerLeftButton;
            buttonList[i][1] = playerRightButton;
        }
        return buttonList;
    }
    

    private Table setUpGameSetupTable() {

        Label[] pLabels = playerLabels(NUMBER_OF_PLAYERS);
        Button[][] pButtons = playerButtons(NUMBER_OF_PLAYERS, pLabels);

        Table playerTable = new Table();
        playerTable.setDebug(true);

        for(int n = 0; n<NUMBER_OF_PLAYERS; n++){
            playerTable.left();
            playerTable.add(pButtons[n][0]).height(60).width(50).pad(5);
            playerTable.add(pLabels[n]).height(60).width(320).expandX().padTop(20).padBottom(20);
            playerTable.right();
            playerTable.add(pButtons[n][1]).height(60).width(50).pad(5);
            playerTable.row();
        }

        return playerTable;
        }

        private Table setUpTurnTimerTable(){
            Label turnTimerLabel = WidgetFactory.genMenuBtnLabel("TURN TIMER");
            turnTimerLabel.setAlignment(0, 0);

            CheckBox turnTimerSwitch = WidgetFactory.genOnOffSwitch();

            Table turnTimerTable = new Table();
            turnTimerTable.setDebug(true);

            turnTimerTable.left();
            turnTimerTable.add(turnTimerLabel).height(60).width(420);
            turnTimerTable.right();
            turnTimerTable.add(turnTimerSwitch).padLeft(50);
            return turnTimerTable;
        }

        private Button[][] collegeButtons(int numberOfPlayers, final Label[] collegeLogos, final Label[] collegeNameLabels){
            Button[][] buttonList = new Button[numberOfPlayers][2];
            for(int i =0; i<numberOfPlayers; i++){
                Button collegeLeftButton = WidgetFactory.genCollegeLeftButton();
                final int finalI = i;
                collegeLeftButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        collegeNameLabels[finalI].setText(selectCollegeLeft(collegeNameLabels[finalI]));
                        collegeLogos[finalI].setStyle(WidgetFactory.genCollegeLabelStyle(collegeNameLabels[finalI].getText().toString()));
                    }
                });
                Button collegeRightButton = WidgetFactory.genCollegeRightButton();
                collegeRightButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        collegeNameLabels[finalI].setText(selectCollegeRight(collegeNameLabels[finalI]));
                        collegeLogos[finalI].setStyle(WidgetFactory.genCollegeLabelStyle(collegeNameLabels[finalI].getText().toString()));
                    }
                });

                buttonList[i][0] = collegeLeftButton;
                buttonList[i][1] = collegeRightButton;
            }
            return buttonList;
        }

        private TextField[] playerTextFields(int numberOfPlayers){
            TextField[] playerTextFieldList = new TextField[numberOfPlayers];
            for(int i =0; i<numberOfPlayers; i++){
                TextField playerName = WidgetFactory.playerNameTextField("Player " + i);
                playerTextFieldList[i] = playerName;
            }
            return playerTextFieldList;
        }

        private Table[] collegeTables(int numberOfPlayers, Table[] infoDisplay, Table[] collegeSelector){
            Table[] colllegeTableList = new Table[numberOfPlayers];
            for (int i = 0; i<numberOfPlayers; i++){
                Table collegeTable = new Table();
                collegeTable.background(new TextureRegionDrawable(new TextureRegion(new Texture("ui/HD-assets/Game-Setup-Name-Box.png"))));
                collegeTable.setDebug(true);
                collegeTable.add(infoDisplay[i]).expand().left().padLeft(20);
                collegeTable.add(collegeSelector[i]).padRight(60);

                colllegeTableList[i] = collegeTable;
            }
            return colllegeTableList;
        }


        private Table setUpCollegeTable(){
            //Initialise college name labels for all players
            Label[] collegeNameLabels = new Label[NUMBER_OF_PLAYERS];
            for(int n=0; n<NUMBER_OF_PLAYERS; n++) {
                Label collegeNameLabel = WidgetFactory.genNameBoxLabel("ALCUIN");
                collegeNameLabel.setAlignment(Align.left);
                collegeNameLabels[n] = collegeNameLabel;
            }

            //Initialise college logos for all players
            Label[] collegeLogos = new Label[NUMBER_OF_PLAYERS];
            for(int n=0; n<NUMBER_OF_PLAYERS; n++) {
                Label collegeLogo = new Label("", WidgetFactory.genCollegeLabelStyle(collegeNameLabels[n].getText().toString()));
                collegeLogos[n] = collegeLogo;
            }

            Button[][] cButtons = collegeButtons(NUMBER_OF_PLAYERS, collegeLogos, collegeNameLabels);
            TextField[] pTextFields = playerTextFields(NUMBER_OF_PLAYERS);

            //Initialise college selectors for all players
            Table[] collegeSelectors = new Table[NUMBER_OF_PLAYERS];
            for(int n=0; n<NUMBER_OF_PLAYERS; n++) {
                Table collegeSelector = new Table();
                collegeSelector.add(cButtons[n][0]).height(50).width(25);
                collegeSelector.add(collegeLogos[n]).height(80).width(100);
                collegeSelector.add(cButtons[n][1]).height(50).width(25);
                collegeSelectors[n] = collegeSelector;
            }

            //Initialise player name and college name display for all players
            Table[] infoDisplay = new Table[NUMBER_OF_PLAYERS];
            for(int n=0; n<NUMBER_OF_PLAYERS; n++) {
                Table playerInfo = new Table();
                playerInfo.add(pTextFields[n]).padBottom(10).width(220).left();
                playerInfo.row();
                playerInfo.add(collegeNameLabels[n]).height(20).width(220);
                infoDisplay[n] = playerInfo;
            }

            Table[]  cTables = collegeTables(NUMBER_OF_PLAYERS, infoDisplay, collegeSelectors);

            Table rightTable = new Table();
            for(int n=0; n<NUMBER_OF_PLAYERS; n++){
                rightTable.add(cTables[n]).padBottom(10).padTop(10);
                rightTable.row();
            }

            return rightTable;
        }


    private void setupUi() {
        TextButton startGameButton = WidgetFactory.genStartGameButton("START GAME");
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setGameScreen();
            }
        });


        table.background(new TextureRegionDrawable(new TextureRegion(new Texture("ui/HD-assets/Menu-Background.png"))));

        table.center();
        table.add(WidgetFactory.genTopBar("GAME SETUP")).colspan(2);

        table.row();
        table.left();
        table.add(setUpGameSetupTable()).expand();
        table.right();
        table.add(setUpCollegeTable()).expand();
        table.row();
        table.left();
        table.add(setUpTurnTimerTable()).expand();
        table.add(startGameButton).height(60).width(420);



        table.row();
        table.center();
        table.add(WidgetFactory.genBottomBar("MAIN MENU")).colspan(2);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        main.applyPreferences();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(Gdx.graphics.getDeltaTime());
        this.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
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

    }
}

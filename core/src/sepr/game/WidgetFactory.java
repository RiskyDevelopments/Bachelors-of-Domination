package sepr.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import sepr.game.utils.TurnPhaseType;

/**
 * class that generates widgets for using in the UI
 */
public class WidgetFactory {

    private static Texture basicButtonTexture;
    private static Texture mapGraphicTexture;
    private static Texture optionsGraphicTexture;

    private static Texture sliderBarTexture;
    private static Texture sliderKnobTexture;

    private static Texture textFieldCursorTexture;

    private static Texture onSwitchTexture;
    private static Texture offSwitchTexture;

    private static Texture gameHUDBottomBarRightPartTexture;
    private static Texture gameHUDTopBarTexture;
    private static Texture endPhaseBtnTexture;

    private static Texture playerLabelTexture;
    private static Texture playerLeftBtnTexture;
    private static Texture playerRightBtnTexture;
    private static Texture menuBtnLabelTexture;
    private static Texture collegeLeftBtnTexture;
    private static Texture collegeRightBtnTexture;
    private static Texture startGameBtnTexture;

    private static Texture menusTopBarLeftTexture;
    private static Texture menusTopBarRightTexture;

    private static Texture alcuinLogoTexture;
    private static Texture derwentLogoTexture;
    private static Texture halifaxLogoTexture;
    private static Texture hesEastLogoTexture;
    private static Texture jamesLogoTexture;
    private static Texture uniOfYorkLogoTexture;
    private static Texture vanbrughLogoTexture;
    private static Texture wentworthLogoTexture;

    private static BitmapFont fontBig;
    private static BitmapFont fontSmall;

    /**
     * initialises all the assets required for generating the UI components
     */
    public WidgetFactory() {
        setupFont();

        basicButtonTexture = new Texture("uiComponents/Menu-Button-Full.png");
        mapGraphicTexture = new Texture("uiComponents/Main-Menu-Map.png");
        optionsGraphicTexture = new Texture("uiComponents/General-Jack.png");

        sliderBarTexture = new Texture("uiComponents/sliderBar.png");
        sliderKnobTexture = new Texture("uiComponents/sliderKnob.png");

        textFieldCursorTexture = new Texture("uiComponents/textFieldCursor.png");

        onSwitchTexture = new Texture("uiComponents/On-Switch.png");
        offSwitchTexture = new Texture("uiComponents/Off-Switch.png");

        playerLabelTexture = new Texture("uiComponents/Player-Label.png");
        playerLeftBtnTexture = new Texture("uiComponents/Player-Left-Button-Full.png");
        playerRightBtnTexture = new Texture("uiComponents/Player-Right-Button-Full.png");
        menuBtnLabelTexture = new Texture("uiComponents/Menu-Button.png");
        collegeLeftBtnTexture = new Texture("uiComponents/College-Left-Button.png");
        collegeRightBtnTexture = new Texture("uiComponents/College-Right-Button.png");
        startGameBtnTexture = new Texture("uiComponents/Start-Game-Button-Full.png");

        menusTopBarLeftTexture = new Texture("uiComponents/MenusTopBarLeft.png");
        menusTopBarRightTexture = new Texture("uiComponents/MenusTopBarRight.png");

        gameHUDBottomBarRightPartTexture = new Texture("uiComponents/HUD-Bottom-Bar-Right-Part.png");
        gameHUDTopBarTexture = new Texture("uiComponents/HUD-Top-Bar.png");
        endPhaseBtnTexture = new Texture("uiComponents/End-Phase-Button.png");

        // load college logos
        alcuinLogoTexture = new Texture("logos/alcuin-logo.png");
        derwentLogoTexture = new Texture("logos/derwent-logo.png");
        halifaxLogoTexture = new Texture("logos/halifax-logo.png");
        hesEastLogoTexture = new Texture("logos/hes-east-logo.png");
        jamesLogoTexture = new Texture("logos/james-logo.png");
        uniOfYorkLogoTexture = new Texture("logos/uni-of-york-logo.png");
        vanbrughLogoTexture = new Texture("logos/vanbrugh-logo.png");
        wentworthLogoTexture = new Texture("logos/wentworth-logo.png");
    }

    /**
     * sets up the big and small fonts
     */
    private void setupFont() {
        FileHandle alteDinBig = new FileHandle("font/Alte-DIN-Big.fnt");
        FileHandle alteDinSmall = new FileHandle("font/Alte-DIN-Small.fnt");
        fontBig = new BitmapFont(alteDinBig);
        fontSmall = new BitmapFont(alteDinSmall);
    }

    /**
     * creates a button with the specified text on it which changes colour when clicked
     *
     * @param buttonText the text on the button
     * @return a button widget
     */
    public static TextButton genBasicButton(String buttonText) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); // create style for buttons to use
        style.up = new TextureRegionDrawable(new TextureRegion(basicButtonTexture, 0, 0, 857, 123)); // image for button to use in default state
        style.down = new TextureRegionDrawable(new TextureRegion(basicButtonTexture, 0, 123, 857, 123)); // image for button to use when pressed down
        style.font = fontSmall;

        return new TextButton(buttonText, style);
    }

    /**
     * creates a bar for the GUI which has a text label in the top left corner
     * used at the top of all menu screens
     *
     * @param text in the top left corner of the menu
     * @return the GUI bar with the specified label
     */
    public static Table genMenusTopBar(String text){
        Image menusTopBarLeft = new Image(new TextureRegionDrawable(new TextureRegion(menusTopBarLeftTexture)));
        Image menusTopBarRight = new Image(new TextureRegionDrawable(new TextureRegion(menusTopBarRightTexture)));

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = fontBig;
        Label textLabel = new Label(text, style);

        Table topBar = new Table();
        topBar.setDebug(false);
        topBar.left();
        topBar.add(menusTopBarLeft).height(60);
        topBar.add(textLabel).padRight(20).padLeft(20);
        topBar.add(menusTopBarRight).fillX().height(60);

        return topBar;
    }

    /**
     * creates a bar for the bottom of the GUI with an escape button in the bottom left, the on click action is configurable, and the game name is on the right
     *
     * @param buttonText the text on the escape button
     * @param changeListener action to be performed when the escape button is pressed
     * @return
     */
    public static Table genBottomBar(String buttonText, ChangeListener changeListener){

        Image leftPart = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("uiComponents/Left-Part-End-Bottom-Bar.png"))));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = fontSmall;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("uiComponents/Esc-Button-Bottom-Bar.png")));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("uiComponents/Esc-Button-Bottom-Bar.png")));
        final TextButton escButton = new TextButton(buttonText, buttonStyle);
        escButton.addListener(changeListener);

        Image centerPart = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("uiComponents/Center-Part-Bottom-Bar.png"))));


        Label.LabelStyle style = new Label.LabelStyle();
        style.font = fontSmall;
        style.background = new TextureRegionDrawable(new TextureRegion(new Texture("uiComponents/Right-Part-Bottom-Bar.png")));
        String text = "BACHELORS OF" + "\n" + "DOMINATION";
        Label textLabel = new Label(text, style);
        textLabel.setAlignment(0, 40);

        Table bottomBar = new Table();
        bottomBar.setDebug(false);
        bottomBar.left();
        bottomBar.add(leftPart).height(60).width(45).bottom();
        bottomBar.add(escButton).height((float) 51.5).width(190).bottom();
        bottomBar.add(centerPart).height(60).fillX().bottom();
        bottomBar.add(textLabel).height(120).width(280);

        return bottomBar;
    }

    /**
     *
     * @return an image widget of the map to be displayed on the main menu
     */
    public static Image genMapGraphic() {
        return new Image(mapGraphicTexture);
    }

    /**
     *
     * @return an image widget of a soldier to be displayed in the options screen
     */
    public static Image genOptionsGraphic() {
        return new Image(optionsGraphicTexture);
    }

    /**
     * gets the logo of the college associated with the college supplied
     *
     * @param college the college to get the logo for
     * @return a Drawable of the college's logo
     */
    public static Drawable genCollegeLogoDrawable(GameSetupScreen.CollegeName college) {
        switch (college) {
            case ALCUIN:
                return new TextureRegionDrawable(new TextureRegion(alcuinLogoTexture));
            case DERWENT:
                return new TextureRegionDrawable(new TextureRegion(derwentLogoTexture));
            case HALIFAX:
                return new TextureRegionDrawable(new TextureRegion(halifaxLogoTexture));
            case HES_EAST:
                return new TextureRegionDrawable(new TextureRegion(hesEastLogoTexture));
            case JAMES:
                return new TextureRegionDrawable(new TextureRegion(jamesLogoTexture));
            case UNI_OF_YORK:
                return new TextureRegionDrawable(new TextureRegion(uniOfYorkLogoTexture));
            case VANBRUGH:
                return new TextureRegionDrawable(new TextureRegion(vanbrughLogoTexture));
            case WENTWORTH:
                return new TextureRegionDrawable(new TextureRegion(wentworthLogoTexture));
        }
        return null;
    }

    /**
     *
     * @param college college to get logo Image off
     * @return an Image of the college logo
     */
    public static Image genCollegeLogoImage(GameSetupScreen.CollegeName college){
        return new Image(genCollegeLogoDrawable(college));
    }

    /**
     * generates a label with the specified text and background image
     *
     * @param labelText the text on the label
     * @param labelBackground the background of the label
     * @param alignment of the text in the label
     * @return a label with the specified text and background
     */
    private static Label getLabel(String labelText, Texture labelBackground, int alignment) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = fontSmall;
        style.background = new TextureRegionDrawable(new TextureRegion(labelBackground));

        Label label = new Label(labelText, style);
        label.setAlignment(alignment);

        return label;
    }

    /**
     * Generates the UI widget to be displayed at the bottom right of the in game GUI
     * @param labelText the text on the bar
     * @return a label that forms right part of the HUD's bottom bar
     */
    public static Label genGameHUDBottomBarRightPart(String labelText) {
        return getLabel(labelText, gameHUDBottomBarRightPartTexture, Align.center);
    }


    /**
     * creates a label used for selecting the player type in the Game Setup Screen
     * @param labelText text to be displayed on label
     * @return a label with the secified background and text
     */
    public static Label genPlayerLabel(String labelText) {
        return getLabel(labelText, playerLabelTexture, Align.center);
    }

    /**
     * creates a label with the specified text to be used in menus
     *
     * @param labelText text to be displayed on label
     * @return a label to be used in menus
     */
    public static Label genMenuLabel(String labelText) {
        return getLabel(labelText, menuBtnLabelTexture, Align.left);
    }

    /**
     * creates label, with a transparent background, for displaying the name of a college in the game setup screen
     *
     * @param collegeName the college name
     * @return a label with no background and the specified text
     */
    public static Label genTransparentLabel(String collegeName) {
        return getLabel(collegeName, new Texture(1, 1, Pixmap.Format.RGBA8888), Align.left);
    }

    /**
     * creates a textfield for the player to input their name into
     *
     * @param name the initial value of the player name
     * @return a textfield with the initial value name
     */
    public static TextField playerNameTextField(String name){

        TextField.TextFieldStyle  style = new TextField.TextFieldStyle();
        style.font = fontSmall;
        style.fontColor = new Color(Color.WHITE);
        style.cursor = new TextureRegionDrawable(new TextureRegion(textFieldCursorTexture));

        return new TextField(name, style);
    }

    /**
     *
     * @return left arrow button for changing the player type
     */
    public static Button genPlayerLeftButton() {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(playerLeftBtnTexture, 0, 0, 111, 123));
        style.down = new TextureRegionDrawable(new TextureRegion(playerLeftBtnTexture, 0, 123, 111, 123));

        return new Button(style);
    }

    /**
     *
     * @return right arrow button for changing the player type
     */
    public static Button genPlayerRightButton() {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(playerRightBtnTexture, 0, 0, 111, 123));
        style.down = new TextureRegionDrawable(new TextureRegion(playerRightBtnTexture, 0, 123, 111, 123));

        return new Button(style);
    }

    /**
     *
     * @return left arrow button for changing the college type
     */
    public static Button genCollegeLeftButton() {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(collegeLeftBtnTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(collegeLeftBtnTexture));

        return new Button(style);
    }

    /**
     *
     * @return right arrow button for changing the college type
     */
    public static Button genCollegeRightButton() {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(collegeRightBtnTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(collegeRightBtnTexture));

        return new Button(style);
    }

    /**
     *
     * @return a button with the text START GAME
     */
    public static TextButton genStartGameButton() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(startGameBtnTexture, 0, 0, 723, 123));
        style.down = new TextureRegionDrawable(new TextureRegion(startGameBtnTexture, 0, 123, 723, 123));
        style.font = fontSmall;

        return new TextButton("START GAME",style);
    }

    /**
     *
     * @return a button with the text END PHASE
     */
    public static TextButton genEndPhaseButton(){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(endPhaseBtnTexture, 0,0, 348, 123));
        style.down = new TextureRegionDrawable(new TextureRegion(endPhaseBtnTexture, 0,123, 348, 123));
        style.font = fontSmall;

        return new TextButton("END PHASE", style);
    }

    /**
     * creates a table containing the components to make up the top bar of the HUD
     *
     * @param turnPhase the phase this bar is for
     * @param gameScreen for creating the leave game dialog
     * @return the top bar of the HUD for the specified phase
     */
    public static Table genGameHUDTopBar(TurnPhaseType turnPhase, final GameScreen gameScreen) {
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = fontSmall;
        TextButton exitButton = new TextButton("QUIT", btnStyle);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DialogFactory.leaveGameDialogBox(gameScreen, actor.getStage());
            }
        });

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = fontSmall;

        String text = "";
        switch (turnPhase) {
            case REINFORCEMENT:
                text = "REINFORCEMENT  -  Attack  -  Movement";
                break;
            case ATTACK:
                text = "Reinforcement  -  ATTACK  -  Movement";
                break;
            case MOVEMENT:
                text = "Reinforcement  -  Attack  -  MOVEMENT";
                break;
        }

        Label label = new Label(text, style);
        label.setAlignment(Align.center);

        Table table = new Table();
        table.background(new TextureRegionDrawable(new TextureRegion(gameHUDTopBarTexture)));
        table.left().add(exitButton).padRight(190).padLeft(20);
        table.add(label).height(60);

        return table;
    }


    /**
     * creates a selector widget with the options specified by the passed string array
     *
     * @param items the list of items that may be selected
     * @return a selector widget styled and using the items provided
     */
    public static SelectBox<String> genStyledSelectBox(String[] items) {
        SelectBox.SelectBoxStyle style = new SelectBox.SelectBoxStyle();
        style.font = fontSmall;
        style.listStyle = new List.ListStyle(fontSmall, Color.BLACK, Color.BLACK, new TextureRegionDrawable(new TextureRegion(sliderBarTexture)));
        style.scrollStyle = new ScrollPane.ScrollPaneStyle(new TextureRegionDrawable(new TextureRegion(sliderKnobTexture)), new TextureRegionDrawable(new TextureRegion(sliderKnobTexture)), new TextureRegionDrawable(new TextureRegion(sliderKnobTexture)), new TextureRegionDrawable(new TextureRegion(sliderKnobTexture)), new TextureRegionDrawable(new TextureRegion(sliderKnobTexture)));

        SelectBox<String> selectBox = new SelectBox<String>(style);
        selectBox.setItems(items);
        return selectBox;
    }

    /**
     *
     * @return a toggleable on off switch widget
     */
    public static CheckBox genOnOffSwitch() {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(onSwitchTexture));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(offSwitchTexture));

        return new CheckBox(null, style);
    }

    /**
     * Generates a slider widget that is styled for use in the options screen
     * @return a slider
     */
    public static Slider genStyledSlider() {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(sliderBarTexture));
        style.knob = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));
        style.knobDown = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));
        style.knobOver = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));

        return new Slider(0f, 1f, 0.01f, false, style);
    }

    /**
     *
     * @return a new instance of the small font
     */
    public static BitmapFont getFontSmall() {
        FileHandle alteDinSmall = new FileHandle("font/Alte-DIN-Small.fnt");
        return new BitmapFont(alteDinSmall);
    }
}

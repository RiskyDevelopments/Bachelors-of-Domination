package sepr.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Dom's Surface Mark 2 on 28/11/2017.
 */
public class WidgetFactory {
    private static Texture basicButtonTexture;
    private static Texture topBarTexture;
    private static Texture bottomBarTexture;
    private static Texture mapGraphicTexture;
    private static Texture labelTexture;
    private static Texture optionsGraphicTexture;

    private static Texture sliderBarTexture;
    private static Texture sliderKnobTexture;

    private static Texture onSwitchTexture;
    private static Texture offSwitchTexture;

    public WidgetFactory() {
        basicButtonTexture = new Texture("ui/buttons.png");
        topBarTexture = new Texture("ui/topBar.png");
        bottomBarTexture = new Texture("ui/bottomBar.png");
        mapGraphicTexture = new Texture("ui/mapGraphic.png");
        labelTexture = new Texture("ui/labelTexture.png");
        optionsGraphicTexture = new Texture("ui/optionsGraphic.png");

        sliderBarTexture = new Texture("ui/sliderBar.png");
        sliderKnobTexture = new Texture("ui/sliderKnob.png");

        onSwitchTexture = new Texture("ui/onSwitch.png");
        offSwitchTexture = new Texture("ui/offSwitch.png");
    }

    public static TextButton genBasicButton(String buttonText) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); // create style for buttons to use
        style.up = new TextureRegionDrawable(new TextureRegion(basicButtonTexture, 0, 0, 400, 150)); // image for button to use in default state
        style.down = new TextureRegionDrawable(new TextureRegion(basicButtonTexture, 0, 150, 400, 150)); // image for button to use when pressed down
        style.font = new BitmapFont(); // set button font to the default Bitmap Font

        return new TextButton(buttonText, style);
    }

    public static Image genTopBarGraphic() {
        return new Image(topBarTexture);
    }

    public static Image genBottomBarGraphic() {
        return new Image(bottomBarTexture);
    }

    public static Image genMapGraphic() {
        return new Image(mapGraphicTexture);
    }

    public static Image genOptionsGraphic() {
        return new Image(optionsGraphicTexture);
    }

    public static Label genStyledLabel(String labelText) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.background = new TextureRegionDrawable(new TextureRegion(labelTexture));

        return new Label(labelText, style);
    }

    /**
     *
     * @param items the list of items that may be selected
     * @return a selector widget styled and using the items provided
     */
    public static SelectBox<String> genStyledSelectBox(String[] items) {
        SelectBox.SelectBoxStyle style = new SelectBox.SelectBoxStyle();
        style.font = new BitmapFont();
        style.listStyle = new List.ListStyle(new BitmapFont(), Color.BLACK, Color.BROWN, new TextureRegionDrawable(new TextureRegion(sliderBarTexture)));
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
}

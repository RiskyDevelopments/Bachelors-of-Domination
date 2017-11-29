package sepr.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Dom's Surface Mark 2 on 28/11/2017.
 */
public class WidgetFactory {
    private static Texture basicButtonTexture;
    private static Texture topBarTexture;
    private static Texture bottomBarTexture;
    private static Texture mapTexture;

    public WidgetFactory() {
        basicButtonTexture = new Texture("ui/buttons.png");
        topBarTexture = new Texture("ui/topBar.png");
        bottomBarTexture = new Texture("ui/bottomBar.png");
        mapTexture = new Texture("ui/mapGraphic.png");
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
        return new Image(mapTexture);
    }
}

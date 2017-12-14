package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public class MenuScreen implements Screen {
    private Main main;
    private Stage stage;
    private Table table; // table for inserting ui widgets into

    public MenuScreen(final Main main) {
        this.main = main;

        this.stage = new Stage();
        this.stage.setViewport(new ScreenViewport());

        this.table = new Table();
        this.table.setFillParent(true); // make ui table fill the entire screen
        this.stage.addActor(table);
        this.table.setDebug(false); // enable table drawing for ui debug

        Texture buttons = new Texture("ui/buttons.png"); // texture sheet for buttons
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); // create style for buttons to use
        style.up = new TextureRegionDrawable(new TextureRegion(buttons, 0, 0, 400, 150)); // image for button to use in default state
        style.down = new TextureRegionDrawable(new TextureRegion(buttons, 0, 150, 400, 150)); // image for button to use when pressed down
        style.font = new BitmapFont(); // set button font to the default Bitmap Font

        final TextButton startGameBtn = new TextButton("New Game", style);
        final TextButton loadGameBtn = new TextButton("Load Game", style);
        final TextButton optionsBtn = new TextButton("Options", style);
        final TextButton exitBtn = new TextButton("Exit", style);

        final Image topBar = new Image(new Texture("ui/topBar.png"));
        final Image bottomBar = new Image(new Texture("ui/bottomBar.png"));
        final Image mapGraphic = new Image(new Texture("ui/mapGraphic.png"));

        /* Create sub-table for all the menu buttons */
        Table btnTable = new Table();
        btnTable.setDebug(true);
        btnTable.left();
        btnTable.add(startGameBtn).pad(30);

        btnTable.row();
        btnTable.left();
        btnTable.add(loadGameBtn).pad(30);

        btnTable.row();
        btnTable.left();
        btnTable.add(optionsBtn).pad(30);

        btnTable.row();
        btnTable.left();
        btnTable.add(exitBtn).pad(30);
        /* Sub-table complete */

        table.background(new TextureRegionDrawable(new TextureRegion(new Texture("ui/background.png"))));

        // insert top bar graphic
        table.center();
        table.add(topBar).colspan(2);

        // insert button table
        table.row();
        table.left();
        table.add(btnTable);

        table.right();
        table.add(mapGraphic).pad(30);

        table.row();
        table.center();
        table.add(bottomBar).colspan(2);

        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setGameScreen();
            }
        });

        loadGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Go to options");
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setOptionsScreen();
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        this.stage.dispose();
    }
}

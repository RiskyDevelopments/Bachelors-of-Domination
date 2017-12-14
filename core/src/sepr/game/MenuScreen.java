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
        this.setupUi();
    }

    private Table setupMenuTable() {
        final TextButton startGameBtn = WidgetFactory.genBasicButton("New Game");
        final TextButton loadGameBtn = WidgetFactory.genBasicButton("Load Game");
        final TextButton optionsBtn = WidgetFactory.genBasicButton("Options");
        final TextButton exitBtn = WidgetFactory.genBasicButton("Exit");

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

        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setGameScreen();
            }
        });

        loadGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Load game");
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

        /* Sub-table complete */
        return btnTable;
    }

    private void setupUi() {
        table.background(new TextureRegionDrawable(new TextureRegion(new Texture("ui/background.png"))));

        table.center();
        table.add(WidgetFactory.genTopBarGraphic()).colspan(2).fillX();

        table.row();
        table.left();
        table.add(setupMenuTable()).expand();

        table.right();
        table.add(WidgetFactory.genMapGraphic()).pad(30);

        table.row();
        table.center();
        table.add(WidgetFactory.genBottomBarGraphic()).colspan(2).fillX();
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
        this.stage.dispose();
    }
}

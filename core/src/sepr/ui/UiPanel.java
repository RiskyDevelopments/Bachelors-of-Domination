package sepr.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Shape2D;

/**
 * Created by Dom's Surface Mark 2 on 26/11/2017.
 */
public class UiPanel implements InputProcessor{
    private static int nextId = 0;
    private int id;
    private Shape2D shape;

    public UiPanel() {
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }

    public boolean inFocus(float x, float y) {
        return shape.contains(x, y);
    }

    public void processInput() {
        Gdx.input.setInputProcessor(this);
    }

    public void render(SpriteBatch batch) {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
        return false;
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

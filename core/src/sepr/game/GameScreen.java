package sepr.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;

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
    private Integer[] turnOrder; // array of player ids in order of players' turns;
    private int currentPlayer; // index of current player

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
        this.gameplayCamera.translate(1920/2, 1080/2, 0);
        this.mapBackground = new Texture("ui/mapBackground.png");

        this.players = players;

        this.keysDown = new HashMap<Integer, Boolean>();
        this.keysDown.put(Input.Keys.W, false);
        this.keysDown.put(Input.Keys.A, false);
        this.keysDown.put(Input.Keys.S, false);
        this.keysDown.put(Input.Keys.D, false);

        this.turnTimerEnabled = turnTimerEnabled;
        this.turnTimerPaused = false;
        this.maxTurnTime = maxTurnTime;
        this.turnTimeElapsed = 0;
        this.turnOrder = this.players.keySet().toArray(new Integer[0]);
        this.currentPlayer = 0;

        setupUi();

        allocateSectors();
        playGame();
    }

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

    private void playGame() {

    }

    /**
     * Allocate sectors to each player in a balanced mannor
     */
    private void allocateSectors() {

    }

    /**
     *
     * @param playerId player's whos turn it is to be carried out
     */
    private void executePlayerTurn(int playerId) {

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

    }

    /**
     * Reads the given string and setsup the game state from this
     * @param gameState
     */
    private void loadGameState(String gameState) {

    }

    private void controlCamera() {
        if (this.keysDown.get(Input.Keys.W)) {
            this.gameplayCamera.translate(0, 4, 0);
        }
        if (this.keysDown.get(Input.Keys.S)) {
            this.gameplayCamera.translate(0, -4, 0);
        }
        if (this.keysDown.get(Input.Keys.A)) {
            this.gameplayCamera.translate(-4, 0, 0);
        }
        if (this.keysDown.get(Input.Keys.D)) {
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
        this.stage.act(Gdx.graphics.getDeltaTime());

        // render UI
        this.stage.draw();
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
        if (keycode == Input.Keys.W) {
            keysDown.put(Input.Keys.W, true);
        }
        if (keycode == Input.Keys.S) {
            keysDown.put(Input.Keys.S, true);
        }
        if (keycode == Input.Keys.A) {
            keysDown.put(Input.Keys.A, true);
        }
        if (keycode == Input.Keys.D) {
            keysDown.put(Input.Keys.D, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            keysDown.put(Input.Keys.W, false);
        }
        if (keycode == Input.Keys.S) {
            keysDown.put(Input.Keys.S, false);
        }
        if (keycode == Input.Keys.A) {
            keysDown.put(Input.Keys.A, false);
        }
        if (keycode == Input.Keys.D) {
            keysDown.put(Input.Keys.D, false);
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
        map.detectSectorClick(screenX, screenY); // does not currently handle changes to cam zoom or pos
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

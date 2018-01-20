package sepr.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * main class for controlling the game
 */
public class GameScreen implements Screen, InputProcessor{
    public static final int NEUTRAL_PLAYER_ID = 4;

    private Main main; // main stored for switching between screens

    private TurnPhaseType currentPhase = TurnPhaseType.REINFORCEMENT; // first phase of game is reinforcement

    private HashMap<TurnPhaseType, Phase> phases;

    private SpriteBatch gameplayBatch;
    protected OrthographicCamera gameplayCamera;
    private Viewport gameplayViewport;
    private Texture mapBackground;

    private Map map;
    private HashMap<Integer, Player> players; // player id mapping to the relevant player

    private HashMap<Integer, Boolean> keysDown; // mapping from key, (Input.Keys), to whether it has been pressed down

    // timer settings
    private boolean turnTimerEnabled;
    private int maxTurnTime;
    private long turnTimeStart;

    private List<Integer> turnOrder; // array of player ids in order of players' turns;
    private int currentPlayer; // index of current player in turnOrder list

    private boolean gameSetup = false; // true once setupGame has been called

    /**
     * Performs the game's initial setup
     * setupGame must be called before a game is ready to be played
     * @param main used to change screen
     */

    public GameScreen(Main main) {
        this.main = main;

        this.gameplayBatch = new SpriteBatch();
        this.gameplayCamera = new OrthographicCamera();
        this.gameplayViewport = new ScreenViewport(gameplayCamera);

        this.mapBackground = new Texture("uiComponents/mapBackground.png");

        // setup hashmap to check which keys were previously pressed
        this.keysDown = new HashMap<Integer, Boolean>();
        this.keysDown.put(Input.Keys.UP, false);
        this.keysDown.put(Input.Keys.LEFT, false);
        this.keysDown.put(Input.Keys.DOWN, false);
        this.keysDown.put(Input.Keys.RIGHT, false);
    }

    /**
     * sets up a new game
     * @param players HashMap of the players in this game
     * @param turnTimerEnabled should players turns be limited
     * @param maxTurnTime time elapsed in current turn, irrelevant if turn timer not enabled
     */
    public void setupGame(HashMap<Integer, Player> players, boolean turnTimerEnabled, int maxTurnTime, boolean allocateNeutralPlayer) {
        this.players = players;
        this.turnOrder = new ArrayList<Integer>();
        for (Integer i : players.keySet()) {
            if (players.get(i).getPlayerType() != PlayerType.NEUTRAL_AI) { // don't add the neutral player to the turn order
                this.turnOrder.add(i);
            }
        }

        this.turnTimerEnabled = turnTimerEnabled;
        this.maxTurnTime = maxTurnTime;
        this.turnTimeStart = System.currentTimeMillis();

        this.map = new Map(this.players, allocateNeutralPlayer);

        // create the game phases and add them to the phases hashmap
        this.phases = new HashMap<TurnPhaseType, Phase>();
        this.phases.put(TurnPhaseType.REINFORCEMENT, new PhaseReinforce(this, map));
        this.phases.put(TurnPhaseType.ATTACK, new PhaseAttack(this, map));
        this.phases.put(TurnPhaseType.MOVEMENT, new PhaseMovement(this, map));

        gameSetup = true;
    }

    /**
     * Input keys for controlling the game camera
     */
    private void updateInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(phases.get(currentPhase));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * checks if game is over by checking how many players are in the turn order, if 1 then player has won
     * @return true if game is over else false
     */
    private boolean isGameOver() {
        return turnOrder.size() <= 1; // game is over if only one player is in the turn order
    }

    /**
     *
     * @return time remaining in turn in seconds
     */
    public int getTurnTimeRemaining(){
        return maxTurnTime - (int)((System.currentTimeMillis() - turnTimeStart) / 1000);
    }

    /**
     *
     * @param id of the player object that is wanted
     * @return the player whose id matches the given one
     * @throws IllegalArgumentException if the supplied id does not match any of the players
     */
    protected Player getPlayerById(int id) throws IllegalArgumentException {
        if (!players.containsKey(id)) throw new IllegalArgumentException("Cannot fetch player as id: " + id + " does not exist");
        return players.get(id);
    }

    /**
     * removes all players who have 0 sectors from the turn order
     */
    private void removeEliminatedPlayers() {
        List<Integer> playerIdsToRemove = new ArrayList<Integer>();
        for (Integer i : turnOrder) {
            boolean hasSector = false;
            for (Integer j : map.getSectorIds()) {
                if (map.getSectorById(j).getOwnerId() == i) {
                    hasSector = true; // sector owned by player i found
                    break; // only need one sector to remain in turn order so can break once one found
                }
            }
            if (!hasSector) { // player has no sectors so remove them from the game
                playerIdsToRemove.add(i);
                System.out.println(i);
            }
        }

        if (playerIdsToRemove.size() > 0) {
            turnOrder.removeAll(playerIdsToRemove);
            String[] playerNames = new String[playerIdsToRemove.size()];
            for (int i = 0; i < playerIdsToRemove.size(); i++) {
                playerNames[i] = players.get(playerIdsToRemove.get(i)).getPlayerName();
            }

            DialogFactory.playersOutDialog(playerNames, phases.get(currentPhase)); // display which players have been eliminated
            if (isGameOver()) {
                gameOver();
            }
        }
    }

    /**
     * This method is used for progression through the phases of a turn evaluating the currentPhase case label
     */
    protected void nextPhase() {
        this.phases.get(currentPhase).endPhase();

        switch (currentPhase) {
            case REINFORCEMENT:
                currentPhase = TurnPhaseType.ATTACK;
                break;
            case ATTACK:
                currentPhase = TurnPhaseType.MOVEMENT;
                break;
            case MOVEMENT:
                currentPhase = TurnPhaseType.REINFORCEMENT;
                nextPlayer();
                break;
        }
        this.updateInputProcessor();
        this.phases.get(currentPhase).enterPhase(players.get(currentPlayer));
        removeEliminatedPlayers();

    }

    /**
     * Called when the player ends the MOVEMENT phase of their turn to advance the game to the next Player's turn
     */
    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == turnOrder.size()) {
            currentPlayer = 0;
        }

        resetCameraPosition();

        if (this.turnTimerEnabled) {
            this.turnTimeStart = System.currentTimeMillis();
        }
    }

    /**
     * method called when one player owns all the sectors in the map
     * @throws RuntimeException if there is more than one player in the turn order when gameOver is called
     */
    private void gameOver() throws RuntimeException {
        if (turnOrder.size() == 0) { // neutral player has won
            DialogFactory.basicDialogBox("Game Over!", "The Neutral Player has won.", phases.get(currentPhase));
        } else if (turnOrder.size() != 1) {
            throw new RuntimeException("Game Over called but more than one player in turn order");
        }
        int winnerId = turnOrder.get(0); // winner will be the only player in the turn order list
        DialogFactory.gameOverDialog(players.get(winnerId).getPlayerName(), players.get(winnerId).getCollegeName().getCollegeName(), main, phases.get(currentPhase));
    }

    protected SpriteBatch getGameplayBatch() {
        return this.gameplayBatch;
    }

    /**
     * moves the camera in the appropriate direction if the corresponding arrow key is down
     */
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

    /**
     * draws a background image behind the map and UI
     */
    private void renderBackground() {
        Vector3 mapDrawPos = gameplayCamera.unproject(new Vector3(0, Gdx.graphics.getHeight(), 0));
        gameplayBatch.draw(mapBackground, mapDrawPos.x, mapDrawPos.y, gameplayCamera.viewportWidth * gameplayCamera.zoom, gameplayCamera.viewportHeight * gameplayCamera.zoom );
    }

    /**
     * changes the screen currently being displayed to the menu
     */
    public void openMenu() {
        main.setMenuScreen();
    }

    /**
     * re-centres the camera and sets the zoom level back to default
     */
    private void resetCameraPosition() {
        this.gameplayCamera.position.x = 1920/2;
        this.gameplayCamera.position.y = 1080/2;
        this.gameplayCamera.zoom = 1;
    }

    /**
     * called once screen is setup to initialise the first phase of the game
     */
    public void startGame() {
        this.phases.get(currentPhase).enterPhase(players.get(currentPlayer));
        resetCameraPosition();
    }

    @Override
    public void show() {
        this.updateInputProcessor();
    }

    /**
     * updates the game and renders it to the screen
     * @throws RuntimeException when method called before the game is setup
     * @param delta time elapsed between this and the previous update
     */
    @Override
    public void render(float delta) {
        if (!gameSetup) throw new RuntimeException("Game must be setup before attempting to play it"); // throw exception if attempt to run game before its setup
        this.controlCamera(); // move camera

        gameplayCamera.update();
        gameplayBatch.setProjectionMatrix(gameplayCamera.combined);

        gameplayBatch.begin(); // begin rendering

        renderBackground(); // draw the background of the game

        map.draw(gameplayBatch); // draw the map
        gameplayBatch.end(); // stop rendering


        if (this.turnTimerEnabled) { // update the timer display, if it is enabled
            this.phases.get(currentPhase).setTimerValue(getTurnTimeRemaining());
        }
        this.phases.get(currentPhase).act(delta); // update the stage of the current phase
        this.phases.get(currentPhase).draw(); // draw the phase UI

        if (this.turnTimerEnabled && (getTurnTimeRemaining() <= 0)) { // goto the next player's turn if the timer is enabled and they have run out of time
            nextPlayer();
        }
    }

    @Override
    public void resize(int width, int height) {
        for (Stage stage : phases.values()) {
            stage.getViewport().update(width, height);
            stage.getCamera().viewportWidth = width;
            stage.getCamera().viewportHeight = height;
            stage.getCamera().position.x = width/2;
            stage.getCamera().position.y = height/2;
            stage.getCamera().update();
        }

        this.gameplayViewport.update(width, height);
        this.gameplayCamera.viewportWidth = width;
        this.gameplayCamera.viewportHeight = height;
        this.gameplayCamera.translate(1920/2, 1080/2, 0);
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

    }

    /**
     * converts a point on the screen to a point in the world, with respect to the gameplay camera
     * @param screenX screen point X
     * @param screenY screen point Y
     * @return the world coordinates as a vector
     */
    public Vector2 screenToWorldCoord(int screenX, int screenY) {
        float worldX = gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).x;
        float worldY = (gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).y - Gdx.graphics.getHeight()) * -1;
        return new Vector2(worldX, worldY);
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
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 worldCoords = screenToWorldCoord(screenX, screenY);

        Sector hoveredSector = map.getSectorById(map.detectSectorContainsPoint((int)worldCoords.x, (int)worldCoords.y));
        phases.get(currentPhase).setBottomBarText(hoveredSector); // update the bottom bar of the UI with the details of the sector currently hovered over by the mouse
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if ((gameplayCamera.zoom > 0.5 && amount < 0) || (gameplayCamera.zoom < 1.5 && amount > 0)) { // if the mouse scrolled zoom in/out
            gameplayCamera.zoom += amount * 0.03f;
        }
        return true;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
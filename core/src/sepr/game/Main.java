package sepr.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import java.util.HashMap;

public class Main extends Game implements ApplicationListener {
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private OptionsScreen optionsScreen;

	/**
	 * Setup the screens and set the first screen as the menu
	 */
	@Override
	public void create () {
		new WidgetFactory(); // setup widget factory for generating UI components

		HashMap<Integer, Player> players = new HashMap<Integer, Player>();
		players.put(0, new Player(0, "x", Color.BLUE) {
			@Override
			protected void processAttackPhase() {

			}

			@Override
			protected void processMovePhase() {

			}

			@Override
			protected int processAllocationPhase(int amount) {
				return 0;
			}
		});
		players.put(1, new Player(1, "y", Color.RED) {
			@Override
			protected void processAttackPhase() {

			}

			@Override
			protected void processMovePhase() {

			}

			@Override
			protected int processAllocationPhase(int amount) {
				return 0;
			}
		});
		players.put(2, new Player(2, "z", Color.GREEN) {
			@Override
			protected void processAttackPhase() {

			}

			@Override
			protected void processMovePhase() {

			}

			@Override
			protected int processAllocationPhase(int amount) {
				return 0;
			}
		});

		this.menuScreen = new MenuScreen(this);
		this.gameScreen = new GameScreen(this, players, false, 100);
		this.optionsScreen = new OptionsScreen(this);

		this.setMenuScreen();
	}

	public void setMenuScreen() {
		this.setScreen(menuScreen);
	}

	public void setGameScreen() {
		this.setScreen(gameScreen);
	}

	public void setOptionsScreen() {
		this.setScreen(optionsScreen);
	}

	/**
	 * Applies the players options preferences
	 * Sets the
	 *      Music Volume
	 *      FX Volume
	 *      Screen Resolution
	 *      Fullscreen
	 *      Colourblind Mode
	 * A default setting should be applied for any missing preferences
	 */
	public void applyPreferences() {
		Preferences prefs = Gdx.app.getPreferences(OptionsScreen.PREFERENCES_NAME);
		AudioHandler.setMusicPercentage(prefs.getFloat(OptionsScreen.MUSIC_VOL_PREF));
		AudioHandler.setFxPercentage(prefs.getFloat(OptionsScreen.FX_VOL_PREF));

		int screenWidth = prefs.getInteger(OptionsScreen.RESOLUTION_WIDTH_PREF, 1920);
		int screenHeight = prefs.getInteger(OptionsScreen.RESOLUTION_HEIGHT_PREF, 1080);
		Gdx.graphics.setWindowedMode(screenWidth, screenHeight);

		if (prefs.getBoolean(OptionsScreen.FULLSCREEN_PREF)) {
			// change game to fullscreen
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		menuScreen.dispose();
		gameScreen.dispose();
	}
}

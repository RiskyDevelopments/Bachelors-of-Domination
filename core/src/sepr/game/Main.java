package sepr.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

		this.menuScreen = new MenuScreen(this);
		this.gameScreen = new GameScreen(this, new HashMap<Integer, Player>(), false, 100);
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

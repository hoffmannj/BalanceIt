package com.hofi.game.balance;

import java.util.Hashtable;

import com.hofi.game.balance.interfaces.GameScreen;
import com.hofi.game.balance.screens.EmptyScreen;
import com.hofi.game.balance.screens.Leaderboard;
import com.hofi.game.balance.screens.Level1;
import com.hofi.game.balance.screens.MainMenu;
import com.hofi.game.balance.screens.SplashScreen;

public class GamePlayManager {
	@SuppressWarnings("rawtypes")
	private Hashtable<String, Class> screens;
	private GameScreen current = null;
	private GameScreen empty;
	private String nextScreen = "", currentScreen = "";
	public int levelsCount = 16;
	private Object lockObj = new Object();

	private final String strLevel = "level_";
	private final String strGameOver = "mainmenu";

	@SuppressWarnings("rawtypes")
	public GamePlayManager() {
		// initialize screens list with GameScreen classes
		empty = new EmptyScreen();
		screens = new Hashtable<String, Class>();
		screens.put("empty", EmptyScreen.class);
		screens.put("splashscreen", SplashScreen.class);
		screens.put("mainmenu", MainMenu.class);
		screens.put("leaderboard", Leaderboard.class);
		screens.put("level_1", Level1.class);
	}

	private void initCurrent() {
		setCurrentScreen("splashscreen");
	}

	public void iniFromGameState() {
		setCurrentScreen(Common.gameState.screenName);
	}

	public GameScreen getCurrent() {
		synchronized (lockObj) {
			if (current == null)
				initCurrent();
			return current;
		}
	}

	private void cleanUpCurrent() {
		if (current == null)
			return;
		Common.mainContactHandler.clearHandlers();
		current.cleanUp();
		Common.clearGameObjects();
		Common.clearUIObjects();
		Common.textures.clearTextures();
	}

	public void setCurrentScreen(String screenName) {
		synchronized (lockObj) {
			nextScreen = screenName;
		}
	}

	public void forceScreenChange() {
		synchronized (lockObj) {
			currentScreen = "";
		}
	}

	public void update(boolean fromState) {
		synchronized (lockObj) {
			if (nextScreen.equalsIgnoreCase(currentScreen))
				return;
			cleanUpCurrent();
			if (!screens.containsKey(nextScreen)) {
				if (nextScreen.startsWith(strLevel)) {
					nextScreen = strGameOver;
				} else {
					current = empty;
					return;
				}
			}

			try {
				currentScreen = nextScreen;
				Common.clearGameObjects();
				Common.clearUIObjects();
				Common.textures.clearTextures();
				Common.mainContactHandler.clearHandlers();
				GameScreen gs = (GameScreen) screens.get(currentScreen)
						.newInstance();
				gs.init();
				if (fromState)
					gs.initFromGameState();
				gs.updateGameState(false);
				current = gs;
				return;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			current = empty;
			return;
		}
	}

	public String getCurrentScreen() {
		return currentScreen;
	}
}

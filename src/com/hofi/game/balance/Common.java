package com.hofi.game.balance;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.GameHelper;
import com.hofi.game.balance.interfaces.ButtonHandler;
import com.hofi.game.balance.interfaces.GameObject;
import com.hofi.game.balance.interfaces.NewButtonHandler;

public class Common {
	public static NewMainActivity mainActivity;
	public static MyAssetManager assetManager;
	public static MyGLSurfaceView glView;
	public static Input input;
	public static InputManager inputManager;
	public static Textures textures;
	public static World world;
	public static PhysicsObjectFactory physicsObjectFactory;
	public static UIObjects uiObjects;
	public static GameObjects gameObjects;
	public static GamePlayManager gamePlayManager;
	public static MainContactHandler mainContactHandler;
	public static GameState gameState;
	public static AppPreferences appPreferences;
	public static GameHelper mHelper;
	public static StopWatch stopWatch;

	public static ShaderHandles shaderHandles;

	public static boolean isSignedIn = false;

	public static int fps;
	public static float screenWidth;
	public static float screenHeight;
	public static long previousHighScore;
	public static long highScore;
	public static int currentLeaderboardType = LeaderboardVariant.COLLECTION_PUBLIC;
	public static long lastConnectTry = 0;

	public static final float Mult = 100.0f;
	public static final Vec2 G = new Vec2(0, -25f);
	public static final float RadDeg = (float) (Math.PI / 180.0f);
	public static final int maxKicks = 3;

	private final static String strUp = "up";
	private final static String strDown = "down";

	public static final String highScoreKey = "balanceHighScore";
	public final static String leaderboard_ID = "CgkIma_ahNobEAIQAA";
	public static int REQUEST_LEADERBOARD = 0x1000;

	private static Vec2 worldPosition = new Vec2();

	public static Vec2 getWorldPosition(float x, float y) {
		float xx = (480f / screenWidth) * x;
		float yy = 800f - (800f / screenHeight) * y;

		worldPosition.set(xx, yy);
		return worldPosition;
	}

	public static <T> T instantiate(final String className, final Class<T> type) {
		try {
			return type.cast(Class.forName(className).newInstance());
		} catch (final InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void updatePhysicsWorld() {
		world.step(1.0f / 60.0f, 10, 25);
	}

	public static void clearUIObjects() {
		uiObjects.clear();
	}

	public static UIObject addButton(Object owner, Vec2 position,
			String btnName, ButtonHandler handler) {
		return uiObjects.addObject(new Button(owner, position, btnName + strUp,
				btnName + strDown).setHandler(handler));
	}

	public static UIObject addNewButton(Object owner, Vec2 position,
			String text, NewButtonHandler handler) {
		return uiObjects.addObject(new NewButton(owner, position, text)
				.setHandler(handler));
	}

	public static void clearGameObjects() {
		gameObjects.clear();
	}

	public static GameObject addGameObject(String objName, GameObject obj) {
		return gameObjects.addObject(objName, obj);
	}

	public static GameObject getGameObject(String objName) {
		return gameObjects.getObject(objName);
	}

	public static void saveHighScore() {
		long hs = appPreferences.getLong(highScoreKey);
		if (gameState.score > hs) {
			appPreferences.putLong(highScoreKey, gameState.score);
			previousHighScore = highScore;
			highScore = gameState.score;
		}
	}

	public static void saveGameState() {
	}

	public static void getAchievement(long score) {
		/*
		 * if (score >= 2) { Games.Achievements.unlockImmediate(googleApiClient,
		 * "CgkIma_ahNobEAIQAg"); } if (score >= 10) {
		 * Games.Achievements.unlockImmediate(googleApiClient,
		 * "CgkIma_ahNobEAIQAw"); } if (score >= 30) {
		 * Games.Achievements.unlockImmediate(googleApiClient,
		 * "CgkIma_ahNobEAIQBA"); } if (score >= 120) {
		 * Games.Achievements.unlockImmediate(googleApiClient,
		 * "CgkIma_ahNobEAIQBQ"); } if (score >= 300) {
		 * Games.Achievements.unlockImmediate(googleApiClient,
		 * "CgkIma_ahNobEAIQBg"); }
		 */
	}

	public static void saveScoreToLeaderboard(long score) {
		gameState.score = score;
		saveHighScore();
		score = highScore;
		if (!Common.mainActivity.isSignedIn()) {
			Common.mainActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mainActivity, "Not logged in",
							Toast.LENGTH_SHORT).show();
				}

			});

			return;
		}
		Games.Leaderboards.submitScore(Common.mHelper.getApiClient(),
				leaderboard_ID, score);
		Common.mainActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mainActivity, "Score saved",
						Toast.LENGTH_SHORT).show();
			}

		});
	}

	public static void getIsSignedIn() {
		mainActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Common.isSignedIn = Common.mainActivity.isSignedIn();
			}

		});
	}
}

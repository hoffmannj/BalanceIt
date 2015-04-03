package com.hofi.game.balance.screens;

import org.jbox2d.common.Vec2;

import android.widget.Toast;

import com.hofi.game.balance.CharMap;
import com.hofi.game.balance.Common;
import com.hofi.game.balance.NewButton;
import com.hofi.game.balance.interfaces.GameScreen;
import com.hofi.game.balance.interfaces.NewButtonHandler;

public class MainMenu implements GameScreen {

	protected CharMap charMap;
	private final String str = "Local Score: ";
	private final String strName = "MainMenu";
	private String strHighScore;

	@Override
	public void TouchStart() {

	}

	@Override
	public void TouchEnd() {

	}

	@Override
	public void TouchMove() {

	}

	@Override
	public String getName() {
		return strName;
	}

	@Override
	public void init() {
		charMap = new CharMap();

		float y = 510;

		Common.getIsSignedIn();
		Common.getIsSignedIn();
		if (!Common.isSignedIn) {
			Common.addNewButton(this, new Vec2(240, y), "Connect",
					new NewButtonHandler() {
						@Override
						public void handle(NewButton sender, Object owner) {
							Common.mainActivity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Common.mHelper.beginUserInitiatedSignIn();
								}

							});

							Common.gamePlayManager.forceScreenChange();
							Common.gamePlayManager.setCurrentScreen("mainmenu");
						}
					});
			y -= 100;
		}

		Common.addNewButton(this, new Vec2(240, y), "Play",
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						Common.gamePlayManager.setCurrentScreen("level_1");
					}
				});
		y -= 100;

		Common.addNewButton(this, new Vec2(240, y), "Leaderboard",
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						Common.getIsSignedIn();
						Common.getIsSignedIn();
						if (!Common.isSignedIn) {
							Toast.makeText(Common.mainActivity,
									"Not logged in", Toast.LENGTH_SHORT).show();
							return;
						}
						Common.gamePlayManager.setCurrentScreen("leaderboard");
					}
				});
		y -= 100;

		Common.addNewButton(this, new Vec2(240, y), "Quit",
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						Common.mainActivity.finish();
					}
				});

		strHighScore = str + Common.highScore;
	}

	@Override
	public void initFromGameState() {

	}

	@Override
	public void draw() {
		Common.uiObjects.draw();
		charMap.drawString(strHighScore,
				240 - charMap.measureString(strHighScore) / 2, 50);
	}

	@Override
	public void update() {

	}

	@Override
	public void updateGameState(boolean saveScore) {
		Common.gameState.screenName = "mainmenu";
	}

	@Override
	public void cleanUp() {

	}

}

package com.hofi.game.balance.screens;

import android.os.SystemClock;

import com.hofi.game.balance.Common;
import com.hofi.game.balance.Sprite;
import com.hofi.game.balance.interfaces.GameScreen;

public class SplashScreen implements GameScreen {

	private Sprite image;
	private long startTime = -1;

	private final String strName = "SplashScreen";

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
		image = new Sprite("splashscreen");
		image.setPosition(240, 400);
		image.setScale(480, 800);
	}

	@Override
	public void draw() {
		image.draw();
	}

	@Override
	public void update() {
		if (startTime == -1)
			startTime = SystemClock.uptimeMillis();
		long currTime = SystemClock.uptimeMillis();
		if (currTime - startTime > 3000) {
			Common.gamePlayManager.setCurrentScreen("mainmenu");
		}
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void initFromGameState() {

	}

	@Override
	public void updateGameState(boolean saveScore) {
		Common.gameState.score = 0;
		Common.gameState.ballPosition.set(45, 45);
		Common.gameState.ballRotation = 0;
		Common.gameState.ballLinearVelocity.set(0, 0);
		Common.gameState.ballAngularVelocity = 0;
	}

}

package com.hofi.game.balance.screens;

import com.hofi.game.balance.Common;
import com.hofi.game.balance.interfaces.GameScreen;

public class EmptyScreen implements GameScreen {

	private final String strName = "Empty";

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

	}

	@Override
	public void draw() {

	}

	@Override
	public void update() {

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

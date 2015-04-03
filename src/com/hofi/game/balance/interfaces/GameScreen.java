package com.hofi.game.balance.interfaces;

public interface GameScreen extends InputListener {
	String getName();

	void init();

	void initFromGameState();

	void draw();

	void update();

	void updateGameState(boolean saveScore);

	void cleanUp();
}

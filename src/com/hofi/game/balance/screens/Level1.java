package com.hofi.game.balance.screens;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import com.hofi.game.balance.CharMap;
import com.hofi.game.balance.Common;
import com.hofi.game.balance.DrawablePhysicsObject;
import com.hofi.game.balance.Sprite;
import com.hofi.game.balance.gameobjects.Borders;
import com.hofi.game.balance.gameobjects.Finger;
import com.hofi.game.balance.gameobjects.Nail;
import com.hofi.game.balance.interfaces.ContactHandler;
import com.hofi.game.balance.interfaces.GameScreen;

public class Level1 implements GameScreen {

	private boolean ballIsMoving = false;
	private float xdist = 0;
	private Vec2 newHolderPos = new Vec2();
	private boolean wallHit = false;
	private Sprite windLeft;
	private Sprite windRight;
	private int windSide = 0;
	private long windFrames = 0;
	private Random random;

	protected CharMap charMap;

	@Override
	public void TouchStart() {
		Vec2 tv = Common.input.getStartPosition();
		float dist = tv.sub(
				Common.getGameObject("holder").getDPO().getPhysicsObject()
						.getWorldPosition()).length();
		if (dist <= 50)
			ballIsMoving = true;
		xdist = tv.x
				- Common.getGameObject("holder").getDPO().getPhysicsObject()
						.getWorldPosition().x;
		float y = Common.getGameObject("holder").getDPO().getPhysicsObject()
				.getWorldPosition().y;
		float nx = tv.x - xdist;
		if (nx < 20)
			nx = 20;
		if (nx > 460)
			nx = 460;
		System.err.println("new pos: " + nx + ", " + y);
		newHolderPos.set(nx, 85);
	}

	@Override
	public void TouchEnd() {
		ballIsMoving = false;
	}

	@Override
	public void TouchMove() {
		if (!ballIsMoving)
			return;
		Vec2 tv = Common.input.getPosition();
		float y = Common.getGameObject("holder").getDPO().getPhysicsObject()
				.getWorldPosition().y;
		float nx = tv.x - xdist;
		if (nx < 20)
			nx = 20;
		if (nx > 460)
			nx = 460;
		System.err.println("new pos: " + nx + ", " + y);
		newHolderPos.set(nx, 85);
	}

	@Override
	public String getName() {
		return "level_1";
	}

	@Override
	public void init() {
		charMap = new CharMap();

		random = new Random();

		windLeft = new Sprite("wind");
		windLeft.setPosition(65, 735);

		windRight = new Sprite("wind");
		windRight.mirrorOnY();
		windRight.setPosition(413, 735);

		Common.addGameObject("borders", new Borders().init());
		Common.addGameObject("holder", new Finger().init());
		Common.addGameObject("needle", new Nail().init());

		setContactHandler();
		Common.stopWatch.reset();
		Common.stopWatch.start();
	}

	@Override
	public void initFromGameState() {

	}

	@Override
	public void draw() {
		Common.gameObjects.draw();
		Common.uiObjects.draw();
		if (windFrames > 0) {
			if (windSide == 0) {
				windLeft.draw();
			} else {
				windRight.draw();
			}
		}
		charMap.drawString(
				"Elapsed: " + (Common.stopWatch.getElapsedTimeMilli() / 1000)
						+ " s", 25, 750, 0, 0, 1, 1);
	}

	@Override
	public void update() {
		if (windFrames > 0) {
			windFrames--;
			if (windSide == 0) {
				Common.getGameObject("needle").getDPO().getPhysicsObject()
						.getBody().applyForceToCenter(new Vec2(300.0f, 0));
			} else {
				Common.getGameObject("needle").getDPO().getPhysicsObject()
						.getBody().applyForceToCenter(new Vec2(-300.0f, 0));
			}
		}
		if (ballIsMoving) {
			Common.getGameObject("holder").getDPO().getPhysicsObject()
					.setWorldPosition(newHolderPos.x, newHolderPos.y);
		}
		Common.getGameObject("needle").getDPO().getPhysicsObject().getBody()
				.setAwake(true);
		Common.getGameObject("holder").getDPO().getPhysicsObject().getBody()
				.setAwake(true);
		if (wallHit) {
			Common.gameState.score = Common.stopWatch.getElapsedTimeMilli() / 1000;
			Common.saveScoreToLeaderboard(Common.gameState.score);
			Common.stopWatch.reset();
			Common.gamePlayManager.setCurrentScreen("mainmenu");
		}
		if (windFrames <= 0 && random.nextInt(240) == 13) {
			windFrames = 30 + random.nextInt(120);
			windSide = random.nextInt(2);
		}
	}

	@Override
	public void updateGameState(boolean saveScore) {
		if (saveScore) {
			Common.gameState.score = Common.stopWatch.getElapsedTimeMilli() / 1000;
			Common.saveScoreToLeaderboard(Common.gameState.score);
		}
	}

	@Override
	public void cleanUp() {

	}

	protected void setContactHandler() {
		Common.mainContactHandler.addHandler("needle", "borders",
				new ContactHandler() {

					@Override
					public void handleEnter(DrawablePhysicsObject objectA,
							Fixture fixtureA, DrawablePhysicsObject objectB,
							Fixture fixtureB) {
						wallHit = true;
					}

					@Override
					public void handleLeave(DrawablePhysicsObject objectA,
							Fixture fixtureA, DrawablePhysicsObject objectB,
							Fixture fixtureB) {
					}

				});
	}
}

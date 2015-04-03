package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

public class GameState {
	public long score;
	public String screenName;
	public Vec2 ballPosition;
	public float ballRotation;
	public Vec2 ballLinearVelocity;
	public float ballAngularVelocity;

	public GameState() {
		score = 0;
		screenName = "mainmenu";
		ballPosition = new Vec2(45, 45);
		ballRotation = 0;
		ballLinearVelocity = new Vec2();
		ballAngularVelocity = 0;
	}
}

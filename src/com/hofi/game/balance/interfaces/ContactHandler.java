package com.hofi.game.balance.interfaces;

import org.jbox2d.dynamics.Fixture;

import com.hofi.game.balance.DrawablePhysicsObject;

public interface ContactHandler {
	void handleEnter(DrawablePhysicsObject objectA, Fixture fixtureA,
			DrawablePhysicsObject objectB, Fixture fixtureB);

	void handleLeave(DrawablePhysicsObject objectA, Fixture fixtureA,
			DrawablePhysicsObject objectB, Fixture fixtureB);
}

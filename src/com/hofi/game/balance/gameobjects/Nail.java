package com.hofi.game.balance.gameobjects;

import org.jbox2d.common.Vec2;

import com.hofi.game.balance.DrawablePhysicsObject;
import com.hofi.game.balance.interfaces.GameObject;

public class Nail implements GameObject {
	private DrawablePhysicsObject obj;

	@Override
	public GameObject init() {
		obj = new DrawablePhysicsObject("nail", "nail", false);
		obj.getPhysicsObject().setWorldPosition(220, 440);
		obj.getPhysicsObject().setLinearDamping(1f);
		obj.getPhysicsObject().getBody().applyForceToCenter(new Vec2(5.0f, 0));
		return this;
	}

	@Override
	public GameObject update() {
		return this;
	}

	@Override
	public GameObject draw() {
		obj.draw();
		return this;
	}

	@Override
	public GameObject cleanUp() {
		return this;
	}

	@Override
	public DrawablePhysicsObject getDPO() {
		return obj;
	}
}

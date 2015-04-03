package com.hofi.game.balance.gameobjects;

import org.jbox2d.dynamics.BodyType;

import com.hofi.game.balance.DrawablePhysicsObject;
import com.hofi.game.balance.interfaces.GameObject;

public class Finger implements GameObject {
	private DrawablePhysicsObject obj;

	@Override
	public GameObject init() {
		obj = new DrawablePhysicsObject("finger", "finger", false);
		obj.getPhysicsObject().setWorldPosition(240, 85);
		obj.getPhysicsObject().setLinearDamping(1f);
		obj.getPhysicsObject().getBody().setType(BodyType.STATIC);
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

package com.hofi.game.balance.gameobjects;

import com.hofi.game.balance.DrawablePhysicsObject;
import com.hofi.game.balance.interfaces.GameObject;

public class Borders implements GameObject {

	private DrawablePhysicsObject obj;

	@Override
	public GameObject init() {
		obj = new DrawablePhysicsObject("borders", "borders", false);
		obj.getPhysicsObject().setWorldPosition(240, 400);
		obj.getPhysicsObject().setLinearDamping(1f);
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

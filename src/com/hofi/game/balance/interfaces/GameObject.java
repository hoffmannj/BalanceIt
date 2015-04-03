package com.hofi.game.balance.interfaces;

import com.hofi.game.balance.DrawablePhysicsObject;

public interface GameObject {
	GameObject init();

	GameObject update();

	GameObject draw();

	GameObject cleanUp();

	DrawablePhysicsObject getDPO();
}

package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

import com.hofi.game.balance.interfaces.GameScreen;
import com.hofi.game.balance.interfaces.InputListener;

public class InputManager implements InputListener {

	private UIObject target = null;

	@Override
	public void TouchStart() {
		Vec2 p = Common.input.getPosition();
		UIObject obj = Common.uiObjects.hitObject(p);
		if (obj != null) {
			target = obj;
			if (target instanceof InputListener) {
				((InputListener) target).TouchStart();
			}
		} else {
			target = null;
			GameScreen gs = Common.gamePlayManager.getCurrent();
			if (gs != null)
				gs.TouchStart();
		}
	}

	@Override
	public void TouchEnd() {
		if (target != null) {
			if (target instanceof InputListener) {
				((InputListener) target).TouchEnd();
			}
		} else {
			GameScreen gs = Common.gamePlayManager.getCurrent();
			if (gs != null)
				gs.TouchEnd();
		}
		target = null;
	}

	@Override
	public void TouchMove() {
		if (target != null) {
			if (target instanceof InputListener) {
				((InputListener) target).TouchMove();
			}
		} else {
			GameScreen gs = Common.gamePlayManager.getCurrent();
			if (gs != null)
				gs.TouchMove();
		}
	}

}

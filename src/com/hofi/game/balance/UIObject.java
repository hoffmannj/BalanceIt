package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

public abstract class UIObject {
	protected float width;
	protected float height;
	protected Vec2 position = new Vec2();
	protected boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	abstract void draw();

	protected boolean isHit(float x, float y) {
		return x >= position.x - width / 2 && x <= position.x + width / 2
				&& y >= position.y - height / 2 && y <= position.y + height / 2;
	}

	abstract void cleanUp();
}

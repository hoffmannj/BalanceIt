package com.hofi.game.balance;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class UIObjects {
	private ArrayList<UIObject> objects;

	public UIObjects() {
		objects = new ArrayList<UIObject>();
	}

	public UIObject addObject(UIObject obj) {
		if (!objects.contains(obj))
			objects.add(obj);
		return obj;
	}

	public void removeObject(UIObject obj) {
		if (objects.contains(obj))
			objects.remove(obj);
	}

	public void clear() {
		for (UIObject obj : objects) {
			obj.cleanUp();
		}
		objects.clear();
	}

	public UIObject hitObject(float x, float y) {
		for (UIObject obj : objects) {
			if (!obj.isEnabled())
				continue;
			if (obj.isHit(x, y))
				return obj;
		}
		return null;
	}

	public UIObject hitObject(Vec2 v) {
		return hitObject(v.x, v.y);
	}

	public void draw() {
		for (UIObject obj : objects) {
			if (!obj.isEnabled())
				continue;
			obj.draw();
		}
	}
}

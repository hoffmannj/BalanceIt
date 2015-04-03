package com.hofi.game.balance;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.jbox2d.dynamics.Body;

import com.hofi.game.balance.interfaces.GameObject;

public class GameObjects {
	private Hashtable<String, GameObject> objects;

	public GameObjects() {
		objects = new Hashtable<String, GameObject>();
	}

	public GameObject addObject(String objName, GameObject obj) {
		if (!objects.containsKey(objName))
			objects.put(objName, obj);
		return obj;
	}

	public void removeObject(String objName) {
		objects.remove(objName);
	}

	public GameObject getObject(String objName) {
		return objects.get(objName);
	}

	public String getObjectNameForDPO(DrawablePhysicsObject dpo) {
		for (Entry<String, GameObject> entry : objects.entrySet()) {
			if (entry.getValue().getDPO() == dpo)
				return entry.getKey();
		}
		return null;
	}

	public void clear() {
		for (GameObject obj : objects.values()) {
			obj.cleanUp();
		}
		Body b = Common.world.getBodyList();
		while (b != null) {
			Common.world.destroyBody(b);
			b = Common.world.getBodyList();
		}
		objects.clear();
	}

	public void draw() {
		for (GameObject obj : objects.values()) {
			obj.draw();
		}
	}
}

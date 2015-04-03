package com.hofi.game.balance;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import com.hofi.game.balance.interfaces.InputListener;

public class Input {
	private boolean inputActive;
	private Vec2 position;
	private Vec2 startPosition;
	private Vec2 endPosition;
	private Object lockObj = new Object();
	private VectorData touchVector;

	private ArrayList<InputListener> listeners;

	public Input() {
		this.position = new Vec2(0, 0);
		this.startPosition = new Vec2(0, 0);
		this.endPosition = new Vec2(0, 0);
		this.touchVector = new VectorData();
		this.inputActive = false;
		listeners = new ArrayList<InputListener>();
	}

	public boolean isInputActive() {
		synchronized (lockObj) {
			return inputActive;
		}
	}

	public Vec2 getPosition() {
		synchronized (lockObj) {
			return position;
		}
	}

	public Vec2 getStartPosition() {
		synchronized (lockObj) {
			return startPosition;
		}
	}

	public Vec2 getEndPosition() {
		synchronized (lockObj) {
			return endPosition;
		}
	}

	public void setTouchStart(Vec2 position) {
		synchronized (lockObj) {
			this.inputActive = true;
			this.position.set(position);
			this.startPosition.set(position);
		}
		for (InputListener listener : listeners) {
			listener.TouchStart();
		}
	}

	public void setTouchEnd(Vec2 position) {
		synchronized (lockObj) {
			this.inputActive = false;
			this.position.set(position);
			this.endPosition.set(position);
		}
		for (InputListener listener : listeners) {
			listener.TouchEnd();
		}
	}

	public void setTouchMove(Vec2 position) {
		synchronized (lockObj) {
			this.inputActive = true;
			this.position.set(position);
		}
		for (InputListener listener : listeners) {
			listener.TouchMove();
		}
	}

	public void AddListener(InputListener listener) {
		this.listeners.add(listener);
	}

	public void RemoveListener(InputListener listener) {
		this.listeners.remove(listener);
	}

	public void ClearListeners() {
		this.listeners.clear();
	}

	public VectorData getTouchVectorData() {
		Vec2 sp = getStartPosition();
		Vec2 ep = getPosition();
		touchVector.vector.set(ep.x - sp.x, ep.y - sp.y);
		float length = touchVector.vector.length();
		if (length > 400) {
			length = 400;
			touchVector.vector.normalize();
			touchVector.vector.mulLocal(length);
		}
		float dx = touchVector.vector.x;
		float dy = touchVector.vector.y;
		float l = (float) Math.sqrt(dx * dx + dy * dy);
		float a = 0;
		if (dy != 0)
			a = -(float) Math.atan(dx / dy) / (float) (Math.PI / 180f);
		else if (l != 0)
			a = -(float) Math.asin(dx / l) / (float) (Math.PI / 180f);
		if (dy >= 0)
			a = a - 180f;
		touchVector.length = l;
		touchVector.angle = a + 180f;
		return touchVector;
	}
}

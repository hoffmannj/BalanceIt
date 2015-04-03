package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

public class VectorData {
	public float angle;
	public float length;
	public Vec2 vector;

	public VectorData() {
		angle = 0;
		length = 0;
		vector = new Vec2();
	}

	public VectorData(float a, float l, Vec2 v) {
		angle = a;
		length = l;
		vector = v;
	}
}

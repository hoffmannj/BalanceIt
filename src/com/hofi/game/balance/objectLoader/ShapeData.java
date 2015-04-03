package com.hofi.game.balance.objectLoader;

import java.util.List;

import org.jbox2d.common.Vec2;

public class ShapeData {
	public int shapeType;
	public boolean isLoop;
	public Vec2 center;
	public float r;
	public List<Vec2> points;
	public List<int[]> triangles;
}

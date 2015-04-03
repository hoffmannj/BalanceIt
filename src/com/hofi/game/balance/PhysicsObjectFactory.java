package com.hofi.game.balance;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import com.hofi.game.balance.objectLoader.BodyDefData;
import com.hofi.game.balance.objectLoader.FixtureData;
import com.hofi.game.balance.objectLoader.ShapeData;

public class PhysicsObjectFactory {

	private final static String strShape = "_shape";
	private final static String strFixture = "_fixture";
	private final static String strBodydef = "_bodydef";

	public PhysicsObject CreateBody(String bodyName) {
		ShapeData dShape = Common.assetManager.readJSonData(ShapeData.class,
				bodyName + strShape);
		FixtureData dFixture = Common.assetManager.readJSonData(
				FixtureData.class, bodyName + strFixture);
		BodyDefData dBodyDef = Common.assetManager.readJSonData(
				BodyDefData.class, bodyName + strBodydef);

		BodyDef bd = getBodyDef(dBodyDef);

		Body body = Common.world.createBody(bd);

		if (dShape.shapeType == 1) {
			createCircleFixture(body, dShape, dFixture, false);
		} else {
			if (dShape.triangles.size() == 0) {
				createChainFixture(body, dShape, dFixture, false);
			} else {
				createMultiFixture(body, dShape, dFixture, false);
			}
		}
		PhysicsObject po = new PhysicsObject(body);
		body.setUserData(po);
		return po;
	}

	public PhysicsObject AddFixtureToPhysicsObject(PhysicsObject object,
			String bodyName) {
		ShapeData dShape = Common.assetManager.readJSonData(ShapeData.class,
				bodyName + strShape);
		FixtureData dFixture = Common.assetManager.readJSonData(
				FixtureData.class, bodyName + strFixture);

		Body body = object.getBody();

		if (dShape.shapeType == 1) {
			createCircleFixture(body, dShape, dFixture, false);
		} else {
			if (dShape.triangles.size() == 0) {
				createChainFixture(body, dShape, dFixture, false);
			} else {
				createMultiFixture(body, dShape, dFixture, false);
			}
		}
		return object;
	}

	public PhysicsObject CreateSensorBody(String bodyName) {
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		bd.type = BodyType.STATIC;
		bd.allowSleep = true;
		Body body = Common.world.createBody(bd);
		PhysicsObject po = new PhysicsObject(body);
		body.setUserData(po);
		addSensorToBody(body, bodyName);
		return po;
	}

	public void addSensorToBody(Body body, String sensorName) {
		ShapeData dShape = Common.assetManager.readJSonData(ShapeData.class,
				sensorName + strShape);
		if (dShape.shapeType == 1) {
			createCircleFixture(body, dShape, null, true);
		} else {
			if (dShape.triangles.size() == 0) {
				createChainFixture(body, dShape, null, true);
			} else {
				createMultiFixture(body, dShape, null, true);
			}
		}
	}

	private BodyDef getBodyDef(BodyDefData bdData) {
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		if (bdData.dynamic)
			bd.type = BodyType.DYNAMIC;
		else
			bd.type = BodyType.STATIC;
		bd.allowSleep = true;
		return bd;
	}

	private Shape getChainShape(ShapeData dShape) {
		int l = dShape.points.size();
		Vec2[] vecs = new Vec2[l];
		for (int i = 0; i < l; i++) {
			Vec2 pd = dShape.points.get(i);
			vecs[i] = new Vec2(pd.x / Common.Mult, -pd.y / Common.Mult);
		}
		ChainShape cs = new ChainShape();
		if (dShape.isLoop)
			cs.createLoop(vecs, l);
		else
			cs.createChain(vecs, l);
		return cs;
	}

	private Shape getCircleShape(ShapeData dShape) {
		CircleShape cs = new CircleShape();
		Vec2 pd = dShape.center;
		cs.m_p.set(new Vec2(pd.x / Common.Mult, -pd.y / Common.Mult));
		cs.setRadius(dShape.r / Common.Mult);
		return cs;
	}

	private void createChainFixture(Body body, ShapeData dShape,
			FixtureData dFixture, boolean isSensor) {
		body.createFixture(
				createFixture(getChainShape(dShape), dFixture, isSensor))
				.setUserData(body);
	}

	private void createCircleFixture(Body body, ShapeData dShape,
			FixtureData dFixture, boolean isSensor) {
		body.createFixture(
				createFixture(getCircleShape(dShape), dFixture, isSensor))
				.setUserData(body);
	}

	private FixtureDef createFixture(Shape shape, FixtureData dFixture,
			boolean isSensor) {
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		if (dFixture != null) {
			fd.density = dFixture.density;
			fd.friction = dFixture.friction;
			fd.restitution = dFixture.restitution;
		}
		fd.isSensor = isSensor;
		return fd;
	}

	private void createMultiFixture(Body body, ShapeData dShape,
			FixtureData dFixture, boolean isSensor) {
		int l = dShape.points.size();
		Vec2[] vecs = new Vec2[l];
		for (int i = 0; i < l; i++) {
			Vec2 pd = dShape.points.get(i);
			vecs[i] = new Vec2(pd.x / Common.Mult, -pd.y / Common.Mult);
		}
		int len = dShape.triangles.size();

		PolygonShape cs = new PolygonShape();
		FixtureDef fd = createFixture(cs, dFixture, isSensor);
		Vec2[] vecs2 = new Vec2[3];
		vecs2[0] = new Vec2();
		vecs2[1] = new Vec2();
		vecs2[2] = new Vec2();

		for (int i = 0; i < len; i++) {
			int[] tri = dShape.triangles.get(i);
			vecs2[0].set(vecs[tri[0]]);
			vecs2[1].set(vecs[tri[1]]);
			vecs2[2].set(vecs[tri[2]]);

			cs.set(vecs2, 3);
			fd.shape = cs;

			body.createFixture(fd).setUserData(body);
		}
	}

}

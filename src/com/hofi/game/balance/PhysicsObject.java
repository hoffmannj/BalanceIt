package com.hofi.game.balance;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public class PhysicsObject {
	private Body body;
	private Vec2 temp = new Vec2();
	private Object userData;

	public PhysicsObject() {
	}

	public PhysicsObject(Body body) {
		this.body = body;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object data) {
		userData = data;
	}

	public Body getBody() {
		return body;
	}

	public Vec2 getPosition() {
		return body.getPosition();
	}

	public Vec2 getWorldPosition() {
		Vec2 p = body.getPosition();
		temp.set(p.x * Common.Mult, p.y * Common.Mult);
		return temp;
	}

	public float getAngle() {
		return body.getAngle();
	}

	public void setPosition(float x, float y) {
		temp.set(x, y);
		body.setTransform(temp, getAngle());
	}

	public void setPosition(Vec2 position) {
		body.setTransform(position, getAngle());
	}

	public void setWorldPosition(float x, float y) {
		temp.set(x / Common.Mult, y / Common.Mult);
		body.setTransform(temp, getAngle());
	}

	public void setWorldPosition(Vec2 position) {
		setWorldPosition(position.x, position.y);
	}

	public void setAngle(float angle) {
		body.setTransform(getPosition(), angle);
	}

	public boolean isAwake() {
		return body.isAwake();
	}

	public Object getBodyUserData() {
		return body.getUserData();
	}

	public void setBodyUserData(Object data) {
		body.setUserData(data);
	}

	public Vec2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	public void setLinearVelocity(Vec2 lv) {
		body.setLinearVelocity(lv);
	}

	public void setLinearVelocity(float x, float y) {
		temp.set(x, y);
		body.setLinearVelocity(temp);
	}

	public Vec2 getWorldLinearVelocity() {
		Vec2 p = body.getLinearVelocity();
		temp.set(p.x * Common.Mult, p.y * Common.Mult);
		return temp;
	}

	public void setWorldLinearVelocity(Vec2 lv) {
		temp.set(lv.x / Common.Mult, lv.y / Common.Mult);
		body.setLinearVelocity(temp);
	}

	public float getLinearDamping() {
		return body.getLinearDamping();
	}

	public void setLinearDamping(float value) {
		body.setLinearDamping(value);
	}

	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	public void setAngularVelocity(float value) {
		body.setAngularVelocity(value);
	}

	public boolean hasFixture(Fixture fixture) {
		if (fixture.getBody() == body)
			return true;
		return false;
	}

	public void loadSensor(String sensorName) {
		Common.physicsObjectFactory.addSensorToBody(body, sensorName);
	}

}

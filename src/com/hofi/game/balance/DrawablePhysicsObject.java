package com.hofi.game.balance;

public class DrawablePhysicsObject {
	private PhysicsObject physicsObject;
	private Sprite sprite;
	private boolean visible;
	private String bodyName;

	private final String strSensor = "_sensor";

	public DrawablePhysicsObject(String bodyName, String textureName,
			boolean justSensor) {
		this.bodyName = bodyName;
		if (justSensor) {
			physicsObject = Common.physicsObjectFactory
					.CreateSensorBody(bodyName);
		} else {
			physicsObject = Common.physicsObjectFactory.CreateBody(bodyName);
		}
		physicsObject.setUserData(this);
		sprite = new Sprite(textureName);
		this.visible = true;
	}

	public void addFixture(String bodyName) {
		Common.physicsObjectFactory.AddFixtureToPhysicsObject(physicsObject,
				bodyName);
	}

	public void loadSensor() {
		physicsObject.loadSensor(bodyName + strSensor);
	}

	public void loadSensor(String sensorName) {
		physicsObject.loadSensor(sensorName);
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void draw() {
		sprite.setPosition(physicsObject.getWorldPosition());
		sprite.setRotation(physicsObject.getAngle() / Common.RadDeg);
		sprite.setVisible(visible);
		sprite.draw();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getBodyName() {
		return bodyName;
	}
}

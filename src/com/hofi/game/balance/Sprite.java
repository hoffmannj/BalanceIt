package com.hofi.game.balance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.opengl.Matrix;

public class Sprite {
	private float[] modelViewMat = new float[16];
	private Vec2 position = new Vec2(0, 0);
	private float z = 0;
	private Vec2 center = new Vec2(0, 0);
	private float rotation = 0;
	private Vec2 scale = new Vec2(1, 1);
	private float[] color = { 1f, 1f, 1f, 1f };
	private boolean visible = true;
	private String texName;
	private float texCoords[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			0.0f };
	private FloatBuffer textureBuffer;

	public Sprite(String texName) {
		this.texName = texName;

		ByteBuffer tb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tb.order(ByteOrder.nativeOrder());
		textureBuffer = tb.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);

		LoadTextures();
		Vec2 sImage = Common.assetManager.getImageSize(texName + ".png");
		scale.set(sImage.x, sImage.y);
	}

	public Sprite(Bitmap bitmap, String texName) {
		this.texName = texName;

		ByteBuffer tb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tb.order(ByteOrder.nativeOrder());
		textureBuffer = tb.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);

		Common.textures.loadTexture(bitmap, texName);
		scale.set(bitmap.getWidth(), bitmap.getHeight());
	}

	public void draw() {
		if (!visible)
			return;

		GLHelper.activateTexture(texName);
		GLHelper.setTextureCoords(textureBuffer);
		GLHelper.enableAttribArrays();
		Matrix.setIdentityM(modelViewMat, 0);
		Matrix.translateM(modelViewMat, 0, position.x, position.y, z);
		Matrix.rotateM(modelViewMat, 0, rotation, 0, 0, 1);
		Matrix.scaleM(modelViewMat, 0, scale.x, scale.y, 1);
		Matrix.translateM(modelViewMat, 0, center.x, center.y, 0);
		GLHelper.setModelView(modelViewMat);
		GLHelper.setBaseColor(color);
		GLHelper.drawQuad();
		GLHelper.disableAttribArrays();
		GLHelper.setDefaultTextureCoords();
	}

	public void cleanUp() {
		textureBuffer.clear();
		Common.textures.removeTexture(texName);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public void setPosition(Vec2 p) {
		position.set(p);
	}

	public Vec2 getPosition() {
		return position;
	}

	public void setCenter(float x, float y) {
		center.set(x, y);
	}

	public void setCenter(Vec2 p) {
		center.set(p);
	}

	public Vec2 getCenter() {
		return center;
	}

	public void setRotation(float angle) {
		rotation = angle;
	}

	public float getRotation() {
		return rotation;
	}

	public void setScale(float x, float y) {
		scale.set(x, y);
	}

	public void setScale(Vec2 p) {
		scale.set(p);
	}

	public Vec2 getScale() {
		return scale;
	}

	public void setTexCoords(Vec2 topLeft, Vec2 bottomRight) {
		texCoords[0] = topLeft.x;
		texCoords[1] = topLeft.y;
		texCoords[2] = topLeft.x;
		texCoords[3] = bottomRight.y;
		texCoords[4] = bottomRight.x;
		texCoords[5] = bottomRight.y;
		texCoords[6] = bottomRight.x;
		texCoords[7] = topLeft.y;
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
	}

	public void mirrorOnY() {
		texCoords[0] = 1;
		texCoords[1] = 0;
		texCoords[2] = 1;
		texCoords[3] = 1;
		texCoords[4] = 0;
		texCoords[5] = 1;
		texCoords[6] = 0;
		texCoords[7] = 0;
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
	}

	public float[] getColor() {
		return color;
	}

	public void setColor(float r, float g, float b, float a) {
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}

	public void setColor(float[] newColor) {
		color[0] = newColor[0];
		color[1] = newColor[1];
		color[2] = newColor[2];
		color[3] = newColor[3];
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean value) {
		visible = value;
	}

	private void LoadTextures() {
		Common.textures.loadTexture(texName);
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}

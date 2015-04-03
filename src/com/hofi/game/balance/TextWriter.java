package com.hofi.game.balance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class TextWriter {

	public enum TextAlign {
		Left, Center, Right
	}

	private float[] modelViewMat = new float[16];
	private float[] color = { 1f, 1f, 1f, 1f };
	private Paint textPaint;
	private Bitmap bitmap;
	private Canvas canvas;
	private Vec2 scale = new Vec2(1, 1);

	private float texCoords[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			0.0f };
	private FloatBuffer textureBuffer;

	public TextWriter(int fontSize) {
		ByteBuffer tb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tb.order(ByteOrder.nativeOrder());
		textureBuffer = tb.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);

		textPaint = new Paint();
		textPaint.setTextSize(fontSize);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0xff, 0xff, 0xff);

		bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		bitmap.eraseColor(0x2f2f2f2f);
		scale.set(800, 800);
	}

	public void setColor(float r, float g, float b, float a) {
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}

	public void writeText(String s, int x, int y, TextAlign align) {
		bitmap.eraseColor(0x00000000);
		float w = textPaint.measureText(s);
		canvas.drawText(s, 400 - w / 2, 400, textPaint);

		int textureHandle = GLHelper.getNewTextureHandle();

		if (textureHandle != 0) {

			if (align == TextAlign.Left)
				x += w / 2;
			else if (align == TextAlign.Right)
				x -= w / 2;

			GLHelper.bindBitmapToTextureHandle(bitmap, textureHandle);

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
			GLES20.glUniform1i(Common.shaderHandles.hTexture, 0);
			GLHelper.setTextureCoords(textureBuffer);
			GLHelper.enableAttribArrays();
			Matrix.setIdentityM(modelViewMat, 0);
			Matrix.translateM(modelViewMat, 0, x, y, 0);
			Matrix.scaleM(modelViewMat, 0, scale.x, scale.y, 1);
			GLHelper.setModelView(modelViewMat);
			GLHelper.setBaseColor(color);
			GLHelper.drawQuad();
			GLHelper.disableAttribArrays();
			GLHelper.setDefaultTextureCoords();

			GLHelper.deleteTexture(textureHandle);
		}
	}
}

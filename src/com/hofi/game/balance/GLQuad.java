package com.hofi.game.balance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public final class GLQuad {
	private static float squareCoords[] = { -0.5f, 0.5f, 0.0f, // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f, // bottom right
			0.5f, 0.5f, 0.0f }; // top right

	private static float texCoords[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 0.0f };

	private static final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to
																	// draw
																	// vertices

	private static FloatBuffer vertexBuffer;
	private static FloatBuffer textureBuffer;
	private static ShortBuffer drawListBuffer;

	static {
		ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		ByteBuffer tb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tb.order(ByteOrder.nativeOrder());
		textureBuffer = tb.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);

		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);
	}

	public static FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public static FloatBuffer getTextureBuffer() {
		return textureBuffer;
	}

	public static ShortBuffer getDrawListBuffer() {
		return drawListBuffer;
	}
}

package com.hofi.game.balance;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class GLHelper {
	private static int shaderProg;
	private static float[] projection = new float[16];
	private static float[] whiteColor = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
			1f, 1f, 1f, 1f, 1f, 1f, 1f };
	private static float[] tempColor = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
			1f, 1f, 1f, 1f, 1f, 1f, 1f };
	private static FloatBuffer colorBuffer;

	public static final String mVertexShader = "uniform mat4 uProjMatrix;"
			+ "uniform mat4 uMVPMatrix;" + "attribute vec4 aPosition;"
			+ "attribute vec4 aColor;" + "attribute vec2 aTextureCoord;"
			+ "varying vec4 v_Color;" + "varying vec2 v_texCoord;"
			+ "void main() {"
			+ "  gl_Position = uProjMatrix * uMVPMatrix * aPosition;"
			+ "  v_texCoord = aTextureCoord;" + "  v_Color = aColor;" + "}";

	public static final String mFragmentShader = "precision mediump float;"
			+ "varying vec2 v_texCoord;" + "varying vec4 v_Color;"
			+ "uniform sampler2D sTexture;" + "void main() {"
			+ "  gl_FragColor = texture2D( sTexture, v_texCoord ) * v_Color;"
			+ "  gl_FragColor.rgb *= v_Color.a;" + "}";

	private final static String strPosition = "aPosition";
	private final static String strMVPMatrix = "uMVPMatrix";
	private final static String strProjMatrix = "uProjMatrix";
	private final static String strTexCoords = "aTextureCoord";
	private final static String strColor = "aColor";
	private final static String strTexture = "sTexture";

	public static int getShaderProg() {
		return shaderProg;
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			System.err.println(glOperation + ": glError " + error
					+ GLU.gluErrorString(error));
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	private static int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				System.err.println("Could not compile shader " + shaderType
						+ ":");
				System.err.println(GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}

	public static void GetShaderProgramHandles() {
		if (Common.shaderHandles == null)
			return;
		Common.shaderHandles.hPosition = GLES20.glGetAttribLocation(
				getShaderProg(), strPosition);
		Common.shaderHandles.hModelView = GLES20.glGetUniformLocation(
				getShaderProg(), strMVPMatrix);
		Common.shaderHandles.hProjection = GLES20.glGetUniformLocation(
				getShaderProg(), strProjMatrix);
		Common.shaderHandles.hTexCoords = GLES20.glGetAttribLocation(
				getShaderProg(), strTexCoords);
		Common.shaderHandles.hColor = GLES20.glGetAttribLocation(
				getShaderProg(), strColor);
		Common.shaderHandles.hTexture = GLES20.glGetUniformLocation(
				getShaderProg(), strTexture);
	}

	public static void createProgram() {
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShader);
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader);

		int program = GLES20.glCreateProgram();
		if (program != 0) {
			GLES20.glAttachShader(program, vertexShader);
			checkGlError("glAttachShader");
			GLES20.glAttachShader(program, pixelShader);
			checkGlError("glAttachShader");
			GLES20.glLinkProgram(program);
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] != GLES20.GL_TRUE) {
				System.err.println("Could not link program: ");
				System.err.println(GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			} else {
				shaderProg = program;
				GetShaderProgramHandles();
			}
		}
	}

	public static void useProgram() {
		GLES20.glUseProgram(shaderProg);
	}

	public static void initGLView(int width, int height) {
		if (Common.shaderHandles == null)
			return;
		GLES20.glViewport(0, 0, width, height);

		Matrix.orthoM(projection, 0, 0, 480, 0, 800, -10, 10);
		GLES20.glUniformMatrix4fv(Common.shaderHandles.hProjection, 1, false,
				projection, 0);

		GLES20.glVertexAttribPointer(Common.shaderHandles.hPosition, 3,
				GLES20.GL_FLOAT, false, 0, GLQuad.getVertexBuffer());
		GLES20.glVertexAttribPointer(Common.shaderHandles.hTexCoords, 2,
				GLES20.GL_FLOAT, false, 0, GLQuad.getTextureBuffer());

		setBaseColor(whiteColor);

		initGLStates();
	}

	public static void initGLStates() {
		GLES20.glClearColor(0.0f, 0.5f, 0.0f, 0.0f);
		// GLES20.glClearDepthf(10.0f);
		// GLES20.glEnable(GL10.GL_DEPTH_TEST);
		// GLES20.glDepthFunc(GL10.GL_LEQUAL);
		GLES20.glDisable(GL10.GL_DEPTH_TEST);

		GLES20.glEnable(GL10.GL_BLEND);
		// GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void frameStart() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(shaderProg);
		GLES20.glUniformMatrix4fv(Common.shaderHandles.hProjection, 1, false,
				projection, 0);
	}

	private static void initColorBuffer() {
		ByteBuffer cb = ByteBuffer.allocateDirect(whiteColor.length * 4);
		cb.order(ByteOrder.nativeOrder());
		colorBuffer = cb.asFloatBuffer();
		colorBuffer.put(whiteColor);
		colorBuffer.position(0);
	}

	public static void setBaseColor(float[] color) {
		if (colorBuffer == null)
			initColorBuffer();
		for (int i = 0; i < 16; i++) {
			tempColor[i] = color[i % 4];
		}
		colorBuffer.position(0);
		colorBuffer.put(tempColor);
		colorBuffer.position(0);
		GLES20.glVertexAttribPointer(Common.shaderHandles.hColor, 4,
				GLES20.GL_FLOAT, false, 0, colorBuffer);
	}

	public static int getNewTextureHandle() {
		final int[] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);
		return textureHandle[0];
	}

	public static void bindBitmapToTextureHandle(Bitmap bmp, int texHandle) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
	}

	public static void deleteTexture(int texId) {
		int[] textureIds = new int[1];
		textureIds[0] = texId;
		GLES20.glDeleteTextures(1, textureIds, 0);
		GLES20.glFlush();
	}

	public static void activateTexture(String texName) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
				Common.textures.getTexture(texName));
		GLES20.glUniform1i(Common.shaderHandles.hTexture, 0);
	}

	public static void enableAttribArrays() {
		GLES20.glEnableVertexAttribArray(Common.shaderHandles.hPosition);
		GLES20.glEnableVertexAttribArray(Common.shaderHandles.hTexCoords);
		GLES20.glEnableVertexAttribArray(Common.shaderHandles.hColor);
	}

	public static void disableAttribArrays() {
		GLES20.glDisableVertexAttribArray(Common.shaderHandles.hColor);
		GLES20.glDisableVertexAttribArray(Common.shaderHandles.hTexCoords);
		GLES20.glDisableVertexAttribArray(Common.shaderHandles.hPosition);
	}

	public static void setTextureCoords(FloatBuffer textureBuffer) {
		GLES20.glVertexAttribPointer(Common.shaderHandles.hTexCoords, 2,
				GLES20.GL_FLOAT, false, 0, textureBuffer);
	}

	public static void setDefaultTextureCoords() {
		GLES20.glVertexAttribPointer(Common.shaderHandles.hTexCoords, 2,
				GLES20.GL_FLOAT, false, 0, GLQuad.getTextureBuffer());
	}

	public static void setModelView(float[] modelViewMat) {
		GLES20.glUniformMatrix4fv(Common.shaderHandles.hModelView, 1, false,
				modelViewMat, 0);
	}

	public static void drawQuad() {
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
				GLQuad.getDrawListBuffer());
	}
}

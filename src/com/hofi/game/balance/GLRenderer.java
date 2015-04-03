package com.hofi.game.balance;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class GLRenderer implements Renderer {

	private long lastStartDraw = -1;
	private long startDraw;
	private long frameCount = 0;
	private long frameCountStart = -1;

	public GLRenderer() {
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (Common.gamePlayManager == null)
			return;
		if (Common.world == null)
			return;
		frameCount++;

		startDraw = SystemClock.uptimeMillis();
		if (frameCountStart < 0)
			frameCountStart = startDraw;
		if (lastStartDraw == -1)
			lastStartDraw = startDraw - 20;

		Common.gamePlayManager.update(false);

		GLHelper.frameStart();

		Common.gamePlayManager.getCurrent().update();
		Common.gamePlayManager.getCurrent().draw();

		if (startDraw - frameCountStart >= 1000) {
			Common.fps = (int) frameCount;
			frameCount = 1;
			frameCountStart = startDraw;
		}

		lastStartDraw = startDraw;
		Common.updatePhysicsWorld();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLHelper.useProgram();
		GLHelper.initGLView(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		/*
		 * if (System.nanoTime() / 1000 - Common.lastConnectTry > 20 * 1000) {
		 * Common.lastConnectTry = System.nanoTime() / 1000; if
		 * (Common.googleApiClient != null &&
		 * !Common.googleApiClient.isConnected())
		 * Common.mHelper.reconnectClient(); Common.googleApiClient =
		 * Common.mHelper.getApiClient(); }
		 */

		GLHelper.createProgram();
		GLHelper.useProgram();
		GLHelper.initGLStates();
		// TODO: proper GameState handling!!!!!!!!!!!!!!
		if (Common.gamePlayManager != null && Common.gameState != null
				&& Common.gameState.screenName != null) {
			Common.gamePlayManager
					.setCurrentScreen(Common.gameState.screenName);
			Common.gamePlayManager.update(true);
		}
	}
}

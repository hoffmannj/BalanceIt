package com.hofi.game.balance;

import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.GameHelper;

public class MainActivity extends Activity implements
		GameHelper.GameHelperListener {
	private MyGLSurfaceView mGLView;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private AdView adView;

	public MainActivity() {
		System.err.println("balance - constructor");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.err.println("balance - onCreate start");
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (Common.mHelper == null) {
			Common.mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);

			Common.mHelper.enableDebugLog(true, "GameHelper");
			// Common.mHelper.setConnectOnStart(false);
			Common.mHelper.setup(this);
		}

		// Common.mainActivity = this;
		if (Common.assetManager == null)
			Common.assetManager = new MyAssetManager();

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		setContentView(R.layout.main);

		if (!supportsEs2) {
			return;
		}

		if (adView == null) {
			adView = (AdView) this.findViewById(R.id.ad);

			try {
				Drawable bmp = null;
				bmp = Drawable.createFromStream(
						getAssets().open("adbackground.png"),
						"adbackground.png");
				adView.setBackgroundDrawable(bmp);
			} catch (IOException e) {
				e.printStackTrace();
			}

			AdRequest adRequest = new AdRequest.Builder().addTestDevice(
					AdRequest.DEVICE_ID_EMULATOR).build();

			adView.setVisibility(AdView.VISIBLE);
			adView.loadAd(adRequest);
		}

		if (Common.glView == null) {
			mGLView = (MyGLSurfaceView) this.findViewById(R.id.glSurface);
			Common.glView = mGLView;
		}

		Common.screenWidth = Common.glView.getWidth();
		Common.screenHeight = Common.glView.getHeight();
		System.err.println("balance - onCreate end");
	}

	@Override
	protected void onStart() {
		System.err.println("balance - onStart start");
		super.onStart();
		Common.mHelper.onStart(this);
		System.err.println("balance - onStart end");
	}

	@Override
	protected void onStop() {
		System.err.println("balance - onStop start");
		super.onStop();
		Common.mHelper.onStop();
		System.err.println("balance - onStop end");
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		System.err.println("balance - onActivityResult start");
		super.onActivityResult(request, response, data);
		Common.mHelper.onActivityResult(request, response, data);
		System.err.println("balance - onActivityResult end");
	}

	@Override
	protected void onPause() {
		System.err.println("balance - onPause start");
		if (Common.glView != null) {
			Common.screenWidth = Common.glView.getWidth();
			Common.screenHeight = Common.glView.getHeight();
		}
		super.onPause();
		if (mGLView != null)
			mGLView.onPause();
		System.err.println("balance - onPause end");
	}

	private Activity getActivity() {
		return this;
	}

	@Override
	protected void onResume() {
		System.err.println("balance - onResume start");
		if (Common.glView != null) {
			Common.screenWidth = Common.glView.getWidth();
			Common.screenHeight = Common.glView.getHeight();
		}
		int checkGooglePlayServices = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
					checkGooglePlayServices, getActivity(),
					DIALOG_GET_GOOGLE_PLAY_SERVICES);
			if (dialog != null)
				dialog.show();
		}
		super.onResume();
		if (mGLView != null)
			mGLView.onResume();
		System.err.println("balance - onResume end");
	}

	@Override
	public void onSignInFailed() {
		Common.gamePlayManager.forceScreenChange();
		Common.gamePlayManager.setCurrentScreen("mainmenu");
	}

	@Override
	public void onSignInSucceeded() {
		// Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
		Common.gamePlayManager.forceScreenChange();
		Common.gamePlayManager.setCurrentScreen("mainmenu");
	}
}

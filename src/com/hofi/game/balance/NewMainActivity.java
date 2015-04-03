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
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.example.games.basegameutils.GameHelper;

public class NewMainActivity extends Activity implements
		GameHelper.GameHelperListener {

	// The game helper object. This class is mainly a wrapper around this
	// object.
	protected GameHelper mHelper;

	// We expose these constants here because we don't want users of this class
	// to have to know about GameHelper at all.
	public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
	public static final int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
	public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
	public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

	// Requested clients. By default, that's just the games client.
	protected int mRequestedClients = CLIENT_GAMES;

	private final static String TAG = "BaseGameActivity";
	protected boolean mDebugLog = false;

	private MyGLSurfaceView mGLView;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private AdView adView;

	/** Constructs a BaseGameActivity with default client (GamesClient). */
	public NewMainActivity() {
		super();
	}

	/**
	 * Constructs a BaseGameActivity with the requested clients.
	 * 
	 * @param requestedClients
	 *            The requested clients (a combination of CLIENT_GAMES,
	 *            CLIENT_PLUS and CLIENT_APPSTATE).
	 */
	/*
	 * protected NewMainActivity(int requestedClients) { super();
	 * setRequestedClients(requestedClients); }
	 */

	/**
	 * Sets the requested clients. The preferred way to set the requested
	 * clients is via the constructor, but this method is available if for some
	 * reason your code cannot do this in the constructor. This must be called
	 * before onCreate or getGameHelper() in order to have any effect. If called
	 * after onCreate()/getGameHelper(), this method is a no-op.
	 * 
	 * @param requestedClients
	 *            A combination of the flags CLIENT_GAMES, CLIENT_PLUS and
	 *            CLIENT_APPSTATE, or CLIENT_ALL to request all available
	 *            clients.
	 */
	protected void setRequestedClients(int requestedClients) {
		mRequestedClients = requestedClients;
	}

	public GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(this, mRequestedClients);
			mHelper.enableDebugLog(mDebugLog);
		}
		return mHelper;
	}

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Common.mHelper = getGameHelper();

		Common.mainActivity = this;
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

		mHelper.setup(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		mHelper.onActivityResult(request, response, data);
	}

	public GoogleApiClient getApiClient() {
		return mHelper.getApiClient();
	}

	public boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	public void beginUserInitiatedSignIn() {
		mHelper.beginUserInitiatedSignIn();
	}

	public void signOut() {
		mHelper.signOut();
	}

	protected void showAlert(String message) {
		mHelper.makeSimpleDialog(message).show();
	}

	protected void showAlert(String title, String message) {
		mHelper.makeSimpleDialog(title, message).show();
	}

	protected void enableDebugLog(boolean enabled) {
		mDebugLog = true;
		if (mHelper != null) {
			mHelper.enableDebugLog(enabled);
		}
	}

	@Deprecated
	protected void enableDebugLog(boolean enabled, String tag) {
		Log.w(TAG, "BaseGameActivity.enabledDebugLog(bool,String) is "
				+ "deprecated. Use enableDebugLog(boolean)");
		enableDebugLog(enabled);
	}

	protected String getInvitationId() {
		return mHelper.getInvitationId();
	}

	protected void reconnectClient() {
		mHelper.reconnectClient();
	}

	protected boolean hasSignInError() {
		return mHelper.hasSignInError();
	}

	protected GameHelper.SignInFailureReason getSignInError() {
		return mHelper.getSignInError();
	}

	@Override
	public void onSignInFailed() {
		Common.isSignedIn = false;
		Common.gamePlayManager.forceScreenChange();
		Common.gamePlayManager.setCurrentScreen("mainmenu");
	}

	@Override
	public void onSignInSucceeded() {
		Common.isSignedIn = true;
		Common.gamePlayManager.forceScreenChange();
		Common.gamePlayManager.setCurrentScreen("mainmenu");
	}

	@Override
	protected void onPause() {
		if (Common.glView != null) {
			Common.screenWidth = Common.glView.getWidth();
			Common.screenHeight = Common.glView.getHeight();
		}
		super.onPause();
		if (mGLView != null)
			mGLView.onPause();
	}

	private Activity getActivity() {
		return this;
	}

	@Override
	protected void onResume() {
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
	}

}

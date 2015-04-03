package com.hofi.game.balance;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

	private SharedPreferences sharedPref;

	public void getPreferences() {
		sharedPref = Common.mainActivity.getPreferences(Context.MODE_PRIVATE);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putFloat(String key, float value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String getString(String key) {
		String def = "";
		return sharedPref.getString(key, def);
	}

	public int getInt(String key) {
		int def = 0;
		return sharedPref.getInt(key, def);
	}

	public long getLong(String key) {
		long def = 0;
		return sharedPref.getLong(key, def);
	}

	public float getFloat(String key) {
		float def = 0;
		return sharedPref.getFloat(key, def);
	}

	public boolean getBoolean(String key) {
		boolean def = false;
		return sharedPref.getBoolean(key, def);
	}
}

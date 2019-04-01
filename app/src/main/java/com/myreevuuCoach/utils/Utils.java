package com.myreevuuCoach.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {
	private Context mActivity;
	private SharedPreferences preferences;

	public Utils(Context activity) {
		mActivity = activity;
		preferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
	}

	public void setString(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public String getString(String key, String defaultValue) {
		return preferences.getString(key, defaultValue);
	}

	public void setBoolean(String key, Boolean value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public boolean getBoolean(String key, Boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

	public void setInt(String key, int value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public int getInt(String key, int defaultValue) {
		return preferences.getInt(key, defaultValue);
	}

	public void clear_shf() {
		preferences.edit().clear().apply();
	}


	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return Math.round(dp * scale);
	}
}

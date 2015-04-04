package com.wm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPfUtil {
	private static String KEY = "LingTaiWm";

	/**
	 * get value from sharedpreferences according to the key
	 * 
	 * @param context
	 * @param key
	 * @return String, the value get from sharedpreferences
	 */
	public static String getValue(Context context, String key) {

		SharedPreferences sp = context.getSharedPreferences(KEY,
				Context.MODE_MULTI_PROCESS);
		return sp.getString(key, "");

	}

	/**
	 * set value to sharedpreferences
	 * 
	 * @param context
	 * @param key
	 *            , the name of sharedpreferences
	 * @param value
	 *            , the value to be saved
	 */
	public static <T> void setValue(Context context, String key, T value) {

		SharedPreferences sp = context.getSharedPreferences(KEY,
				Context.MODE_MULTI_PROCESS);
		Editor editor = sp.edit();
		editor.putString(key, String.valueOf(value));
		editor.commit();

	}
}

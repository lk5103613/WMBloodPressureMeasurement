package com.wm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class StateSharePrefs {
	
	public static String SP_NAME = "prefs_states";
	public static String TYPE_AUTH = "auth";
	public static String TYPE_LOGIN = "login";
	public static String TYPE_USER_PHONE="phone_number";
	public static String TYLE_USER_PWD="pwd";
	
	private SharedPreferences mSharePreferences;
	private Context mContext;
	private static StateSharePrefs mState;
	
	private StateSharePrefs(Context context) {
		this.mContext = context;
		this.mSharePreferences = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}
	
	public static StateSharePrefs getInstance(Context context) {
		if(mState == null) {
			mState = new StateSharePrefs(context);
		}
		return mState;
	}
	
	public void saveState(String type, boolean value) {
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putBoolean(type, value);
		editor.commit();
	}
	
	public boolean getState(String type) {
		return mSharePreferences.getBoolean(type, false);
	}
	
	public void saveStr(String type, String value) {
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putString(type, value);
		editor.commit();
	}
	
	public String getStr(String type) {
		return mSharePreferences.getString(type, "");
	}
	

}

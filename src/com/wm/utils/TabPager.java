package com.wm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TabPager {
	
	public static String PREVIOUS_TAB_PAGE = "previous_page";
	public static String SP_NAME = "prefs_page";
	
	private SharedPreferences mSharePreferences;
	private Context mContext;
	public static int PAGE_HOME = 0;
	public static int PAGE_DEVICE = 1;
	public static int PAGE_SETTING = 2;
	private static TabPager mTabPager = null;
	
	private TabPager(Context context) {
		this.mContext = context;
		this.mSharePreferences = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}
	
	public static TabPager getInstance(Context context) {
		if(mTabPager == null) {
			mTabPager = new TabPager(context);
		}
		return mTabPager;
	}
	
	public void savePosition(int page) {
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putInt(PREVIOUS_TAB_PAGE, page);
		editor.commit();
	}
	
	public void clear() {
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putInt(PREVIOUS_TAB_PAGE, -1);
		editor.commit();
	}
	
	public int getCurrentPage() {
		return mSharePreferences.getInt(PREVIOUS_TAB_PAGE, -1);
	}
	
}

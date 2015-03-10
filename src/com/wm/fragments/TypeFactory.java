package com.wm.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.wm.activity.R;
import com.wm.entity.DeviceInfo;

public class TypeFactory {
	
	private static BaseResultFragment mBPResultFragment = new BPResultFragment();
	private static BaseResultFragment mBSResultFragment = new BSResultFragment();
	private static BaseResultFragment mFHResultFragment = new FHResultFragment();
	
	private static Fragment mBPHistoryFragment = new BPHistoryFragment();
	private static Fragment mBSHistoryFragment = new BSHistoryFragment();
	private static Fragment mFHHistoryFragment = new FHHistoryFragment();
	
	public static BaseResultFragment getResultFragment(String type) {
		if(type.equals(DeviceInfo.TYPE_BS)) {
			return mBSResultFragment;
		} else if(type.equals(DeviceInfo.TYPE_BP)) {
			return mBPResultFragment;
		} else if (DeviceInfo.TYPE_FH.equals(type)) {
			return mFHResultFragment;
		}
		return null;
	}
	
	public static Fragment getHistoryFragment(String type) {
		if(type.equals(DeviceInfo.TYPE_BS)) {
			return mBSHistoryFragment;
		} else if(type.equals(DeviceInfo.TYPE_BP)) {
			return mBPHistoryFragment;
		} else if (DeviceInfo.TYPE_FH.equals(type)) {
			return mFHHistoryFragment;
		}
		return null;
	}
	
	public static String getTitle(Context context, String type) {
		String title = "";
		switch (type) {
		case DeviceInfo.TYPE_BP:
			title = context.getResources().getString(R.string.bp_text);
			break;
		case DeviceInfo.TYPE_BS:
			title = context.getResources().getString(R.string.bs_text);
			break;
		case DeviceInfo.TYPE_FH:
			title = context.getResources().getString(R.string.fh_text);
			break;
		}
		return title;
	}

}

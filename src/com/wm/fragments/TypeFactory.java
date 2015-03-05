package com.wm.fragments;

import com.wm.entity.DeviceInfo;

public class TypeFactory {
	
	private static BaseResultFragment mBPResultFragment = new BPResultFragment();
	private static BaseResultFragment mBSResultFragment = new BSResultFragment();
	private static BaseResultFragment mEOResultFragment = new EOResultFragment();
	
	public static BaseResultFragment getResultFragment(String type) {
		if(type.equals(DeviceInfo.TYPE_GLUCOMETER)) {
			return mBSResultFragment;
		} else if(type.equals(DeviceInfo.TYPE_TURGOSCOPE)) {
			return mBPResultFragment;
		} else if(type.equals(DeviceInfo.TYPE_EMBRYO)) {
			return mEOResultFragment;
		}
		return null;
	}

}

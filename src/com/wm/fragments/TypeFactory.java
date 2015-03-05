package com.wm.fragments;

import com.wm.entity.DeviceInfo;

import android.support.v4.app.Fragment;

public class TypeFactory {
	
	private static Fragment mBPResultFragment = new BPResultFragment();
	private static Fragment mBSRFragment = new BSResultFragment();
	
	public static Fragment getResultFragment(String type) {
		if(type.equals(DeviceInfo.TYPE_GLUCOMETER)) {
			return mBSRFragment;
		} else if(type.equals(DeviceInfo.TYPE_TURGOSCOPE)) {
			return mBPResultFragment;
		}
		return null;
	}

}

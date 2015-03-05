package com.wm.fragments;

import com.wm.entity.DeviceInfo;

public class TypeFactory {
	
	private static BaseResultFragment mBPResultFragment = new BPResultFragment();
	private static BaseResultFragment mBSRFragment = new BSResultFragment();
	private static BaseResultFragment mEmbryoFragment = new EmbryoResultFragment();
	
	public static BaseResultFragment getResultFragment(String type) {
		if(type.equals(DeviceInfo.TYPE_BS)) {
			return mBSRFragment;
		} else if(type.equals(DeviceInfo.TYPE_BP)) {
			return mBPResultFragment;
		} else if (DeviceInfo.TYPE_FH.equals(type)) {
			return mEmbryoFragment;
		}
		return null;
	}

}

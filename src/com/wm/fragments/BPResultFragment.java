package com.wm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wm.activity.R;

public class BPResultFragment extends BaseResultFragment  {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bp_result, container, false);
	}

	@Override
	public void record() {
		
	}

}

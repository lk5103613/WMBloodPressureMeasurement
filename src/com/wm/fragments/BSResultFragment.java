package com.wm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wm.wmbloodpressuremeasurement.R;

public class BSResultFragment extends BaseResultFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bs_result, container, false);
	}
	
	public void saveData() {
		
	}

	@Override
	public void record() {
		
	}

}

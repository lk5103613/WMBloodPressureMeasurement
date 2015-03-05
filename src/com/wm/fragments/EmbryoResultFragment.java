package com.wm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.wm.activity.R;

public class EmbryoResultFragment extends BaseResultFragment {
	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;
	@InjectView(R.id.result_container)
	LinearLayout mResultContainer;
	@InjectView(R.id.text_result)
	TextView mResultTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fh_result, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void record() {
		// TODO Auto-generated method stub
		
	}

}

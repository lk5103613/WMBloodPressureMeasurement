package com.wm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.github.mikephil.charting.charts.LineChart;
import com.wm.wmbloodpressuremeasurement.R;

public class EmbryoResultFragment extends Fragment {
	@InjectView(R.id.embryo_result_chart)
	LineChart mChart;
	@InjectView(R.id.result_container)
	LinearLayout mResultContainer;
	@InjectView(R.id.text_result)
	TextView mResultTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_embryo_result, container, false);
		ButterKnife.inject(this, view);
		return view;
	}
	
	public void saveData() {
		
	}

}

package com.wm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.wmbloodpressuremeasurement.R;

public class SettingFragment extends Fragment {
	
	@InjectView(R.id.setting_list)
	RecyclerView mSettingList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		ButterKnife.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSettingList.setLayoutManager(new LinearLayoutManager(getActivity()));
	}
	
	public class SettingListAdapter extends RecyclerView.Adapter<ViewHolder> {

		@Override
		public int getItemCount() {
			return 0;
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int arg1) {
			
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			return null;
		}

	}
	
}

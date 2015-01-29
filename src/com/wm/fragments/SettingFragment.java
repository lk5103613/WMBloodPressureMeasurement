package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.SettingListAdapter;
import com.wm.entity.SettingData;
import com.wm.wmbloodpressuremeasurement.AboutActivity;
import com.wm.wmbloodpressuremeasurement.R;

public class SettingFragment extends Fragment {
	
	@InjectView(R.id.setting_list)
	RecyclerView mSettingList;
	
	private List<SettingData> items = new ArrayList<SettingData>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		ButterKnife.inject(this, view);
		return view;
	}
	
	private void initItems() {
		// 版本信息应从服务器获取
		items.add(new SettingData("版本信息", "Demo版"));
		items.add(new SettingData("关于我们", new Intent(getActivity(), AboutActivity.class), getActivity()));
		items.add(new SettingData("使用帮助", new Intent(), getActivity()));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSettingList.setLayoutManager(new LinearLayoutManager(getActivity()));
		SettingListAdapter adapter = new SettingListAdapter(items);
		mSettingList.setAdapter(adapter);
		if(items.size() == 0) {
			initItems();
		}
	}
	
}

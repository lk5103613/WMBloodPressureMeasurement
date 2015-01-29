package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.wm.adapter.SettingListAdapter.SettingItemClickCallback;
import com.wm.entity.SettingData;
import com.wm.wmbloodpressuremeasurement.AboutActivity;
import com.wm.wmbloodpressuremeasurement.MainActivity;
import com.wm.wmbloodpressuremeasurement.R;

public class SettingFragment extends Fragment implements SettingItemClickCallback {
	
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
		items.add(new SettingData("关于我们", new Intent(getActivity(), AboutActivity.class)));
		items.add(new SettingData("使用帮助", new Intent()));
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSettingList.setLayoutManager(new LinearLayoutManager(getActivity()));
		SettingListAdapter adapter = new SettingListAdapter(this, items);
		mSettingList.setAdapter(adapter);
		if(items.size() == 0) {
			initItems();
		}
	}

	@Override
	public void clickCallback(int position) {
		SettingData data = this.items.get(position);
		if(data.hasMoreContent) {
			getActivity().startActivity(data.targetIntent);
		}
		SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(MainActivity.PREVIOUS_TAB_PAGE, MainActivity.PAGE_SETTING);
		editor.commit();
	}
	
}

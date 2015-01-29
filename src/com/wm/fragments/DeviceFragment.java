package com.wm.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.DeviceListAdapter;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.entity.TypeEnum;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceFragment extends Fragment {
	@InjectView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	@InjectView(R.id.btn_change)
	Button mBtnUpdate;
	DeviceDataSet deviceDataSet;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_device, container, false);
		ButterKnife.inject(this, view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(layoutManager);
		
		final DeviceListAdapter adapter = new DeviceListAdapter(deviceDataSet);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());  
		
		mBtnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mBtnUpdate.getText().equals("删除")){
					deviceDataSet.option= OptionEnum.ITEM_UPDATE;
					adapter.notifyDataSetChanged();
					mBtnUpdate.setText("修改");
				}else {
					deviceDataSet.option= OptionEnum.ITEM_DELETE;
					adapter.notifyDataSetChanged();
					mBtnUpdate.setText("删除");
				}
				
			} 
		});
	}

	private void initData() {
		deviceDataSet = new DeviceDataSet();
		deviceDataSet.option = OptionEnum.ITEM_DELETE;
		ArrayList<DeviceInfo> deviceInfos = new ArrayList<>();

		DeviceInfo deviceInfo = null;
		for (int i = 0; i < 30; i++) {
			deviceInfo = new DeviceInfo(TypeEnum.TURGOSCOPE, "血压计 -" + i);
			deviceInfos.add(deviceInfo);
		}
		deviceDataSet.deviceInfos = deviceInfos;
	}

}

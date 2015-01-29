package com.wm.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.wm.adapter.DeviceListAdapter;
import com.wm.adapter.DeviceListAdapter.DeviceListCallBack;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.entity.TypeEnum;
import com.wm.wmbloodpressuremeasurement.AddDeviceActivity;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceFragment extends Fragment implements DeviceListCallBack{
	@InjectView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	@InjectView(R.id.btn_change)
	Button mBtnUpdate;
	DeviceDataSet deviceDataSet;
	
	EditText mNameEditText;
	
	DeviceListAdapter adapter;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_device, container, false);
		ButterKnife.inject(this, view);

		context = getActivity();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(layoutManager);
		
		adapter = new DeviceListAdapter(deviceDataSet,this);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());  
		
		mBtnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (mBtnUpdate.getText().equals("删除")){
//					deviceDataSet.option= OptionEnum.ITEM_UPDATE;
//					adapter.notifyDataSetChanged();
//					mBtnUpdate.setText("修改");
//				}else {
//					deviceDataSet.option= OptionEnum.ITEM_DELETE;
//					adapter.notifyDataSetChanged();
//					mBtnUpdate.setText("删除");
//				}
				
				Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
				startActivity(intent);
				
			} 
		});
		
		mNameEditText = new EditText(getActivity());
		mNameEditText.setTextColor(getResources().getColor(R.color.dark_gray));
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

	@Override
	public void update(int i) {
		String name = deviceDataSet.deviceInfos.get(i).getName();
		mNameEditText.setText(name);
		new AlertDialog.Builder(context).setTitle("设备名称").setIcon(
			     R.drawable.ic_action_edit).setView(
			     mNameEditText).setPositiveButton("确定",new DialogClickListener(i))
			     .setNegativeButton("取消", null).show();
	}

	@Override
	public void delete(int i) {
		deviceDataSet.deviceInfos.remove(i);
		adapter.notifyDataSetChanged();
		
	}
	
	
	class DialogClickListener implements OnClickListener{
		private int position;
		
		public DialogClickListener(int position){
			this.position = position;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			System.out.println(which);
			deviceDataSet.deviceInfos.get(position)
				.setName(mNameEditText.getText().toString().trim());
			adapter.notifyDataSetChanged();
		}
		
	}
	

}

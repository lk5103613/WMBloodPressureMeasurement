package com.wm.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.wm.wmbloodpressuremeasurement.BloodHistoryActivity;
import com.wm.wmbloodpressuremeasurement.MainActivity;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceFragment extends Fragment implements DeviceListCallBack {
	@InjectView(R.id.recyclerView)
	RecyclerView mRecyclerView;

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
		setHasOptionsMenu(true);//显示fragment的menu
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		
		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(
				getActivity());
		mRecyclerView.setLayoutManager(layoutManager);

		adapter = new DeviceListAdapter(deviceDataSet, this);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater().inflate(R.menu.device, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_add_device:
			savePosition();
			deviceDataSet.option = OptionEnum.ITEM_ADD;
			adapter.notifyDataSetChanged();
			Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_fade_out);
			
			break;
		case R.id.action_delete_device:
			deviceDataSet.option = OptionEnum.ITEM_DELETE;
			adapter.notifyDataSetChanged();
			break;
		case R.id.action_change_name:
			deviceDataSet.option = OptionEnum.ITEM_UPDATE;
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initData() {
		deviceDataSet = new DeviceDataSet();
		deviceDataSet.option = OptionEnum.ITEM_ADD;
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
		mNameEditText = new EditText(getActivity());
		mNameEditText.setTextColor(getResources().getColor(R.color.dark_gray));
		mNameEditText.setText(name);
		new AlertDialog.Builder(context).setTitle("设备名称")
				.setIcon(R.drawable.ic_action_edit).setView(mNameEditText)
				.setPositiveButton("确定", new DialogClickListener(i))
				.setNegativeButton("取消", null).show();
	}

	@Override
	public void delete(int i) {
		deviceDataSet.deviceInfos.remove(i);
		adapter.notifyDataSetChanged();
	}

	class DialogClickListener implements OnClickListener {
		private int position;

		public DialogClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {

			deviceDataSet.deviceInfos.get(position).setName(
					mNameEditText.getText().toString().trim());
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void checkHistory(int i) {
		savePosition();
		Intent intent = new Intent(getActivity(), BloodHistoryActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_fade_out);

	}
	
	private void savePosition(){
		SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(MainActivity.PREVIOUS_TAB_PAGE, MainActivity.PAGE_DEVICE);
		editor.commit();
	}


}

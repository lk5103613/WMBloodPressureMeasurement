package com.wm.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.wm.activity.AddDeviceActivity;
import com.wm.activity.BPHistoryActivity;
import com.wm.activity.BSHistoryActivity;
import com.wm.activity.FHHistoryActivity;
import com.wm.activity.MainActivity;
import com.wm.activity.R;
import com.wm.customview.DeviceIcon;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.utils.BtnCallback;
import com.wm.utils.DialogUtils;

public class DeviceFragment extends Fragment {

	public static int STATE_EDIT = 0;
	public static int STATE_DELETE = 1;
	public static int STATE_NORMAL = 2;

	@InjectView(R.id.device_listview)
	ListView mDeviceListView;

	OnStateChangeListener mCallback;
	DeviceDataSet mDeviceDataSet;
	EditText mNameEditText;
	DeviceListAdapter mAdapter;
	Context mContext;
	private boolean isDelete = false;
	private boolean isEdit = false;

	public interface OnStateChangeListener {
		public void onStateChange(int state);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnStateChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_device, container, false);
		ButterKnife.inject(this, view);
		mContext = getActivity();
		setHasOptionsMenu(true);// 显示fragment的menu

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mAdapter = new DeviceListAdapter(mContext, mDeviceDataSet);
		mDeviceListView.setAdapter(mAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater().inflate(R.menu.device, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (!isDelete) {
			menu.findItem(R.id.action_delete_device).setVisible(true);
			menu.findItem(R.id.action_cancel_delete).setVisible(false);
		} else {
			menu.findItem(R.id.action_delete_device).setVisible(false);
			menu.findItem(R.id.action_cancel_delete).setVisible(true);
		}

		if (!isEdit) {
			menu.findItem(R.id.action_change_name).setVisible(true);
			menu.findItem(R.id.action_cancel_change).setVisible(false);
		} else {
			menu.findItem(R.id.action_change_name).setVisible(false);
			menu.findItem(R.id.action_cancel_change).setVisible(true);
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add_device:
			savePosition();
			mDeviceDataSet.option = OptionEnum.ITEM_ADD;
			mAdapter.notifyDataSetChanged();
			Intent intent = new Intent(mContext, AddDeviceActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.scale_fade_out);
			break;
		case R.id.action_delete_device:
			isDelete = true;
			isEdit = false;
			mCallback.onStateChange(STATE_DELETE);
			mDeviceDataSet.option = OptionEnum.ITEM_DELETE;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.action_cancel_delete:
			isDelete = false;
			mCallback.onStateChange(STATE_NORMAL);
			mDeviceDataSet.option = null;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.action_change_name:
			isEdit = true;
			isDelete = false;
			mCallback.onStateChange(STATE_EDIT);
			mDeviceDataSet.option = OptionEnum.ITEM_UPDATE;
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.action_cancel_change:
			isEdit = false;
			mCallback.onStateChange(STATE_NORMAL);
			mDeviceDataSet.option = null;
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void resetList() {
		mDeviceDataSet.option = null;
		mAdapter.notifyDataSetChanged();
		this.isDelete = false;
		this.isEdit = false;
		mCallback.onStateChange(STATE_NORMAL);
	}

	private void initData() {
		mDeviceDataSet = new DeviceDataSet();
		mDeviceDataSet.option = OptionEnum.ITEM_ADD;
		ArrayList<DeviceInfo> deviceInfos = new ArrayList<>();

		DeviceInfo deviceInfo = null;
		for (int i = 0; i < 2; i++) {
			deviceInfo = new DeviceInfo(DeviceInfo.TYPE_BP, "血压计 -" + i);
			deviceInfos.add(deviceInfo);
		}
		for (int i = 0; i < 2; i++) {
			deviceInfo = new DeviceInfo(DeviceInfo.TYPE_BS, "血糖仪" + i);
			deviceInfos.add(deviceInfo);
		}
		for (int i = 0; i < 2; i++) {
			deviceInfo = new DeviceInfo(DeviceInfo.TYPE_FH, "胎心仪" + i);
			deviceInfos.add(deviceInfo);
		}
		mDeviceDataSet.deviceInfos = deviceInfos;
	}

	@OnItemClick(R.id.device_listview)
	public void checkHistory(int i) {
		savePosition();
		Intent intent = null;
		String type = mDeviceDataSet.deviceInfos.get(i).type;
		if (DeviceInfo.TYPE_BS.equals(type)) {// 血糖
			intent = new Intent(mContext, BSHistoryActivity.class);
		} else if (DeviceInfo.TYPE_BP.equals(type)) {// 血压
			intent = new Intent(mContext, BPHistoryActivity.class);
		} else {// 胎心仪
			intent = new Intent(mContext, FHHistoryActivity.class);
		}
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
	}

	private void savePosition() {
		SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.SP_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(MainActivity.PREVIOUS_TAB_PAGE, MainActivity.PAGE_DEVICE);
		editor.commit();
	}

	class DeviceListAdapter extends BaseAdapter {

		// 数据集
		private DeviceDataSet mDeviceDataSet;
		private LayoutInflater mInflater;

		public DeviceListAdapter(Context context, DeviceDataSet deviceDataSet) {
			this.mDeviceDataSet = deviceDataSet;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mDeviceDataSet.deviceInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.device_item, null);
				mHolder = new ViewHolder();

				/* 获取 视图控件 */
				mHolder.mDeviceIcon = ButterKnife.findById(convertView,
						R.id.device_icon);
				mHolder.mDeviceName = ButterKnife.findById(convertView,
						R.id.device_namge);
				mHolder.mBtnDelete = ButterKnife.findById(convertView,
						R.id.btn_delete);
				mHolder.mBtnUpdate = ButterKnife.findById(convertView,
						R.id.btn_update);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();// 取出viewholder对象
			}

			if (OptionEnum.ITEM_DELETE.equals(mDeviceDataSet.option)) {
				mHolder.mBtnDelete.setVisibility(View.VISIBLE);
				mHolder.mBtnUpdate.setVisibility(View.GONE);
			} else if (OptionEnum.ITEM_UPDATE.equals(mDeviceDataSet.option)) {
				mHolder.mBtnDelete.setVisibility(View.GONE);
				mHolder.mBtnUpdate.setVisibility(View.VISIBLE);
			} else {
				mHolder.mBtnDelete.setVisibility(View.GONE);
				mHolder.mBtnUpdate.setVisibility(View.GONE);
			}

			mHolder.mDeviceIcon.setType(mDeviceDataSet.deviceInfos
					.get(position).type);
			mHolder.mDeviceName.setText(mDeviceDataSet.deviceInfos
					.get(position).name);
			mHolder.mBtnDelete
					.setOnClickListener(new BtnClickListener(position));
			mHolder.mBtnUpdate
					.setOnClickListener(new BtnClickListener(position));
			return convertView;
		}

	}

	public void update(final int i) {
		String name = mDeviceDataSet.deviceInfos.get(i).name;
		LayoutInflater factory = LayoutInflater.from(mContext);
		View mainView = factory.inflate(R.layout.change_device_name, new LinearLayout(mContext), false);
		final EditText txtDeviceName = ButterKnife.findById(mainView,
				R.id.txt_device_name);
		txtDeviceName.setText(name);
		txtDeviceName.setSelection(name.length());
		DialogUtils.showViewDialog(mContext, R.drawable.ic_action_edit,
				"设备名称", mainView, "确定", "取消", new BtnCallback() {
					@Override
					public void click(final DialogInterface dialog,
							final int which) {
						String deviceName = txtDeviceName.getText().toString();
						mDeviceDataSet.deviceInfos.get(i).name = deviceName;
						mAdapter.notifyDataSetChanged();
					}
				}, null);
	}

	class BtnClickListener implements View.OnClickListener {
		private int mPosition;

		public BtnClickListener(int position) {
			this.mPosition = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_delete:
				DialogUtils.dialogTwoButton(mContext, "删除设备？", "删除", "删除",
						"取消", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mDeviceDataSet.deviceInfos.remove(mPosition);
								mAdapter.notifyDataSetChanged();
							}
						});

				break;
			case R.id.btn_update:
				update(mPosition);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Save widget
	 * 
	 * @author MGC01
	 */
	final class ViewHolder {
		public DeviceIcon mDeviceIcon;
		public TextView mDeviceName;
		public ImageButton mBtnDelete;
		public ImageButton mBtnUpdate;
	}
}

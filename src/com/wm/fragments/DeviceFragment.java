package com.wm.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.wm.activity.AddDeviceActivity;
import com.wm.activity.HistoryActivity;
import com.wm.activity.R;
import com.wm.customview.DeviceIcon;
import com.wm.db.DeviceDBManager;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.utils.BtnCallback;
import com.wm.utils.DialogUtils;
import com.wm.utils.TabPager;

public class DeviceFragment extends Fragment {

	public final static String KEY_DEVICE_INFO = "device_info";
	public static int STATE_EDIT = 0;
	public static int STATE_DELETE = 1;
	public static int STATE_NORMAL = 2;

	@InjectView(R.id.device_listview)
	ListView mDeviceListView;

	private OnStateChangeListener mCallback;
	private DeviceDataSet mDeviceDataSet;
	private DeviceListAdapter mAdapter;
	private Context mContext;
	private boolean isDelete = false;
	private boolean isEdit = false;
	private TabPager mTabPager;
	private List<DeviceInfo> mDevices;
	private DeviceDBManager mDeviceDBManager;
	private Handler mHandler;

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
		mHandler = new Handler();
		mDeviceDBManager = DeviceDBManager.getInstance(mContext);
		mTabPager = TabPager.getInstance(mContext);
		setHasOptionsMenu(true);// 显示fragment的menu

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mDevices = mDeviceDBManager.getAllDevices();
				if (mDeviceDataSet == null)
					mDeviceDataSet = new DeviceDataSet();
				mDeviceDataSet.option = OptionEnum.ITEM_ADD;
				mDeviceDataSet.deviceInfos = mDevices;
				if (mAdapter == null) {
					mAdapter = new DeviceListAdapter(mContext, mDeviceDataSet);
					mDeviceListView.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
				}
			}
		}, 500);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
			mTabPager.savePosition(TabPager.PAGE_DEVICE);
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

	@OnItemClick(R.id.device_listview)
	public void checkHistory(int i) {
		mTabPager.savePosition(TabPager.PAGE_DEVICE);
		String type = mDeviceDataSet.deviceInfos.get(i).type;
		Intent intent = new Intent(mContext, HistoryActivity.class);
		intent.putExtra(DeviceInfo.INTENT_TYPE, type);
		intent.putExtra(KEY_DEVICE_INFO, mDeviceDataSet.deviceInfos.get(i));
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.scale_fade_out);
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
				mHolder.mBtnContainer = ButterKnife.findById(convertView, 
						R.id.btn_container);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();// 取出viewholder对象
			}

			if (OptionEnum.ITEM_DELETE.equals(mDeviceDataSet.option)) {
				mHolder.mBtnDelete.setVisibility(View.VISIBLE);
				mHolder.mBtnUpdate.setVisibility(View.GONE);
				mHolder.mBtnContainer.setVisibility(View.VISIBLE);
			} else if (OptionEnum.ITEM_UPDATE.equals(mDeviceDataSet.option)) {
				mHolder.mBtnDelete.setVisibility(View.GONE);
				mHolder.mBtnUpdate.setVisibility(View.VISIBLE);
				mHolder.mBtnContainer.setVisibility(View.VISIBLE);
			} else {
				mHolder.mBtnDelete.setVisibility(View.GONE);
				mHolder.mBtnUpdate.setVisibility(View.GONE);
				mHolder.mBtnContainer.setVisibility(View.GONE);
			}

			mHolder.mDeviceIcon.setType(mDeviceDataSet.deviceInfos
					.get(position).type);
			mHolder.mDeviceName.setText(mDeviceDataSet.deviceInfos
					.get(position).name.trim());
			mHolder.mBtnDelete
					.setOnClickListener(new BtnClickListener(position));
			mHolder.mBtnUpdate
					.setOnClickListener(new BtnClickListener(position));
			return convertView;
		}
	}

	public void update(final int i) {
		System.out.println("click " + i);
		String name = mDeviceDataSet.deviceInfos.get(i).name;
		LayoutInflater factory = LayoutInflater.from(mContext);
		View mainView = factory.inflate(R.layout.change_device_name,
				new LinearLayout(mContext), false);
		final EditText txtDeviceName = ButterKnife.findById(mainView,
				R.id.txt_device_name);
		txtDeviceName.setText(name);
		txtDeviceName.setSelection(name.length());
		DialogUtils.showViewDialog(mContext, R.drawable.ic_action_edit, "设备名称",
				mainView, "确定", "取消", new BtnCallback() {
					@Override
					public void click(final DialogInterface dialog,
							final int which) {
						String deviceName = txtDeviceName.getText().toString();
						mDeviceDataSet.deviceInfos.get(i).name = deviceName;
						new Thread(new Runnable() {
							@Override
							public void run() {
								mDeviceDBManager
										.updateDevice(mDeviceDataSet.deviceInfos
												.get(i));
							}
						}).start();
						mAdapter.notifyDataSetChanged();
					}
				}, null).show();
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
								final DeviceInfo tmpDeviceInfo = mDeviceDataSet.deviceInfos
										.get(mPosition);
								new Thread(new Runnable() {
									@Override
									public void run() {
										mDeviceDBManager
												.removeDeviceById(tmpDeviceInfo.id);
									}
								}).start();
								mDeviceDataSet.deviceInfos.remove(mPosition);
								mAdapter.notifyDataSetChanged();
							}
						});

				break;
			case R.id.btn_update:
				System.out.println("position " + mPosition);
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
		public Button mBtnDelete;
		public Button mBtnUpdate;
		public RelativeLayout mBtnContainer;
		
	}
}

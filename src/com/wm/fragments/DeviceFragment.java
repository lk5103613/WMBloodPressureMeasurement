package com.wm.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.wm.activity.AddDeviceActivity;
import com.wm.activity.HistoryActivity;
import com.wm.activity.R;
import com.wm.customview.DeviceIcon;
import com.wm.db.DeviceDBManager;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.utils.DialogUtils;
import com.wm.utils.TabPager;

public class DeviceFragment extends Fragment implements View.OnClickListener {

	public final static String KEY_DEVICE_INFO = "device_info";
	public static int STATE_EDIT = 0;
	public static int STATE_DELETE = 1;
	public static int STATE_NORMAL = 2;

	@InjectView(R.id.device_listview)
	ListView mDeviceListView;
	@InjectView(R.id.device_toolbar)
	RelativeLayout mToolbar;
//	@InjectView(R.id.empty)
//	TextView mEmptyView;

	private OnStateChangeListener mCallback;
	private DeviceDataSet mDeviceDataSet;
	private DeviceListAdapter mAdapter;
	private Context mContext;
	private TabPager mTabPager;
	private List<DeviceInfo> mDevices;
	private DeviceDBManager mDeviceDBManager;
	private Handler mHandler;

	private TextView actionAdd, actionDel, actionCancelDel, actionUpd,
			actionCancelUpd;
	AlertDialog alertDialog;
	View dialogView;
	
	AlertDialog changeNameDialog;
	View changeNameView;
	Button btnChangeNameYes, btnChangeNameNo;
	EditText nameEdit;
	Button btnDelYes = null;
	Button btnDelNo = null;
	
	AlertDialog delDialog;

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
//		mDeviceListView.setEmptyView(mEmptyView);  //add empty view
		return view;
	}

	public void showMenuDialog() {
		if (alertDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(true);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			dialogView = inflater.inflate(R.layout.dialog_menu_layout, null);
			builder.setView(dialogView);
			alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(true);
		}
		
		alertDialog.show();
		
		actionAdd = ButterKnife.findById(dialogView, R.id.action_add);
		actionDel = ButterKnife.findById(dialogView, R.id.action_delete_device);
		actionCancelDel = ButterKnife.findById(dialogView, R.id.action_cancel_delete);
		actionUpd = ButterKnife.findById(dialogView, R.id.action_change_name);
		actionCancelUpd = ButterKnife.findById(dialogView, R.id.action_cancel_change);

		actionAdd.setOnClickListener(this);
		actionDel.setOnClickListener(this);
		actionCancelDel.setOnClickListener(this);
		actionCancelUpd.setOnClickListener(this);
		actionUpd.setOnClickListener(this);

		Window dialogWindow = alertDialog.getWindow();
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������

		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		params.gravity = Gravity.TOP;
		params.y = mToolbar.getHeight() + 10;
		params.x = 200;

		Point p = new Point();
		d.getSize(p);
		params.width = (int) (p.x * 0.65); // �������Ϊ��Ļ��0.65

		dialogWindow.setAttributes(params);

	}

	@OnClick(R.id.menu_img)
	public void menuClick() {
		showMenuDialog();
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
				//mDeviceDataSet.option = OptionEnum.ITEM_ADD;
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

	public void resetList() {
		mDeviceDataSet.option = null;
		mAdapter.notifyDataSetChanged();
		mCallback.onStateChange(STATE_NORMAL);
		actionCancelDel.setVisibility(View.GONE);
		actionCancelUpd.setVisibility(View.GONE);
		actionDel.setVisibility(View.VISIBLE);
		actionUpd.setVisibility(View.VISIBLE);
		mTabPager.savePosition(TabPager.PAGE_DEVICE);
		
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

		// ���ݼ�
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
			System.out.println(" position " + position);
			ViewHolder mHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.device_item, null);
				mHolder = new ViewHolder();

				/* ��ȡ ��ͼ�ؼ� */
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
				mHolder = (ViewHolder) convertView.getTag();// ȡ��viewholder����
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
		System.out.println("update " +i);
		String name = mDeviceDataSet.deviceInfos.get(i).name;
		if (changeNameDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(true);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View mainView = inflater.inflate(R.layout.dialog_change_name,
					new LinearLayout(mContext), false);
			nameEdit = (EditText)mainView.findViewById(R.id.txt_device_name);
			btnChangeNameYes = (Button) mainView.findViewById(R.id.btn_change_name_yes);
			btnChangeNameNo = (Button)mainView.findViewById(R.id.btn_change_name_no);
			changeNameDialog = builder.create();
			changeNameDialog.setView(mainView, 0, 0, 0, 0);
			changeNameDialog.setCanceledOnTouchOutside(true);
		}
		btnChangeNameYes.setOnClickListener(new BtnClickListener(i));
		btnChangeNameNo.setOnClickListener(new BtnClickListener(i));
		changeNameDialog.show();
		nameEdit.setText(name);
		nameEdit.setSelection(name.length());
	}
	
	public void delete(int position) {
		
		if(delDialog ==null) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setCancelable(true);
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View mainView = inflater.inflate(R.layout.dialog_delete,
					new LinearLayout(mContext), false);
			TextView title = (TextView) mainView.findViewById(R.id.title);
			btnDelYes = (Button) mainView.findViewById(R.id.btn_del_yes);
			btnDelNo = (Button)mainView.findViewById(R.id.btn_del_no);
			delDialog = builder.create();
			delDialog.setView(mainView, 0, 0, 0, 0);
			delDialog.setCanceledOnTouchOutside(true);
		}

		btnDelYes.setOnClickListener(new BtnClickListener(position));
		btnDelNo.setOnClickListener(new BtnClickListener(position));
		
		delDialog.show();
		Window dialogWindow = delDialog.getWindow();
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������

		WindowManager.LayoutParams params = dialogWindow.getAttributes();

		Point p = new Point();
		d.getSize(p);
		params.width = (int) (p.x * 0.75); // �������Ϊ��Ļ��0.65
		params.height = (int) (p.y * 0.3);
		dialogWindow.setAttributes(params);
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
				delete(mPosition);
				break;
			case R.id.btn_update:
				update(mPosition);
				break;
			case R.id.btn_change_name_no:
				changeNameDialog.dismiss();
				break;
			case R.id.btn_change_name_yes:
				System.out.println("mposition " +mPosition);
				String deviceName = nameEdit.getText().toString();
				mDeviceDataSet.deviceInfos.get(mPosition).name = deviceName;
				new Thread(new Runnable() {
					@Override
					public void run() {
						mDeviceDBManager.updateDevice(mDeviceDataSet.deviceInfos
										.get(mPosition));
					}
				}).start();
				mAdapter.notifyDataSetChanged();
				changeNameDialog.dismiss();
				break;
			case R.id.btn_del_no:
				delDialog.dismiss();
				break;
			case R.id.btn_del_yes:
				final DeviceInfo tmpDeviceInfo = mDeviceDataSet.deviceInfos
						.get(mPosition);
				new Thread(new Runnable() {
					@Override
					public void run() {
						mDeviceDBManager.removeDeviceById(tmpDeviceInfo.id);
					}
				}).start();
				mDeviceDataSet.deviceInfos.remove(mPosition);
				mAdapter.notifyDataSetChanged();
				delDialog.dismiss();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_add:
			actionCancelDel.setVisibility(View.GONE);
			actionCancelUpd.setVisibility(View.GONE);
			actionDel.setVisibility(View.VISIBLE);
			actionUpd.setVisibility(View.VISIBLE);
			mTabPager.savePosition(TabPager.PAGE_DEVICE);
			mDeviceDataSet.option = OptionEnum.ITEM_ADD;
			mAdapter.notifyDataSetChanged();
			Intent intent = new Intent(mContext, AddDeviceActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_right,
					R.anim.scale_fade_out);
			break;
		case R.id.action_delete_device:
			mCallback.onStateChange(STATE_DELETE);
			mDeviceDataSet.option = OptionEnum.ITEM_DELETE;
			mAdapter.notifyDataSetChanged();
			v.setVisibility(View.GONE);
			actionCancelDel.setVisibility(View.VISIBLE);
			actionCancelUpd.setVisibility(View.GONE);
			actionUpd.setVisibility(View.VISIBLE);
			break;
		case R.id.action_cancel_delete:
			mCallback.onStateChange(STATE_NORMAL);
			mDeviceDataSet.option = null;
			mAdapter.notifyDataSetChanged();
			v.setVisibility(View.GONE);
			actionDel.setVisibility(View.VISIBLE);
			break;
		case R.id.action_change_name:
			mCallback.onStateChange(STATE_EDIT);
			mDeviceDataSet.option = OptionEnum.ITEM_UPDATE;
			mAdapter.notifyDataSetChanged();
			v.setVisibility(View.GONE);
			actionCancelUpd.setVisibility(View.VISIBLE);
			actionCancelDel.setVisibility(View.GONE);
			actionDel.setVisibility(View.VISIBLE);
			break;
		case R.id.action_cancel_change:
			mCallback.onStateChange(STATE_NORMAL);
			mDeviceDataSet.option = null;
			mAdapter.notifyDataSetChanged();
			v.setVisibility(View.GONE);
			actionUpd.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		
		alertDialog.dismiss();

	}
}

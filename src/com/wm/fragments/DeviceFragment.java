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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

import com.wm.customview.DeviceIcon;
import com.wm.entity.DeviceDataSet;
import com.wm.entity.DeviceInfo;
import com.wm.entity.OptionEnum;
import com.wm.entity.TypeEnum;
import com.wm.utils.DialogUtils;
import com.wm.wmbloodpressuremeasurement.AddDeviceActivity;
import com.wm.wmbloodpressuremeasurement.BloodHistoryActivity;
import com.wm.wmbloodpressuremeasurement.MainActivity;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceFragment extends Fragment{
	@InjectView(R.id.device_listview)
	ListView mDeviceListView;

	DeviceDataSet deviceDataSet;
	EditText mNameEditText;
	DeviceListAdapter adapter;
	Context context;
	private boolean isDelete = false;
	private boolean isEdit = false;

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
		adapter = new DeviceListAdapter(context, deviceDataSet);
		mDeviceListView.setAdapter(adapter);
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
			deviceDataSet.option = OptionEnum.ITEM_ADD;
			adapter.notifyDataSetChanged();
			Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_fade_out);
			
			break;
		case R.id.action_delete_device:
			isDelete= true;
			deviceDataSet.option = OptionEnum.ITEM_DELETE;
			adapter.notifyDataSetChanged();
			break;
		case R.id.action_cancel_delete:
			isDelete= false;
			deviceDataSet.option = null;
			adapter.notifyDataSetChanged();
			break;
		case R.id.action_change_name:
			isEdit = true;
			deviceDataSet.option = OptionEnum.ITEM_UPDATE;
			adapter.notifyDataSetChanged();
			break;
		case R.id.action_cancel_change:
			isEdit = false;
			deviceDataSet.option = null;
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
		for (int i = 0; i < 2; i++) {
			deviceInfo = new DeviceInfo(TypeEnum.TURGOSCOPE, "血压计 -" + i);
			deviceInfos.add(deviceInfo);
		}
		for (int i = 0; i < 2; i++) {
			deviceInfo = new DeviceInfo(TypeEnum.GLUCOMETER, "血糖仪" + i);
			deviceInfos.add(deviceInfo);
		}
		deviceDataSet.deviceInfos = deviceInfos;
	}


	@OnItemClick(R.id.device_listview)
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
	
	
	class DeviceListAdapter extends BaseAdapter{

		// 数据集
		private DeviceDataSet mDeviceDataSet;
		private LayoutInflater mInflater;
		private Context mContext;
		public DeviceListAdapter(Context context, DeviceDataSet deviceDataSet) {
			this.mContext = context;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.device_item, null);
				mHolder = new ViewHolder();
				
				/*获取 视图控件*/
				mHolder.mDeviceIcon = ButterKnife.findById(convertView,R.id.device_icon);
				mHolder.mDeviceName = ButterKnife.findById(convertView,R.id.device_namge);
				mHolder.mBtnDelete = ButterKnife.findById(convertView,R.id.btn_delete);
				mHolder.mBtnUpdate = ButterKnife.findById(convertView,R.id.btn_update);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder)convertView.getTag();//取出viewholder对象
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
			
			mHolder.mDeviceIcon.setType(mDeviceDataSet.deviceInfos.get(position).getType());
			mHolder.mDeviceName.setText(mDeviceDataSet.deviceInfos.get(position).getName());
			mHolder.mBtnDelete.setOnClickListener(new BtnClickListener(position));
			mHolder.mBtnUpdate.setOnClickListener(new BtnClickListener(position));
			return convertView;
		}
		
	}
	
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
	
	
	class BtnClickListener  implements View.OnClickListener{
		private int mPosition;
		public BtnClickListener(int position) {
			this.mPosition = position;
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_delete:
				DialogUtils.dialogTwoButton(getActivity(),
						"删除设备？", "删除",
						"删除","取消", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								deviceDataSet.deviceInfos.remove(mPosition);
								adapter.notifyDataSetChanged();
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
	
	
	/**
	 * Save widget
	 * @author MGC01
	 */
	final class ViewHolder{    
		public DeviceIcon mDeviceIcon;           
		public TextView mDeviceName;
		public ImageButton mBtnDelete;
		public ImageButton mBtnUpdate;
	} 
}

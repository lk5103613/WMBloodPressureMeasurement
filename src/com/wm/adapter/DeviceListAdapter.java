package com.wm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.entity.DeviceDataSet;
import com.wm.entity.OptionEnum;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
	 // ���ݼ�
	 private DeviceDataSet deviceDataSet;
	 DeviceListCallBack callBack;
	 Context context;
	
	 public DeviceListAdapter(DeviceDataSet dataset, DeviceListCallBack callBack) {
		 super();
		 this.deviceDataSet = dataset;
		 this.callBack = callBack;
	 }
	
	 @Override
	 public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 // ����һ��View�������ֱ��ʹ��ϵͳ�ṩ�Ĳ��֣�����һ��TextView
		 View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
		 // ����һ��ViewHolder
		 ViewHolder holder = new ViewHolder(viewGroup.getContext(),view,callBack, i);
		 
		 return holder;
	 }
	
	 @Override
	 public void onBindViewHolder(ViewHolder viewHolder, int i) {
		 // �����ݵ�ViewHolder��
		 switch (deviceDataSet.deviceInfos.get(i).getType()) {
		case TURGOSCOPE:
			viewHolder.deviceImg.setImageResource(R.drawable.home_tab_selector);
			break;

		default:
			break;
		}
		 
		viewHolder.deviceName.setText(deviceDataSet.deviceInfos.get(i).getName());
		
		if (deviceDataSet.option.equals(OptionEnum.ITEM_DELETE)) {
			viewHolder.btnDelete.setVisibility(View.VISIBLE);
			viewHolder.btnUpdate.setVisibility(View.GONE);
		} else if (deviceDataSet.option.equals(OptionEnum.ITEM_UPDATE)) {
			viewHolder.btnDelete.setVisibility(View.GONE);
			viewHolder.btnUpdate.setVisibility(View.VISIBLE);
		} else {
			viewHolder.btnDelete.setVisibility(View.GONE);
			viewHolder.btnUpdate.setVisibility(View.GONE);
		}
		 
	 }
	
	 @Override
	 public int getItemCount() {
		 return deviceDataSet.deviceInfos.size();
	 }
	
	 public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
	
		 public ImageView deviceImg;
		 public TextView deviceName;
		 public ImageButton btnDelete;
		 public ImageButton btnUpdate;
		 
		 Context context;
		 DeviceListCallBack callBack;
		 int position;
		
		 public ViewHolder(Context context,final View itemView, DeviceListCallBack callBack, int position) {
			 super(itemView);
			 
			 this.context = context;
			 this.callBack = callBack;
			 this.position = position;
					 
			 deviceImg = (ImageView) itemView.findViewById(R.id.device_img);
			 deviceName = (TextView) itemView.findViewById(R.id.device_namge);
			 btnUpdate = (ImageButton) itemView.findViewById(R.id.btn_update);
			 btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);
			 
			 btnUpdate.setOnClickListener(this);
			 btnDelete.setOnClickListener(this);
			 
//			 Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//			 
//			 titleTxt.setTypeface(typeface);
//			 desTxt.setTypeface(typeface);
//			 inforTxt.setTypeface(typeface);
			
		 }

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_delete:
				callBack.delete(position);
				break;
			case R.id.btn_update:
				callBack.update(position);
				break;
			default:
				break;
			}
			
		}
		
		
	 }
	 
	 public interface DeviceListCallBack{
		 public void update(int i);
		 
		 public void delete(int i);
	 }
	 
}

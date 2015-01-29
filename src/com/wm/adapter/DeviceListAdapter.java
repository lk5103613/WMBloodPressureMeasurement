package com.wm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.entity.DeviceDataSet;
import com.wm.entity.OptionEnum;
import com.wm.wmbloodpressuremeasurement.R;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{
	 // 数据集
	 private DeviceDataSet deviceDataSet;
	 Context context;
	
	 public DeviceListAdapter(DeviceDataSet dataset) {
		 super();
		 this.deviceDataSet = dataset;
	 }
	
	 @Override
	 public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
		 View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
		 // 创建一个ViewHolder
		 ViewHolder holder = new ViewHolder(viewGroup.getContext(),view);
		 return holder;
	 }
	
	 @Override
	 public void onBindViewHolder(ViewHolder viewHolder, int i) {
		 // 绑定数据到ViewHolder上
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
	
	 public static class ViewHolder extends RecyclerView.ViewHolder{
	
		 public ImageView deviceImg;
		 public TextView deviceName;
		 public Button btnDelete;
		 public Button btnUpdate;
		
		 public ViewHolder(Context context,final View itemView) {
			 super(itemView);
			 
			 deviceImg = (ImageView) itemView.findViewById(R.id.device_img);
			 deviceName = (TextView) itemView.findViewById(R.id.device_namge);
			 btnUpdate = (Button) itemView.findViewById(R.id.btn_update);
			 btnDelete = (Button) itemView.findViewById(R.id.btn_delete);
			 
			 
//			 Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//			 
//			 titleTxt.setTypeface(typeface);
//			 desTxt.setTypeface(typeface);
//			 inforTxt.setTypeface(typeface);
			
		 }
	 }
}

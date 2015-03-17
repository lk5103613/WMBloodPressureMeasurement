package com.wm.receiver;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;

import com.wm.activity.R;
import com.wm.utils.DialogUtils;
import com.wm.utils.NetUtils;

public class NetChangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String netType = NetUtils.isInternetConnectionAvailable(context);
		if (netType == null) {
			return;
		}
		if (NetUtils.NET_TYPE_WIFI.equals(netType) ){
			Toast.makeText(context, "wifi", Toast.LENGTH_LONG).show();
		} else if (NetUtils.NET_TYPE_GPRS.equals(netType)) {
			Toast.makeText(context, "gprs", Toast.LENGTH_LONG).show();
			
			DialogUtils.dialogInReceiver(context, "上传数据", "当前为数据流量, 是否要上传数据？", "是", "否", new DialogInterface.OnClickListener() {  
	            @Override 
	            public void onClick(DialogInterface dialog, int arg1) {
	                dialog.dismiss();
	            }  
	        }, null);
		}
	}
	
}

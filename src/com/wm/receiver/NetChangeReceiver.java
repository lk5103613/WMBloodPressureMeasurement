package com.wm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wm.utils.NetUtils;

public class NetChangeReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int netType = NetUtils.getConnectState(context);
		switch (netType) {
			case NetUtils.TYPE_GPRS:
				break;
			case NetUtils.TYPE_WIFI:
				break;
			case NetUtils.TYPE_NONE:
				break;
		}
//		if (NetUtils.NET_TYPE_WIFI.equals(netType) ){
//			Toast.makeText(context, "wifi", Toast.LENGTH_LONG).show();
//		} else if (NetUtils.NET_TYPE_GPRS.equals(netType)) {
//			Toast.makeText(context, "gprs", Toast.LENGTH_LONG).show();
//			
//			DialogUtils.dialogInReceiver(context, "上传数据", "当前为数据流量, 是否要上传数据？", "是", "否", new DialogInterface.OnClickListener() {  
//	            @Override 
//	            public void onClick(DialogInterface dialog, int arg1) {
//	                dialog.dismiss();
//	            }
//	        }, null);
//		}
	}
	
}

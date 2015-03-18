package com.wm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_GPRS = 0;
	public static final int TYPE_WIFI = 1;
	  /**
     * check existing Internet connection
     * @param _context
     * @return
     */
    public static final int getConnectState(final Context _context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
        	return TYPE_WIFI;
        }
        if (mobNetInfo != null && mobNetInfo.isConnected()) {
        	return TYPE_GPRS;
        }
		return TYPE_NONE;
    }

}

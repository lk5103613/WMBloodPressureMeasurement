package com.wm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	
	public static final String NET_TYPE_WIFI = "WIFI";
	public static final String NET_TYPE_GPRS = "GPRS";
	  /**
     * check existing internet connection
     * @param _context
     * @return
     */
    public static final String isInternetConnectionAvailable(final Context _context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
        	return NET_TYPE_WIFI;
        }
        if (mobNetInfo != null && mobNetInfo.isConnected()) {
        	return NET_TYPE_GPRS;
        }
		return null;
    }

}

package com.wm.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {
	
	/**
	 * show dialog with the positive and negative button
	 * 
	 * @param context
	 * @param message
	 * @param title
	 * @param positive
	 * @param negative
	 * @param Listener
	 */
	public static void dialogTwoButton(Context context, String message,
			String title, String positive, String negative,
			OnClickListener Listener) {
		
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(positive, Listener);
		builder.setNegativeButton(negative, null);
		builder.setCancelable(true);
		builder.create().show();
	}
	
}

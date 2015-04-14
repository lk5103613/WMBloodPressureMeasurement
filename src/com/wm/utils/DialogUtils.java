package com.wm.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wm.activity.R;

public class DialogUtils {
	
	public final static int NO_ICON = -1;
	
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
	
	 public static AlertDialog showAlertDialog(Context context, int drawableId, String title, String pos, String nega, final BtnCallback posCallback, final BtnCallback negCallBack) {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setCancelable(false);
	        if(drawableId != NO_ICON) {
		        builder.setIcon(drawableId);
	        }
	        builder.setTitle(title);
	        builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                if(posCallback == null) {
	                    dialog.dismiss();
	                } else {
	                    posCallback.click(dialog, which);
	                }
	            }
	        });
	        builder.setNegativeButton(nega, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                if(negCallBack == null) {
	                    dialog.dismiss();
	                } else {
	                    negCallBack.click(dialog, whichButton);
	                }
	            }
	        });
	        return builder.create();
	    }

	    public static AlertDialog showAlertDialog(Context context, int drawableId, String title, String message, String pos, String nega, final BtnCallback posCallback, final BtnCallback negCallBack) {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setCancelable(false);
	        if(drawableId != NO_ICON) {
		        builder.setIcon(drawableId);
	        }
	        builder.setTitle(title);
	        builder.setMessage(message);
	        builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                if(posCallback == null) {
	                    dialog.dismiss();
	                } else {
	                    posCallback.click(dialog, which);
	                }
	            }
	        });
	       
	        builder.setNegativeButton(nega, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                if(negCallBack == null) {
	                    dialog.dismiss();
	                } else {
	                    negCallBack.click(dialog, whichButton);
	                }
	            }
	        });
	        return builder.create();
	    }

	    public static AlertDialog showViewDialog(Context context, int drawableId, String title, View view, String pos, String neg, final BtnCallback posCallback , final BtnCallback negCallback) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setCancelable(true);
	        builder.setIcon(drawableId);
	        builder.setTitle(title);
	        builder.setView(view);
	        if(pos != null) {
	            builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    if(posCallback == null) {
	                        dialog.dismiss();
	                    } else {
	                        posCallback.click(dialog, whichButton);
	                    }
	                }
	            });
	        }
	        if(neg != null) {
	            builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    if(negCallback == null) {
	                        dialog.dismiss();
	                    } else {
	                        negCallback.click(dialog, whichButton);
	                    }
	                }
	            });
	        }
	        AlertDialog alertDialog = builder.create();
	        return alertDialog;
	    }


	    public static ProgressDialog createProgressDialog(Context context, String title, String message) {
	        ProgressDialog progressDialog = new ProgressDialog(context);
	        progressDialog.setCanceledOnTouchOutside(false);
	        progressDialog.setTitle(title);
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setMessage(message);
	        return progressDialog;
	    }
	    
	    public static AlertDialog createDialog(Activity context,
	    		View.OnClickListener yesClickListener,
	    		View.OnClickListener noClickListener){
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(true);
			LayoutInflater inflater = context.getLayoutInflater();
			View mainView = inflater.inflate(R.layout.dialog_delete,
					new LinearLayout(context), false);
			Button btnDelYes = (Button) mainView.findViewById(R.id.btn_del_yes);
			Button btnDelNo = (Button)mainView.findViewById(R.id.btn_del_no);
			btnDelYes.setOnClickListener(yesClickListener);
			btnDelNo.setOnClickListener(noClickListener);
			AlertDialog alertDialog = builder.create();
			alertDialog.setView(mainView, 0, 0, 0, 0);
			alertDialog.setCanceledOnTouchOutside(true);
	    	return alertDialog;
	    }
	
}

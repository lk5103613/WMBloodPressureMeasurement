package com.wm.utils;

import com.wm.activity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
	    
	    public static AlertDialog createChangeNameDialot(Activity context, View.OnClickListener yesClickListener,
	    		View.OnClickListener noClickListener){
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(true);
			LayoutInflater inflater = context.getLayoutInflater();
			View mainView = inflater.inflate(R.layout.dialog_change_name,
					new LinearLayout(context), false);
			EditText nameEdit = (EditText)mainView.findViewById(R.id.txt_device_name);
			Button btnChangeNameYes = (Button) mainView.findViewById(R.id.btn_change_name_yes);
			Button btnChangeNameNo = (Button)mainView.findViewById(R.id.btn_change_name_no);
			btnChangeNameYes.setOnClickListener(yesClickListener);
			btnChangeNameNo.setOnClickListener(noClickListener);
			AlertDialog alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(true);
	    	return alertDialog;
	    }

//	    public static void dialogInReceiver(Context mContext, String title, String message,
//	    		String btnNav, String btnPos, OnClickListener btnPosListener, OnClickListener btnNavListener){
//	    	Builder builder=new Builder(mContext);  
//	        builder.setIcon(R.drawable.ic_launcher);  
//	        builder.setTitle(title);  
//	        builder.setMessage(message);  
//	        builder.setNegativeButton("ÊÇ", btnPosListener);  
//	        builder.setPositiveButton("·ñ", btnNavListener);
//	        Dialog dialog=builder.create();
//	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//	        dialog.show(); 
//	    }
	
}

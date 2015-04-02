package com.wm.customview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wm.activity.R;

public class MenuDialog extends DialogFragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.menu_dialog_layout, container,false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NORMAL; 
		setStyle(style, R.style.dialog);
	}

//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//		View view = inflater.inflate(R.layout.menu_dialog_layout, null);
//		builder.setView(view);
//		AlertDialog alertDialog = builder.create();
//		WindowManager.LayoutParams WMLP = alertDialog.getWindow().getAttributes();
//		WMLP.gravity = Gravity.TOP;
//		WMLP.y = 200;
//		WMLP.x = 400;
//		alertDialog.getWindow().setAttributes(WMLP);
//		return alertDialog;
//	}
}

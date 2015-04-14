package com.wm.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.wm.activity.R;
import com.wm.activity.WelcomeActivity;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.IUploadEntity;
import com.wm.entity.RequestEntity;
import com.wm.utils.SharedPfUtil;
import com.wm.utils.SystemUtils;

public class CheckNeedUploadTask extends AsyncTask<Void, Void, Map<Integer, IUploadEntity>> {
	
	private Context mContext;
	private int mConnectState;
	private HistoryDBManager mDbManager;
	private AlertDialog mDialog;
	Button btnUdpNo;
	Button btnUpdYes;
	
	public CheckNeedUploadTask(Context context, AlertDialog mAlertDialog, Button btnUdpNo, Button btnUpdYes,int connectState){
		System.out.println("check need upload");
		this.mContext = context;
		this.mConnectState = connectState;
		this.mDialog = mAlertDialog;
		this.btnUdpNo = btnUdpNo;
		this.btnUpdYes = btnUpdYes;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDbManager = HistoryDBManager.getInstance(mContext);
	}

	@Override
	protected  Map<Integer, IUploadEntity> doInBackground(Void... params) {
		Map<Integer, IUploadEntity> uploadEntities = new HashMap<>();
		String auth = SharedPfUtil.getValue(mContext, WelcomeActivity.AUTH);
		if(!auth.equals("true")) {
			System.out.println("autho false");
			return uploadEntities;
		}
		
		List<BPResult> bpResults = mDbManager.getBpResultsByStatus(0);
		List<BSResult> bsResults = mDbManager.getBsResultsByStatus(0);
		List<FHResult> fhResults = mDbManager.getFhResultsByStatus(0);
		if(bpResults != null && bpResults.size() != 0) {
			RequestEntity<BPResult> uploadBps = new RequestEntity<BPResult>("test", "test", bpResults);
			uploadEntities.put(RequestEntity.TYPE_BP, uploadBps);
		}
		if(bsResults != null && bsResults.size() != 0) {
			RequestEntity<BSResult> uploadBss = new RequestEntity<>("test", "test", bsResults);
			uploadEntities.put(RequestEntity.TYPE_BS, uploadBss);
		}
		if(fhResults != null && fhResults.size() != 0) {
			RequestEntity<FHResult> uploadFhs = new RequestEntity<>("test", "test", fhResults);
			uploadEntities.put(RequestEntity.TYPE_FH, uploadFhs);
		}
		return uploadEntities;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(final Map<Integer, IUploadEntity> result) {
		if(result.isEmpty()) {
			return;
		}
		switch (mConnectState) {
			case SystemUtils.TYPE_GPRS:
				if(mDialog == null)
					return;
				btnUpdYes.setOnClickListener(new BtnClickListener(result));
				btnUdpNo.setOnClickListener(new BtnClickListener(result));
				if(!mDialog.isShowing()){
					mDialog.show();
				}
				break;
			case SystemUtils.TYPE_WIFI:
				new UploadDataTask(mContext).execute(result);
				break;
		}
	}
	
	class BtnClickListener implements View.OnClickListener{
		
		private Map<Integer, IUploadEntity> result;
		public BtnClickListener(Map<Integer, IUploadEntity> result) {
			this.result = result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_upload_no:
				mDialog.dismiss();
				break;
			case R.id.btn_upload_yes:
				mDialog.dismiss();
				new UploadDataTask(mContext).execute(result);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	public void hideProgress() {
	    if(mDialog != null) {
	        if(mDialog.isShowing()) { //check if dialog is showing.

	            //get the Context object that was used to great the dialog
	            Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();

	            //if the Context used here was an activity AND it hasn't been finished or destroyed
	            //then dismiss it
	            if(context instanceof Activity) { 
	                if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed()) 
	                	mDialog.dismiss();
	            } else //if the Context used wasnt an Activity, then dismiss it too
	            	mDialog.dismiss();
	        }
//	        mDialog = null;
	    }
	}

}

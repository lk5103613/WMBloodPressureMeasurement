package com.wm.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.wm.activity.WelcomeActivity;
import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.IUploadEntity;
import com.wm.entity.UploadEntity;
import com.wm.utils.NetUtils;
import com.wm.utils.SharedPfUtil;

public class CheckNeedUploadTask extends AsyncTask<Void, Void, Map<Integer, IUploadEntity>> {
	
	private Context mContext;
	private int mConnectState;
	private HistoryDBManager mDbManager;
	private AlertDialog mDialog;
	
	public CheckNeedUploadTask(Context context, int connectState, AlertDialog dialog){
		this.mContext = context;
		this.mConnectState = connectState;
		this.mDialog = dialog;
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
		int allBPCount = mDbManager.getUploadedBpCounts();
		if(allBPCount > 300)
			mDbManager.deleteBpDatas();
		int allBSCount = mDbManager.getUploadedBsCounts();
		if(allBSCount > 300) 
			mDbManager.deleteBsDatas();
		int allFHCount = mDbManager.getUploadedFhCounts();
		if(allFHCount > 300)
			mDbManager.deleteFhDatas();
		List<BPResult> bpResults = mDbManager.getBpResultsByStatus(0);
		List<BSResult> bsResults = mDbManager.getBsResultsByStatus(0);
		List<FHResult> fhResults = mDbManager.getFhResultsByStatus(0);
		if(bpResults != null && bpResults.size() != 0) {
			UploadEntity<BPResult> uploadBps = new UploadEntity<BPResult>("test", "test", bpResults);
			uploadEntities.put(UploadEntity.TYPE_BP, uploadBps);
		}
		if(bsResults != null && bsResults.size() != 0) {
			UploadEntity<BSResult> uploadBss = new UploadEntity<>("test", "test", bsResults);
			uploadEntities.put(UploadEntity.TYPE_BS, uploadBss);
		}
		if(fhResults != null && fhResults.size() != 0) {
			UploadEntity<FHResult> uploadFhs = new UploadEntity<>("test", "test", fhResults);
			uploadEntities.put(UploadEntity.TYPE_FH, uploadFhs);
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
			case NetUtils.TYPE_GPRS:
				mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ÊÇ", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new UploadDataTask(mContext).execute(result);
					}
				});
				if(mDialog.isShowing()) 
					return;
				mDialog.show();
				break;
			case NetUtils.TYPE_WIFI:
				new UploadDataTask(mContext).execute(result);
				break;
		}
	}

}

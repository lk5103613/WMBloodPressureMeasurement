package com.wm.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.IUploadEntity;
import com.wm.entity.UploadEntity;
import com.wm.utils.NetUtils;

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
		List<BPResult> bpResults = mDbManager.getBpResultsByStatus(0);
		List<BSResult> bsResults = mDbManager.getBsResultsByStatus(0);
		List<FHResult> fhResults = mDbManager.getFhResultsByStatus(0);
		if(bpResults != null && bpResults.size() != 0) {
			UploadEntity<BPResult> uploadBps = new UploadEntity<BPResult>("test", "test", bpResults);
			uploadEntities.put(UploadEntity.TYPE_BP, uploadBps);
		}
		if(bsResults != null && bpResults.size() != 0) {
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
	protected void onPostExecute(Map<Integer, IUploadEntity> result) {
		super.onPostExecute(result);
		if(result.isEmpty()) {
			return;
		}
		switch (mConnectState) {
			case NetUtils.TYPE_GPRS:
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

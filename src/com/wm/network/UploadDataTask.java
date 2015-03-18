package com.wm.network;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.IUploadEntity;
import com.wm.entity.ResponseData;
import com.wm.entity.UploadEntity;

public class UploadDataTask extends AsyncTask<Map<Integer, IUploadEntity>, Integer, ResponseData>{
	private Context mContext;
	private HistoryDBManager mDbManager;
	private UploadService mService;
	
	public UploadDataTask(Context context){
		this.mContext = context;
		mService = NetworkFactory.getUploadService();
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDbManager = HistoryDBManager.getInstance(mContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ResponseData doInBackground(Map<Integer, IUploadEntity>... params) {
		if(params == null || params.length == 0) {
			List<BPResult> bpResults = mDbManager.getBpResultsByStatus(0);
			List<BSResult> bsResults = mDbManager.getBsResultsByStatus(0);
			List<FHResult> fhResults = mDbManager.getFhResultsByStatus(0);
			if(bpResults != null && bpResults.size() != 0) {
				UploadEntity<BPResult> uploadBps = new UploadEntity<>("test", "test", bpResults);
				mService.uploadBloodpressure(uploadBps);
			}
			if(bsResults != null && bsResults.size() != 0) {
				UploadEntity<BSResult> uploadBss = new UploadEntity<>("test", "test", bsResults);
				mService.uploadBloodGlucose(uploadBss);
			}
			if(fhResults != null && fhResults.size() != 0) {
				UploadEntity<FHResult> uploadFhs = new UploadEntity<>("test", "test", fhResults);
				mService.uploadFetalHeart(uploadFhs);
			}
		}else {
			Map<Integer, IUploadEntity> uploadEntities = params[0];
			UploadEntity<BPResult> uploadBps = (UploadEntity<BPResult>) uploadEntities.get(UploadEntity.TYPE_BP);
			if(uploadBps != null) {
				mService.uploadBloodpressure(uploadBps);
			}
			UploadEntity<BSResult> uploadBss = (UploadEntity<BSResult>) uploadEntities.get(UploadEntity.TYPE_BS);
			if(uploadBss != null) {
				mService.uploadBloodGlucose(uploadBss);
			}
			UploadEntity<FHResult> uploadFhs = (UploadEntity<FHResult>) uploadEntities.get(UploadEntity.TYPE_FH);
			if(uploadFhs != null) {
				mService.uploadFetalHeart(uploadFhs);
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ResponseData result) {
		super.onPostExecute(result);
	}
	

}

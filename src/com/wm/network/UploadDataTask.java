package com.wm.network;

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
		Map<Integer, IUploadEntity> uploadEntities = params[0];
		UploadEntity<BPResult> uploadBps = (UploadEntity<BPResult>) uploadEntities.get(UploadEntity.TYPE_BP);
		try {
			if(uploadBps != null) {
				ResponseData data = mService.uploadBloodpressure(uploadBps);
				if(data.code == 0) {
					mDbManager.changeBpStatus(uploadBps.requestDatas);
					mDbManager.deleteBpDatas();//上传成功之后保留最近300表记录
				}
					
			}
			UploadEntity<BSResult> uploadBss = (UploadEntity<BSResult>) uploadEntities.get(UploadEntity.TYPE_BS);
			if(uploadBss != null) {
				ResponseData data = mService.uploadBloodGlucose(uploadBss);
				if(data.code == 0){
					mDbManager.changeBsSate(uploadBss.requestDatas);
					mDbManager.deleteBsDatas();//上传成功之后保留最近300表记录
				}
					
			}
			UploadEntity<FHResult> uploadFhs = (UploadEntity<FHResult>) uploadEntities.get(UploadEntity.TYPE_FH);
			if(uploadFhs != null) {
				ResponseData data = mService.uploadFetalHeart(uploadFhs);
				if(data.code == 0) {
					mDbManager.changeFhState(uploadFhs.requestDatas);
					mDbManager.deleteFhDatas();//上传成功之后保留最近300表记录
				}
					
			}
		} catch(Exception e) {
			
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ResponseData result) {
		super.onPostExecute(result);
	}

}

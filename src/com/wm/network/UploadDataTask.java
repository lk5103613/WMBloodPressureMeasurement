package com.wm.network;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.wm.db.HistoryDBManager;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;
import com.wm.entity.IUploadEntity;
import com.wm.entity.Response;
import com.wm.entity.RequestEntity;

public class UploadDataTask extends
		AsyncTask<Map<Integer, IUploadEntity>, Integer, Response> {
	private Context mContext;
	private HistoryDBManager mDbManager;
	private UploadService mService;

	public UploadDataTask(Context context) {
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
	protected Response doInBackground(Map<Integer, IUploadEntity>... params) {
		Map<Integer, IUploadEntity> uploadEntities = params[0];
		RequestEntity<BPResult> uploadBps = (RequestEntity<BPResult>) uploadEntities
				.get(RequestEntity.TYPE_BP);
		try {
			if (uploadBps != null) {
				Response data = mService.uploadBloodpressure(uploadBps);
				if (data.code == 0) {
					mDbManager.changeBpStatus(uploadBps.requestDatas);
					mDbManager.deleteBpDatas();// 上传成功之后保留最近300表记录
				}

			}
			RequestEntity<BSResult> uploadBss = (RequestEntity<BSResult>) uploadEntities
					.get(RequestEntity.TYPE_BS);
			if (uploadBss != null) {
				Response data = mService.uploadBloodGlucose(uploadBss);
				if (data.code == 0) {
					mDbManager.changeBsSate(uploadBss.requestDatas);
					mDbManager.deleteBsDatas();// 上传成功之后保留最近300表记录
				}

			}
			RequestEntity<FHResult> uploadFhs = (RequestEntity<FHResult>) uploadEntities
					.get(RequestEntity.TYPE_FH);
			if (uploadFhs != null) {
				Response data = mService.uploadFetalHeart(uploadFhs);
				if (data.code == 0) {
					mDbManager.changeFhState(uploadFhs.requestDatas);
					mDbManager.deleteFhDatas();// 上传成功之后保留最近300表记录
				}

			}
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Response result) {
		super.onPostExecute(result);
	}

}

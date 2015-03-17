package com.wm.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.wm.db.HistoryDBManager;
import com.wm.db.DeviceDataContract.FHDataEntry;
import com.wm.entity.BPResult;
import com.wm.entity.BSResult;
import com.wm.entity.FHResult;

public class UploadDataTask extends AsyncTask<String, Integer, String>{
	private Context mContext;
	private HistoryDBManager mDbManager;
	
	public UploadDataTask(Context context){
		this.mContext = context;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDbManager = HistoryDBManager.getInstance(mContext);
	}

	@Override
	protected String doInBackground(String... params) {
		List<BPResult> bpResults = mDbManager.getBpResultsByStatus(0);
		List<BSResult> bsResults = mDbManager.getBsResultsByStatus(0);
		List<FHResult> fhResults = mDbManager.getFhResultsByStatus(0);
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	

}

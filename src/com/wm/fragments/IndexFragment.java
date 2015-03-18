package com.wm.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wm.activity.LoginActivity;
import com.wm.activity.R;
import com.wm.entity.BPResult;
import com.wm.entity.ResponseData;
import com.wm.entity.UploadEntity;
import com.wm.network.NetworkFactory;
import com.wm.network.UploadService;

public class IndexFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);// ÏÔÊ¾fragmentµÄmenu
		View view = inflater.inflate(R.layout.fragment_index, container, false);
		ButterKnife.inject(this, view);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		 super.onCreateOptionsMenu(menu, inflater);
         menu.add("µÇÂ¼").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
	
	@OnClick(R.id.btn_test_upload)
	public void upload(View v) {
		new AsyncTask<Void, Void, ResponseData>() {

			@Override
			protected ResponseData doInBackground(Void... params) {
				UploadService service = NetworkFactory.getUploadService();
				if(service == null) {
					System.out.println("service is null");
					return null;
				}
				UploadEntity<BPResult> uploadEntity = new UploadEntity<BPResult>();
				uploadEntity.callerName = "test";
				uploadEntity.password = "test";
				List<BPResult> bpResults = new ArrayList<BPResult>();
				bpResults.add(new BPResult("123",12,12,12,"2011-1-1","123"));
				uploadEntity.requestDatas = bpResults;
				ResponseData responseData = null;
				try{
					responseData = service.uploadBloodpressure(uploadEntity);
				} catch(Exception e) {
					System.out.println("upload blood pressure failed");
					return null;
				}
				return responseData;
			}
			
			@Override
			protected void onPostExecute(ResponseData result) {
				super.onPostExecute(result);
				if(result == null) 
					return;
				System.out.println("over " + result.info + "   " + result.code);
			}
		}.execute();
	}

	
}

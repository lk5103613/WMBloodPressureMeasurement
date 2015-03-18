package com.wm.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class NetworkFactory {
	
	private static UploadService mUploadService;
	private static Gson mGson;
	
	public static UploadService getUploadService() {
		if(mGson == null) {
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		}
		if(mUploadService == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint("http://www.lichkin.com:38050/V0100")
				.setConverter(new GsonConverter(mGson))
				.build();
			mUploadService = restAdapter.create(UploadService.class);
		}
		return mUploadService;
		
	}
	
}

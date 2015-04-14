package com.wm.network;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

public class NetworkFactory {
	
	private static UploadService mUploadService;
	private static AuthService mAuthService;
	private static Gson mGson;
	
	public static UploadService getUploadService() {
		if(mGson == null) {
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		}
		if(mUploadService == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint("http://120.26.91.90:38050/V0100")
				.setClient(new OkClient(new OkHttpClient()))
				.setConverter(new GsonConverter(mGson))
				.build();
			mUploadService = restAdapter.create(UploadService.class);
		}
		return mUploadService;
	}
	
	public static AuthService getAuthService() {
		if(mGson == null) {
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		}
		if(mAuthService == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint("http://120.26.91.90:38050/V0100")
				.setClient(new OkClient(new OkHttpClient()))
				.setConverter(new GsonConverter(mGson))
				.build();
			mAuthService = restAdapter.create(AuthService.class);
		}
		return mAuthService;
	}
	
}

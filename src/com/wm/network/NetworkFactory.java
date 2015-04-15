package com.wm.network;

import retrofit.RequestInterceptor;
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
	private static RequestInterceptor mInterceptor;
	private static RestAdapter mRestAdapter;

	public static UploadService getUploadService() {
		if (mGson == null) {
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		}
		if (mInterceptor == null) {
			mInterceptor = new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {
					request.addHeader("Accept-Charset", "UTF-8");
				}
			};
		}
		if (mUploadService == null) {
			if (mRestAdapter == null) {
				mRestAdapter = new RestAdapter.Builder()
						.setLogLevel(RestAdapter.LogLevel.FULL)
						.setEndpoint("http://120.26.91.90:38050/V0100")
						.setClient(new OkClient(new OkHttpClient()))
						.setConverter(new GsonConverter(mGson))
						//.setRequestInterceptor(mInterceptor)
						.build();
			}
			mUploadService = mRestAdapter.create(UploadService.class);
		}
		return mUploadService;
	}

	public static AuthService getAuthService() {
		if (mGson == null) {
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		}
		if (mInterceptor == null) {
			mInterceptor = new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {
					request.addHeader("Accept-Charset", "UTF-8");
				}
			};
		}
		if (mAuthService == null) {
			if (mRestAdapter == null) {
				mRestAdapter = new RestAdapter.Builder()
						.setLogLevel(RestAdapter.LogLevel.FULL)
						.setEndpoint("http://120.26.91.90:38050/V0100")
						.setClient(new OkClient(new OkHttpClient()))
						.setConverter(new GsonConverter(mGson))
						//.setRequestInterceptor(mInterceptor)
						.build();
			}
			mAuthService = mRestAdapter.create(AuthService.class);
		}
		return mAuthService;
	}

}

package com.wm.network;

import retrofit.http.Body;
import retrofit.http.POST;

import com.wm.entity.LoginEntity;
import com.wm.entity.RegisterEntity;
import com.wm.entity.Response;
import com.wm.entity.RequestEntity;

public interface AuthService {
	
	@POST("/register/2J.do")
	Response register(@Body RequestEntity<RegisterEntity> registerEntity);
	
	@POST("/login/2J.do")
	Response login(@Body RequestEntity<LoginEntity> registerEntity);

}

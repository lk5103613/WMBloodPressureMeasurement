package com.wm.entity;

import java.util.List;

import com.google.gson.annotations.Expose;

public class UploadEntity<T> {
	
	@Expose public String callerName;
	@Expose public String password;
	@Expose public List<T> requestDatas;

}

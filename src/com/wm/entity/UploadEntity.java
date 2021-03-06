package com.wm.entity;

import java.util.List;

import com.google.gson.annotations.Expose;

public class UploadEntity<T> implements IUploadEntity {
	
	public final static int TYPE_BP = 0;
	public final static int TYPE_BS = 1;
	public final static int TYPE_FH = 2;
	
	@Expose public String callerName;
	@Expose public String password;
	@Expose public List<T> requestDatas;
	
	public UploadEntity() {
		super();
	}
	public UploadEntity(String callerName, String password, List<T> requestDatas) {
		super();
		this.callerName = callerName;
		this.password = password;
		this.requestDatas = requestDatas;
	}

}

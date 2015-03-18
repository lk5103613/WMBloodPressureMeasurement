package com.wm.entity;

import java.util.List;

import com.google.gson.annotations.Expose;

public class ResponseData {
	
	@Expose public List<String> datas;
	@Expose public String backUrl;
	@Expose public String requestTime;
	@Expose public String requestId;
	@Expose public int code;
	@Expose public String responseTime;
	@Expose public String info;
	
}

package com.wm.entity;

import com.google.gson.annotations.Expose;

public class BSResult {

	public int id;
	@Expose public String userCard;
	@Expose public int bg;
	@Expose public long measureTime;
	@Expose public String remarks;
	public int status;
	
	public BSResult(int bsValue, long date) {
		super();
		this.bg = bsValue;
		this.measureTime = date;
	}

	public BSResult(int id, int bsValue, long date) {
		this(bsValue, date);
		this.id = id;
	}

	public BSResult(String userCard, int bg, long measureTime, String remarks) {
		super();
		this.userCard = userCard;
		this.bg = bg;
		this.measureTime = measureTime;
		this.remarks = remarks;
	}
	
}

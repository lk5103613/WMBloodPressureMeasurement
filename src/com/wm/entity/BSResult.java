package com.wm.entity;

import com.google.gson.annotations.Expose;

public class BSResult {

	public int id;
	@Expose public String userCard;
	@Expose public int bg;
	public long date;
	@Expose public String measureTime;
	@Expose public String remarks;
	public int status;
	
	public BSResult(int bsValue, long date) {
		super();
		this.bg = bsValue;
		this.date = date;
	}

	public BSResult(int id, int bsValue, long date) {
		this(bsValue, date);
		this.id = id;
	}

	public BSResult(String userCard, int bg, long measureTime, String remarks) {
		super();
		this.userCard = userCard;
		this.bg = bg;
		this.date = measureTime;
		this.remarks = remarks;
	}
	
}

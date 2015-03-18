package com.wm.entity;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

public class FHResult {
	
	public int id;
	@Expose public String userCard;
	public List<Float> fhValues;
	@Expose public String fh;
	@Expose public long measureTime;
	@Expose public String remarks;
	public int status;
	
	public FHResult(){}
	
	public FHResult(List<Float> fhValues) {
		this.fhValues = fhValues;
		this.measureTime = new Date().getTime();
	}
	
	public FHResult(List<Float> fhValues, long date){
		this.fhValues = fhValues;
		this.measureTime = date;
	}
	
	public FHResult(int id, List<Float> fhValues, long date) {
		this(fhValues, date);
		this.id = id;
	}
	
	
}

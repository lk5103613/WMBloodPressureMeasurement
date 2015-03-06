package com.wm.entity;

import java.util.List;

public class FHResult {
	private List<Float> fhValues;
	private Long date;
	
	public FHResult(){}
	public FHResult(List<Float> fhValues, Long date){
		this.fhValues = fhValues;
		this.date = date;
	}
	public List<Float> getFhValues() {
		return fhValues;
	}
	public void setFhValues(List<Float> fhValues) {
		this.fhValues = fhValues;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	
	

}

package com.wm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.wm.utils.DateUtil;

public class FHResult {
	
	public int id;
	@Expose public String userCard;
	public List<Integer> fhValues;
	@Expose public String fh;
	public long date;
	@Expose public String measureTime;
	@Expose public String remarks;
	public int status;
	
	public FHResult(){}
	
	public FHResult(List<Integer> fhValues) {
		this.fhValues = fhValues;
		this.date = new Date().getTime();
	}
	
	public FHResult(List<Integer> fhValues, long date){
		this.userCard = "330310198611010909";
		this.remarks = "test";
		this.fhValues = fhValues;
		this.date = date;
	}
	
	public FHResult(int id,String userCard, String fh, long date, String remarks) {
		this.id = id;
		this.userCard = userCard;
		this.fh = fh;
		this.date = date;
		this.measureTime = DateUtil.getFormatDate(DateUtil.DATA_FORMAT, date);
		this.fhValues = splitFhValues(fh);
		this.remarks = remarks;
	}
	
	
	/**
	 * ̥���ַ����ŷ�Ϊlist
	 * 
	 * @param valueStr
	 * @return List
	 */
	private List<Integer> splitFhValues(String valueStr){
		List<Integer> fhList = new ArrayList<>();
		if (!"".equals(fhList)){
			String[] fhArray = valueStr.split(",");
			for (int i = 0; i < fhArray.length; i++) {
				fhList.add(Integer.parseInt(fhArray[i]));
			}
		}
		return fhList;
	}
	
}

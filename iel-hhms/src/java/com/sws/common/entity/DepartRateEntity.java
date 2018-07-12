package com.sws.common.entity;

import java.util.ArrayList;
import java.util.List;

public class DepartRateEntity {
	private String time;
	private List<String> nameList = new ArrayList<String>();
	private List<String> rateList = new ArrayList<String>();
	private List<Integer> rightCountList = new ArrayList<Integer>();
	private List<Integer> wrongCountList = new ArrayList<Integer>();
	
	public String getTime() {
		return time;
	}
	public List<String> getNameList() {
		return nameList;
	}
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<String> getRateList() {
		return rateList;
	}
	public void setRateList(List<String> rateList) {
		this.rateList = rateList;
	}
	public List<Integer> getRightCountList() {
		return rightCountList;
	}
	public void setRightCountList(List<Integer> rightCountList) {
		this.rightCountList = rightCountList;
	}
	public List<Integer> getWrongCountList() {
		return wrongCountList;
	}
	public void setWrongCountList(List<Integer> wrongCountList) {
		this.wrongCountList = wrongCountList;
	}
	
	
	
}
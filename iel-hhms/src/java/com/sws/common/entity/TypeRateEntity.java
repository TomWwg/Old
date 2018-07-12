package com.sws.common.entity;

import java.util.ArrayList;
import java.util.List;

public class TypeRateEntity {
	private String depart;
	private String rate;
	private Integer rightCount;
	private Integer wrongCount;
	private List<String> nameList = new ArrayList<String>();
	private List<String> rateList = new ArrayList<String>();
	private List<Integer> staffCountList = new ArrayList<Integer>();//人数
	private List<Integer> rightCountList = new ArrayList<Integer>();
	private List<Integer> wrongCountList = new ArrayList<Integer>();
	
	//add by yxnne 5-9-2017
	//为了计算和封装部门的两前两后数据
	private double rateBeforeCloseNick;//接触病人前
	private double rateBeforeAsepticOperation;//无菌操作前
	private double rateAfterCloseNick;//接触病人后
	private double rateAfterCloseNickEnvri;//接触病人环境后
	
	public TypeRateEntity(){
		rightCount=0;
		wrongCount=0;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getRightCount() {
		return rightCount;
	}
	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}
	public Integer getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}
	public List<String> getNameList() {
		return nameList;
	}
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public List<String> getRateList() {
		return rateList;
	}
	public void setRateList(List<String> rateList) {
		this.rateList = rateList;
	}
	public List<Integer> getStaffCountList() {
		return staffCountList;
	}
	public void setStaffCountList(List<Integer> staffCountList) {
		this.staffCountList = staffCountList;
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
	public double getRateBeforeCloseNick() {
		return rateBeforeCloseNick;
	}
	public void setRateBeforeCloseNick(double rateBeforeCloseNick) {
		this.rateBeforeCloseNick = rateBeforeCloseNick;
	}
	public double getRateBeforeAsepticOperation() {
		return rateBeforeAsepticOperation;
	}
	public void setRateBeforeAsepticOperation(double rateBeforeAsepticOperation) {
		this.rateBeforeAsepticOperation = rateBeforeAsepticOperation;
	}
	public double getRateAfterCloseNick() {
		return rateAfterCloseNick;
	}
	public void setRateAfterCloseNick(double rateAfterCloseNick) {
		this.rateAfterCloseNick = rateAfterCloseNick;
	}
	public double getRateAfterCloseNickEnvri() {
		return rateAfterCloseNickEnvri;
	}
	public void setRateAfterCloseNickEnvri(double rateAfterCloseNickEnvri) {
		this.rateAfterCloseNickEnvri = rateAfterCloseNickEnvri;
	}
	
}

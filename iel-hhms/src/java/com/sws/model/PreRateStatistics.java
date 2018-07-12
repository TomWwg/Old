package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class PreRateStatistics extends  BaseEntity {
	/**
	 *  用于存储一天手卫生情况功能ByRfid
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String rfid; //胸牌编号  
	private Integer  recordType;//记录类型
	private String timeStr; //记录时间
	private Integer  effectWash;//洗手次数
	private Integer inRoom; //进入病区未洗手
	private Integer outRoom;//离开病区未洗手
	private Integer beforeInBed;//接近病床前未洗手
	private Integer longOutBed;//长时离开病床未洗手
	private String rate;//依从率
	public PreRateStatistics(){
		effectWash=0;
		inRoom=0;
		outRoom=0;
		beforeInBed=0;
		longOutBed=0;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public Integer getEffectWash() {
		return effectWash;
	}
	public void setEffectWash(Integer effectWash) {
		this.effectWash = effectWash;
	}
	public Integer getInRoom() {
		return inRoom;
	}
	public void setInRoom(Integer inRoom) {
		this.inRoom = inRoom;
	}
	public Integer getOutRoom() {
		return outRoom;
	}
	public void setOutRoom(Integer outRoom) {
		this.outRoom = outRoom;
	}
	public Integer getBeforeInBed() {
		return beforeInBed;
	}
	public void setBeforeInBed(Integer beforeInBed) {
		this.beforeInBed = beforeInBed;
	}
	public Integer getLongOutBed() {
		return longOutBed;
	}
	public void setLongOutBed(Integer longOutBed) {
		this.longOutBed = longOutBed;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	
	
	  
}

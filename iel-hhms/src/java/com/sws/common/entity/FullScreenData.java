package com.sws.common.entity;

public class FullScreenData {
	private Integer  effectWash;//洗手次数
	private Integer inRoom; //进入病区未洗手
	private Integer outRoom;//离开病区未洗手
	private Integer beforeInBed;//接近病床前未洗手
	private Integer longOutBed;//长时离开病床未洗手
	public FullScreenData(){
		effectWash=0;
		inRoom=0;
		outRoom=0;
		beforeInBed=0;
		longOutBed=0;
	}
	public Integer getEffectWash() {
		return effectWash;
	}
	public void setEffectWash(Integer effectWash) {
		this.effectWash = effectWash;
	}
	public Integer getBeforeInBed() {
		return beforeInBed;
	}
	public void setBeforeInBed(Integer beforeInBed) {
		this.beforeInBed = beforeInBed;
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
	public Integer getLongOutBed() {
		return longOutBed;
	}
	public void setLongOutBed(Integer longOutBed) {
		this.longOutBed = longOutBed;
	}
}

package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;


public class ScheduleTemplate extends  BaseEntity {

	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private Date startHm;
	private Date endHm;
	private String sHm;//开始时分
	private String eHm;//结束时分
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartHm() {
		return startHm;
	}
	public void setStartHm(Date startHm) {
		this.startHm = startHm;
	}
	public Date getEndHm() {
		return endHm;
	}
	public void setEndHm(Date endHm) {
		this.endHm = endHm;
	}
	public String getsHm() {
		return sHm;
	}
	public void setsHm(String sHm) {
		this.sHm = sHm;
	}
	public String geteHm() {
		return eHm;
	}
	public void seteHm(String eHm) {
		this.eHm = eHm;
	}
	
	
}
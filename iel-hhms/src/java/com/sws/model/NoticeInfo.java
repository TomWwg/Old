package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;


public class NoticeInfo extends  BaseEntity {
	/**
	 *  公告信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private String remark; //公告信息
	private Date expireTime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
}

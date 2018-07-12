package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;


public class RoleInfo extends  BaseEntity {
	/**
	 *  角色信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private String remark;
	private Date expireTime;
	private String menuIds;
	private Long id;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	} 
}

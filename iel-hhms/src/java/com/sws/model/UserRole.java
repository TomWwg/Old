package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class UserRole extends  BaseEntity {
	/**
	 *  用户角色信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private UserInfo userInfo;
	private RoleInfo roleInfo;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public RoleInfo getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
	  
}

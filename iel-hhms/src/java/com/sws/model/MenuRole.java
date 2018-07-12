package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class MenuRole extends  BaseEntity {
	/**
	 *  角色信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private RoleInfo roleInfo;
	private MenuInfo menuInfo;
	public RoleInfo getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
	public MenuInfo getMenuInfo() {
		return menuInfo;
	}
	public void setMenuInfo(MenuInfo menuInfo) {
		this.menuInfo = menuInfo;
	}
	
	  
}

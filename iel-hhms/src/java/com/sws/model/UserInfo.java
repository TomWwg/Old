package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;


public class UserInfo extends  BaseEntity {
	/**
	 *  用户信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private String password;
	private String tel;
	private Date expireTime; //过期时间
	private String remark;  //备注
	private GroupTree groupTree;//所属分组
	private Date lastLoginTime;
	private Integer loginCounts = 0;
	private Integer userStatus = 0; //0正常，1，停用，2过期
	private RoleInfo roleInfo;
	private String favoriteIds;
	private String departIds;
	private String rfId;
	private String strRev1;//科室名
	private String strRev2;//角色名
	//显示
	private String expireStr;//过期时间
	
	public String getExpireStr() {
		return expireStr;
	}
	public void setExpireStr(String expireStr) {
		this.expireStr = expireStr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public GroupTree getGroupTree() {
		return groupTree;
	}
	public void setGroupTree(GroupTree groupTree) {
		this.groupTree = groupTree;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Integer getLoginCounts() {
		return loginCounts;
	}
	public void setLoginCounts(Integer loginCounts) {
		this.loginCounts = loginCounts;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public String getStrRev1() {
		return strRev1;
	}
	public void setStrRev1(String strRev1) {
		this.strRev1 = strRev1;
	}
	public String getStrRev2() {
		return strRev2;
	}
	public void setStrRev2(String strRev2) {
		this.strRev2 = strRev2;
	}
	public RoleInfo getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
	public String getFavoriteIds() {
		return favoriteIds;
	}
	public void setFavoriteIds(String favoriteIds) {
		this.favoriteIds = favoriteIds;
	}
	public String getDepartIds() {
		return departIds;
	}
	public void setDepartIds(String departIds) {
		this.departIds = departIds;
	}
	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
	}
	  
}
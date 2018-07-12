package com.sws.bo;

import java.util.Date;


public class UserBo extends BaseBo {

	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机
	 */
	private String phoneNo;
	/**
	 * 状态名称
	 */
	private Integer status = 0;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 备注
	 */
	private String description;
	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 登录次数
	 */
	private Integer loginCounts = 0;
	
	/**
	 * IP
	 */
	private String ip;
	
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public Date getExpireTime() {
		return expireTime;
	}
	
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	
	
	
	
	
}

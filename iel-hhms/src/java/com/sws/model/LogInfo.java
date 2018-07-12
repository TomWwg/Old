package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class LogInfo extends  BaseEntity {
	/**
	 *  角色信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private UserInfo userInfo;
	private Integer type;//1:用户注册;2:用户登陆;3:设备添加;4:用户登出
	private String remark; //日志内容
	private String optUser;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOptUser() {
		return optUser;
	}
	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}
	
}

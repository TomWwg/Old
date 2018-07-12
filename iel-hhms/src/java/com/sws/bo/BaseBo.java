package com.sws.bo;

import java.util.Date;


public class BaseBo {
	/**
	 * id
	 */
	private Integer id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 创建者
	 */
	private String operator;
	/**
	 * 保留字段1
	 */
	private String strRev1;
	/**
	 * 保留字段2
	 */
	private String strRev2;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
	
	
}

package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class GroupTree extends  BaseEntity {
	/**
	 *  组织结构树
	 */
	private static final long serialVersionUID = 1;
	private String name;
	private Long parentId;	//父亲组织的ID
	private Integer type;//0科室，1病区，2病床
	private String remark;
	private String addType;
	private String deviceType;
	private Integer count;
	private Integer departLevel;
	
	public Integer getDepartLevel() {
		return departLevel;
	}
	public void setDepartLevel(Integer departLevel) {
		this.departLevel = departLevel;
	}
	public String getAddType() {
		return addType;
	}
	public void setAddType(String addType) {
		this.addType = addType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "GroupTree [name=" + name + ", parentId=" + parentId + ", type="
				+ type + ", remark=" + remark + ", addType=" + addType
				+ ", deviceType=" + deviceType + ", count=" + count
				+ ", departLevel=" + departLevel + "]";
	}
	  
}

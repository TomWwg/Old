package com.sws.common.entity;

public class GroupTree {
	
	private static final long serialVersionUID = 1;
	private String name;
	private Long parentId;	//父亲组织的ID
	private Integer type;//0科室，1病区，2病床
	private String remark;
	private String addType;
	private String deviceType;
	private Integer count;
	private Integer departLevel;
	private String parentName;
	
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
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public Integer getDepartLevel() {
		return departLevel;
	}
	
	public void setDepartLevel(Integer departLevel) {
		this.departLevel = departLevel;
	}
	
	public String getParentName() {
		return parentName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "GroupTree [name=" + name + ", parentId=" + parentId + ", type="
				+ type + ", remark=" + remark + ", addType=" + addType
				+ ", deviceType=" + deviceType + ", count=" + count
				+ ", departLevel=" + departLevel + ", parentName=" + parentName
				+ "]";
	}
	public GroupTree(){}

	public GroupTree(com.sws.model.GroupTree a) {
		super();
		this.name = a.getName();
		this.parentId = a.getParentId();
		this.type = a.getType();
		this.remark = a.getRemark();
		this.addType = a.getAddType();
		this.deviceType = a.getDeviceType();
		this.count = a.getCount();
		this.departLevel = a.getDepartLevel();
		//this.parentName = a.getRemark();
	}

}

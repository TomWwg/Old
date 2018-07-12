package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class DeviceInfo extends  BaseEntity {
	/**
	 *  设备信息
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String name;
	private String no;
	private String type;
	private Integer deviceStatus; //0正常 1未启用 2欠压 3通讯故障
	private String remark;
	private GroupTree groupTree;//所属分组
	private Long departId;
	private Long roomId;
	private Long bedId;
	private Long staffId;//胸牌时，才用到
	private String deviceStatusName;
	private String showName;
	private String typeName;
	public Long getDepartId() {
		return departId;
	}
	public void setDepartId(Long departId) {
		this.departId = departId;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getBedId() {
		return bedId;
	}
	public void setBedId(Long bedId) {
		this.bedId = bedId;
	}
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public String getDeviceStatusName() {
		return deviceStatusName;
	}
	public void setDeviceStatusName(String deviciStatusName) {
		this.deviceStatusName = deviciStatusName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	  
}

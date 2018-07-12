package com.sws.model;

import java.util.Date;

import com.gk.extend.hibernate.entity.BaseEntity;


public class WashHandLog extends  BaseEntity {
	

	/**
	 *  手卫生记录表
	 */
	private static final long serialVersionUID = 1844345216569746917L;
	private String eventType;
	private String apNo; //AP编码
	private String deviceNo; //识别器编号
	private String deviceType; //识别器类型
	private String rfid; //胸牌编号
	private Integer rfidStatus;//0无定义；1不清洁；2有限清洁；3清洁；4短时接近病床；5长时接近病床；6进入病区未洗手；7离开病区未洗手
	private String content;//信号内容
	//mq补充添加
	private String timeStr; //识别器类型
	private String type; //消息类型
	private String ip;
	private String washHandMomentName;
	private String staffTypeName;
	private String deviceInfo;
	private String location;
	private String sex;
	
	/*
	 * 添加上传时间
	 * wwg
	 */
	private Date updateTime;
	
	/*
	 * 添加创建时间
	 */
	private Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/*
	 * 返回前端需要String类型的时间
	 */
	private String updateTimeString;
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTimeString() {
		return updateTimeString;
	}

	public void setUpdateTimeString(String updateTimeString) {
		this.updateTimeString = updateTimeString;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getWashHandMomentName() {
		return washHandMomentName;
	}
	public void setWashHandMomentName(String washHandMomentName) {
		this.washHandMomentName = washHandMomentName;
	}
	public String getStaffTypeName() {
		return staffTypeName;
	}
	public void setStaffTypeName(String staffTypeName) {
		this.staffTypeName = staffTypeName;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	//暂时没用
	private String postCode;//邮编
	private String hospitalNo;//医院编号
	private String departmentNo;//科室编号
	
	private String docName;
	private String departName;
	private String eventTypeName;
	private String rfidStatusName;
	private String deviceTypeName;//识别器类型名称
	private String remark;//信号内容
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	public String getRfidStatusName() {
		return rfidStatusName;
	}
	public void setRfidStatusName(String rfidStatusName) {
		this.rfidStatusName = rfidStatusName;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getApNo() {
		return apNo;
	}
	public void setApNo(String apNo) {
		this.apNo = apNo;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public Integer getRfidStatus() {
		return rfidStatus;
	}
	public void setRfidStatus(Integer rfidStatus) {
		this.rfidStatus = rfidStatus;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	public String getDepartmentNo() {
		return departmentNo;
	}
	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}
    public String getTimeStr() {
        return timeStr;
    }
    
    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Override
	public String toString() {
		return "WashHandLog [eventType=" + eventType + ", apNo=" + apNo
				+ ", deviceNo=" + deviceNo + ", deviceType=" + deviceType
				+ ", rfid=" + rfid + ", rfidStatus=" + rfidStatus
				+ ", content=" + content + ", timeStr=" + timeStr + ", type="
				+ type + ", ip=" + ip + ", washHandMomentName="
				+ washHandMomentName + ", staffTypeName=" + staffTypeName
				+ ", deviceInfo=" + deviceInfo + ", location=" + location
				+ ", sex=" + sex + ", updateTime=" + updateTime
				+ ", createTime=" + createTime + ", updateTimeString="
				+ updateTimeString + ", postCode=" + postCode + ", hospitalNo="
				+ hospitalNo + ", departmentNo=" + departmentNo + ", docName="
				+ docName + ", departName=" + departName + ", eventTypeName="
				+ eventTypeName + ", rfidStatusName=" + rfidStatusName
				+ ", deviceTypeName=" + deviceTypeName + ", remark=" + remark
				+ "]";
	}

}

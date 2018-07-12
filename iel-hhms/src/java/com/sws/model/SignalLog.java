package com.sws.model;

import com.gk.extend.hibernate.entity.BaseEntity;


public class SignalLog extends  BaseEntity {
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
	//暂时没用
	private String postCode;//邮编
	private String hospitalNo;//医院编号
	private String departmentNo;//科室编号
	
	private String docName;
	private String departName;
	private String eventTypeName;
	private String rfidStatusName;
	private String deviceTypeName;
	
	
	
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getRfidStatusName() {
		return rfidStatusName;
	}
	public void setRfidStatusName(String rfidStatusName) {
		this.rfidStatusName = rfidStatusName;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	  
}

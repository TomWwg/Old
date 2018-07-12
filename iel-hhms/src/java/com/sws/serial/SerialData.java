package com.sws.serial;

public class SerialData {
	private String com;
	private String baudRate;
	private String checkBit;
	private String dataBit;
	private String stopBit;
	private String deviceType;
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getBaudRate() {
		return baudRate;
	}
	public void setBaudRate(String baudRate) {
		this.baudRate = baudRate;
	}
	public String getCheckBit() {
		return checkBit;
	}
	public void setCheckBit(String checkBit) {
		this.checkBit = checkBit;
	}
	public String getDataBit() {
		return dataBit;
	}
	public void setDataBit(String dataBit) {
		this.dataBit = dataBit;
	}
	public String getStopBit() {
		return stopBit;
	}
	public void setStopBit(String stopBit) {
		this.stopBit = stopBit;
	}
}

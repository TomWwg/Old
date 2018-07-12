package com.sws.common;

import java.io.Serializable;

public class MenuParams implements Serializable {

	/** 序列化ID */
	private static final long serialVersionUID = 2406621497278704361L;
	// 模块控制
	private boolean enabledDeviceManger;
	private boolean enabledAlarmLog;
	private boolean enabledUserManager;
	private boolean enabledFriendManager;
	private boolean enabledOperationLog;
	private boolean enabledSystemManager;
	
	
	public boolean isEnabledDeviceManger() {
		return enabledDeviceManger;
	}
	public void setEnabledDeviceManger(boolean enabledDeviceManger) {
		this.enabledDeviceManger = enabledDeviceManger;
	}
	
	public boolean isEnabledAlarmLog() {
		return enabledAlarmLog;
	}
	public void setEnabledAlarmLog(boolean enabledAlarmLog) {
		this.enabledAlarmLog = enabledAlarmLog;
	}
	
	public boolean isEnabledUserManager() {
		return enabledUserManager;
	}
	public void setEnabledUserManager(boolean enabledUserManager) {
		this.enabledUserManager = enabledUserManager;
	}
	
	public boolean isEnabledFriendManager() {
		return enabledFriendManager;
	}
	public void setEnabledFriendManager(boolean enabledFriendManager) {
		this.enabledFriendManager = enabledFriendManager;
	}
	
	public boolean isEnabledOperationLog() {
		return enabledOperationLog;
	}
	public void setEnabledOperationLog(boolean enabledOperationLog) {
		this.enabledOperationLog = enabledOperationLog;
	}
	
	public boolean isEnabledSystemManager() {
		return enabledSystemManager;
	}
	public void setEnabledSystemManager(boolean enabledSystemManager) {
		this.enabledSystemManager = enabledSystemManager;
	}
	

}

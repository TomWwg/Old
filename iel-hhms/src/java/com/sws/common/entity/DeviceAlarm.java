package com.sws.common.entity;
/** 
 * @author WangYabei 2015-11-22 下午8:18:22 
 * @version V1.0
 * @Edit {修改人} 2015-11-22
 */
public class DeviceAlarm {
	private String name;//设备名称
	private Integer normal = 0; //正常
	private Integer error = 0;   //疑似故障
	private String rate;  //比例
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNormal() {
		return normal;
	}
	public void setNormal(Integer normal) {
		this.normal = normal;
	}
	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
}

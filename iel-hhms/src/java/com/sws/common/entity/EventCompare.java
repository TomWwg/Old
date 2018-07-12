package com.sws.common.entity;
/** 
 * @author WangYabei 2015-11-22 下午8:18:22 
 * @version V1.0
 * @Edit {修改人} 2015-11-22
 */
public class EventCompare {
	private String name;//科室名称
	private Integer evenBefore = 0; //接触前次数
	private Integer evenAfter = 0;   //接触后次数
	private String rate;  //比例
	private String type; 
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public EventCompare(){
		evenBefore=0;
		evenAfter=0;
	}
	
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getEvenBefore() {
		return evenBefore;
	}
	public void setEvenBefore(Integer evenBefore) {
		this.evenBefore = evenBefore;
	}
	public Integer getEvenAfter() {
		return evenAfter;
	}
	public void setEvenAfter(Integer evenAfter) {
		this.evenAfter = evenAfter;
	}
	
}

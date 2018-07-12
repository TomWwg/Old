package com.sws.common.entity;
/** 
 * @author WangYabei 2015-10-6 下午12:19:25 
 * @version V1.0
 * @Edit {修改人} 2015-10-6
 */
public class StaffEntity {
	private String name;
	private String rate; 
	private Integer rightCount = 0;//类别1
	private Integer wrongCount = 0;
	private Integer perpleNum = 0;
	private String type;
	public StaffEntity(){
		rightCount=0;
		wrongCount=0;
		perpleNum=0;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getRightCount() {
		return rightCount;
	}
	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}
	public Integer getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}
	public Integer getPerpleNum() {
		return perpleNum;
	}
	public void setPerpleNum(Integer perpleNum) {
		this.perpleNum = perpleNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "StaffEntity [name=" + name + ", rate=" + rate + ", rightCount="
				+ rightCount + ", wrongCount=" + wrongCount + ", perpleNum="
				+ perpleNum + ", type=" + type + "]";
	}
}

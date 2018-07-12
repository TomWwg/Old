package com.sws.common.entity;
/** 
 * @author yxnne 2017-06-06  
 * @version V1.0
 * 针对部门洗手时机的实体
 */
public class WashHandMoment {
	private String name;//科室名称
	private Integer evenBefore = 0; //接触前次数
	private Integer evenAfter = 0;   //接触后次数
	
	//洗手时机：洗手次数
	private Integer washBeforeCloseNickTimes = 0;
	private Integer washBeforeAsepticOperationTimes = 0;
	private Integer washAfterCloseNickTimes = 0;
	private Integer washAfterCloseNickEnvriTimes = 0;
	//洗手时机: 未洗手次数
	private Integer notWashBeforeCloseNickTimes = 0;
	private Integer notWashBeforeAsepticOperationTimes = 0;
	private Integer notWashAfterCloseNickTimes = 0;
	private Integer notWashAfterCloseNickEnvriTimes = 0;
	
	private String rate;  //比例
	
	private String rateWashBeforeCloseNick;  
	private String rateWashBeforeAsepticOperation;  
	private String rateWashAfterCloseNick;  
	private String rateWashAfterCloseNickEnvri;  
	
	private String type;
	
	public String getRateWashBeforeCloseNick() {
		return rateWashBeforeCloseNick;
	}
	public void setRateWashBeforeCloseNick(String rateWashBeforeCloseNick) {
		this.rateWashBeforeCloseNick = rateWashBeforeCloseNick;
	}
	public String getRateWashBeforeAsepticOperation() {
		return rateWashBeforeAsepticOperation;
	}
	public void setRateWashBeforeAsepticOperation(
			String rateWashBeforeAsepticOperation) {
		this.rateWashBeforeAsepticOperation = rateWashBeforeAsepticOperation;
	}
	public String getRateWashAfterCloseNick() {
		return rateWashAfterCloseNick;
	}
	public void setRateWashAfterCloseNick(String rateWashAfterCloseNick) {
		this.rateWashAfterCloseNick = rateWashAfterCloseNick;
	}
	public String getRateWashAfterCloseNickEnvri() {
		return rateWashAfterCloseNickEnvri;
	}
	public void setRateWashAfterCloseNickEnvri(String rateWashAfterCloseNickEnvri) {
		this.rateWashAfterCloseNickEnvri = rateWashAfterCloseNickEnvri;
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
	public Integer getWashBeforeCloseNickTimes() {
		return washBeforeCloseNickTimes;
	}
	public void setWashBeforeCloseNickTimes(Integer washBeforeCloseNickTimes) {
		this.washBeforeCloseNickTimes = washBeforeCloseNickTimes;
	}
	public Integer getWashBeforeAsepticOperationTimes() {
		return washBeforeAsepticOperationTimes;
	}
	public void setWashBeforeAsepticOperationTimes(
			Integer washBeforeAsepticOperationTimes) {
		this.washBeforeAsepticOperationTimes = washBeforeAsepticOperationTimes;
	}
	public Integer getWashAfterCloseNickTimes() {
		return washAfterCloseNickTimes;
	}
	public void setWashAfterCloseNickTimes(Integer washAfterCloseNickTimes) {
		this.washAfterCloseNickTimes = washAfterCloseNickTimes;
	}
	public Integer getWashAfterCloseNickEnvriTimes() {
		return washAfterCloseNickEnvriTimes;
	}
	public void setWashAfterCloseNickEnvriTimes(Integer washAfterCloseNickEnvriTimes) {
		this.washAfterCloseNickEnvriTimes = washAfterCloseNickEnvriTimes;
	}
	public Integer getNotWashBeforeCloseNickTimes() {
		return notWashBeforeCloseNickTimes;
	}
	public void setNotWashBeforeCloseNickTimes(Integer notWashBeforeCloseNickTimes) {
		this.notWashBeforeCloseNickTimes = notWashBeforeCloseNickTimes;
	}
	public Integer getNotWashBeforeAsepticOperationTimes() {
		return notWashBeforeAsepticOperationTimes;
	}
	public void setNotWashBeforeAsepticOperationTimes(
			Integer notWashBeforeAsepticOperationTimes) {
		this.notWashBeforeAsepticOperationTimes = notWashBeforeAsepticOperationTimes;
	}
	public Integer getNotWashAfterCloseNickTimes() {
		return notWashAfterCloseNickTimes;
	}
	public void setNotWashAfterCloseNickTimes(Integer notWashAfterCloseNickTimes) {
		this.notWashAfterCloseNickTimes = notWashAfterCloseNickTimes;
	}
	public Integer getNotWashAfterCloseNickEnvriTimes() {
		return notWashAfterCloseNickEnvriTimes;
	}
	public void setNotWashAfterCloseNickEnvriTimes(
			Integer notWashAfterCloseNickEnvriTimes) {
		this.notWashAfterCloseNickEnvriTimes = notWashAfterCloseNickEnvriTimes;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "WashHandMoment [name=" + name + ", evenBefore=" + evenBefore
				+ ", evenAfter=" + evenAfter + ", washBeforeCloseNickTimes="
				+ washBeforeCloseNickTimes
				+ ", washBeforeAsepticOperationTimes="
				+ washBeforeAsepticOperationTimes
				+ ", washAfterCloseNickTimes=" + washAfterCloseNickTimes
				+ ", washAfterCloseNickEnvriTimes="
				+ washAfterCloseNickEnvriTimes
				+ ", notWashBeforeCloseNickTimes="
				+ notWashBeforeCloseNickTimes
				+ ", notWashBeforeAsepticOperationTimes="
				+ notWashBeforeAsepticOperationTimes
				+ ", notWashAfterCloseNickTimes=" + notWashAfterCloseNickTimes
				+ ", notWashAfterCloseNickEnvriTimes="
				+ notWashAfterCloseNickEnvriTimes + ", rate=" + rate
				+ ", rateWashBeforeCloseNick=" + rateWashBeforeCloseNick
				+ ", rateWashBeforeAsepticOperation="
				+ rateWashBeforeAsepticOperation + ", rateWashAfterCloseNick="
				+ rateWashAfterCloseNick + ", rateWashAfterCloseNickEnvri="
				+ rateWashAfterCloseNickEnvri + ", type=" + type + "]";
	}
	
}

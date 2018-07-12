package com.sws.common.entity;

import java.util.HashMap;
import java.util.Map;

/** 
 * @author WangYabei 2015-11-27 上午11:25:08 
 * @version V1.0
 * @Edit {修改人} 2015-11-27
 */
public class StaffWork implements Comparable<StaffWork>{
	private String docName;
	private String departName;//部门名称
	private String docType;//医生类别
	private String rfid;//胸牌编码
	private Integer normalNum = 0;
	private Integer errorNum = 0;
	private String rate;//依从率
	
	//add yxnne 2017/5/3  for 洗手时机
	private Integer num0003 = 0;
	private Integer num0007 = 0;
	private Integer num0103 = 0;
	private Integer num0110 = 0;
	private Integer num0008 = 0;
	//add yxnne 2017/5/3 for Rank in Department
	private Integer rank = 0;
	
	//add by yxnne 2017/5/10
	//为了计算和封装部门的两前两后数据
	private double rateBeforeCloseNick;//接触病人前
	private double rateBeforeAsepticOperation;//无菌操作前
	private double rateAfterCloseNick;//接触病人后
	private double rateAfterCloseNickEnvri;//接触病人环境后
	
	
	
	public double getRateBeforeCloseNick() {
		return rateBeforeCloseNick;
	}
	public void setRateBeforeCloseNick(double rateBeforeCloseNick) {
		this.rateBeforeCloseNick = rateBeforeCloseNick;
	}
	public double getRateBeforeAsepticOperation() {
		return rateBeforeAsepticOperation;
	}
	public void setRateBeforeAsepticOperation(double rateBeforeAsepticOperation) {
		this.rateBeforeAsepticOperation = rateBeforeAsepticOperation;
	}
	public double getRateAfterCloseNick() {
		return rateAfterCloseNick;
	}
	public void setRateAfterCloseNick(double rateAfterCloseNick) {
		this.rateAfterCloseNick = rateAfterCloseNick;
	}
	public double getRateAfterCloseNickEnvri() {
		return rateAfterCloseNickEnvri;
	}
	public void setRateAfterCloseNickEnvri(double rateAfterCloseNickEnvri) {
		this.rateAfterCloseNickEnvri = rateAfterCloseNickEnvri;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public Integer getNormalNum() {
		return normalNum;
	}
	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}
	public Integer getErrorNum() {
		return errorNum;
	}
	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getNum0003() {
		return num0003;
	}
	public void setNum0003(Integer num0003) {
		this.num0003 = num0003;
	}
	public Integer getNum0007() {
		return num0007;
	}
	public void setNum0007(Integer num0007) {
		this.num0007 = num0007;
	}
	public Integer getNum0103() {
		return num0103;
	}
	public void setNum0103(Integer num0103) {
		this.num0103 = num0103;
	}
	public Integer getNum0110() {
		return num0110;
	}
	public void setNum0110(Integer num0110) {
		this.num0110 = num0110;
	}
	public Integer getNum0008() {
		return num0008;
	}
	public void setNum0008(Integer num0008) {
		this.num0008 = num0008;
	}

	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	@Override
	public int compareTo(StaffWork o) {
		double thisRate = (double)this.normalNum/(double)(this.normalNum+this.errorNum);
		double oRate = (double)o.normalNum/(double)(o.normalNum+o.errorNum);
		int res = 0;
		if(thisRate - oRate > 0 ){
			res = -1;
		}
		else if(thisRate - oRate < 0){
			res = 1;
		}
		return res;
	}
	
}

package com.sws.common.entity;

/**
 * 该类于科室人员手卫生次数的返回
 * @author wwg
 *
 */
public class WashTimes {
	
	//人员姓名
	private String docName;
	//手卫生总次数
	private String totalWashTimes;
	//不清洁的次数
	private String notcleanTimes;
	//有限清洁的次数
	private String limitcleanTimes;
	//有效清洁的次数
	private String cleanTimes;
	
	private int rank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getTotalWashTimes() {
		return totalWashTimes;
	}

	public void setTotalWashTimes(String totalWashTimes) {
		this.totalWashTimes = totalWashTimes;
	}

	public String getNotcleanTimes() {
		return notcleanTimes;
	}

	public void setNotcleanTimes(String notcleanTimes) {
		this.notcleanTimes = notcleanTimes;
	}

	public String getLimitcleanTimes() {
		return limitcleanTimes;
	}

	public void setLimitcleanTimes(String limitcleanTimes) {
		this.limitcleanTimes = limitcleanTimes;
	}

	public String getCleanTimes() {
		return cleanTimes;
	}

	public void setCleanTimes(String cleanTimes) {
		this.cleanTimes = cleanTimes;
	}

	@Override
	public String toString() {
		return "WashTimes [docName=" + docName + ", totalWashTimes="
				+ totalWashTimes + ", notcleanTimes=" + notcleanTimes
				+ ", limitcleanTimes=" + limitcleanTimes + ", cleanTimes="
				+ cleanTimes + ", rank=" + rank + "]";
	}



}

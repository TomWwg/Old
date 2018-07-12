package com.sws.common.entity;

import java.util.List;
import java.util.Map;

import com.sws.model.ManualRecord;

/**
 * 人工录入的返回类
 * @author wwg
 *
 */
public class ManualRecordReturn {

	/*
	 * 人工录入的信息（员工的手卫生信息）
	 */
	private List<ManualRecord> manualRecords;
	
	/*
	 * 部门依从率
	 */
	private Map<String, String> rateByDepartMent;
	
	/*
	 * 人员类别依从率
	 */
	private Map<String, String> rateByRole;
	
	/*
	 * 手卫生时机（两前三后的洗手）依从率
	 */
	private Map<String, String> rateByOccassion;
	
	/*
	 * 手卫生时机（两前三后的洗手）正确性
	 */
	private Map<String, String> correctByOccassion;

	public List<ManualRecord> getManualRecords() {
		return manualRecords;
	}

	public void setManualRecords(List<ManualRecord> manualRecords) {
		this.manualRecords = manualRecords;
	}

	public Map<String, String> getRateByDepartMent() {
		return rateByDepartMent;
	}

	public void setRateByDepartMent(Map<String, String> rateByDepartMent) {
		this.rateByDepartMent = rateByDepartMent;
	}

	public Map<String, String> getRateByRole() {
		return rateByRole;
	}

	public void setRateByRole(Map<String, String> rateByRole) {
		this.rateByRole = rateByRole;
	}

	public Map<String, String> getRateByOccassion() {
		return rateByOccassion;
	}

	public void setRateByOccassion(Map<String, String> rateByOccassion) {
		this.rateByOccassion = rateByOccassion;
	}

	public Map<String, String> getCorrectByOccassion() {
		return correctByOccassion;
	}

	public void setCorrectByOccassion(Map<String, String> correctByOccassion) {
		this.correctByOccassion = correctByOccassion;
	}

	@Override
	public String toString() {
		return "ManualRecordReturn [manualRecords=" + manualRecords
				+ ", rateByDepartMent=" + rateByDepartMent + ", rateByRole="
				+ rateByRole + ", rateByOccassion=" + rateByOccassion
				+ ", correctByOccassion=" + correctByOccassion + "]";
	}
	
}

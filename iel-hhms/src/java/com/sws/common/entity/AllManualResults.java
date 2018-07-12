package com.sws.common.entity;

import java.io.Serializable;
import java.util.Map;

public class AllManualResults implements Serializable {
	
	private static final long serialVersionUID = -9188777141825445185L;

	private String departRate;
	
	private String departCorrect;
	
	private String departExecute;
	
	private Map<Short, String> executeMapOccassion;
	
	private Map<Short, String> correctMapOccassion;
	
	private Map<Long, String> staffExecuteRate;
	
	private Map<Long, String> staffCorrectRate;

	public String getDepartRate() {
		return departRate;
	}

	public void setDepartRate(String departRate) {
		this.departRate = departRate;
	}

	public String getDepartCorrect() {
		return departCorrect;
	}

	public void setDepartCorrect(String departCorrect) {
		this.departCorrect = departCorrect;
	}

	public String getDepartExecute() {
		return departExecute;
	}

	public void setDepartExecute(String departExecute) {
		this.departExecute = departExecute;
	}

	public Map<Short, String> getExecuteMapOccassion() {
		return executeMapOccassion;
	}

	public void setExecuteMapOccassion(Map<Short, String> executeMapOccassion) {
		this.executeMapOccassion = executeMapOccassion;
	}

	public Map<Short, String> getCorrectMapOccassion() {
		return correctMapOccassion;
	}

	public void setCorrectMapOccassion(Map<Short, String> correctMapOccassion) {
		this.correctMapOccassion = correctMapOccassion;
	}

	public Map<Long, String> getStaffExecuteRate() {
		return staffExecuteRate;
	}

	public void setStaffExecuteRate(Map<Long, String> staffExecuteRate) {
		this.staffExecuteRate = staffExecuteRate;
	}

	public Map<Long, String> getStaffCorrectRate() {
		return staffCorrectRate;
	}

	public void setStaffCorrectRate(Map<Long, String> staffCorrectRate) {
		this.staffCorrectRate = staffCorrectRate;
	}
	
	public AllManualResults(String departRate, String departCorrect,
			String departExecute, Map<Short, String> executeMapOccassion,
			Map<Short, String> correctMapOccassion,
			Map<Long, String> staffExecuteRate,
			Map<Long, String> staffCorrectRate) {
		super();
		this.departRate = departRate;
		this.departCorrect = departCorrect;
		this.departExecute = departExecute;
		this.executeMapOccassion = executeMapOccassion;
		this.correctMapOccassion = correctMapOccassion;
		this.staffExecuteRate = staffExecuteRate;
		this.staffCorrectRate = staffCorrectRate;
	}

	@Override
	public String toString() {
		return "AllManualResults [departRate=" + departRate
				+ ", departCorrect=" + departCorrect + ", departExecute="
				+ departExecute + ", executeMapOccassion="
				+ executeMapOccassion + ", correctMapOccassion="
				+ correctMapOccassion + ", staffExecuteRate="
				+ staffExecuteRate + ", staffCorrectRate=" + staffCorrectRate
				+ "]";
	}

}

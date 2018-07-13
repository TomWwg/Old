package com.sws.common.entity;

import java.util.List;

public class StaffStatistic {
	
	private String name;
	
	private String role;
	
	private int times;
	
	private int rank;
	
	private String department;
	
	private List<Detail> detail;
	
	public class Detail{
		private String position;
		
		private int times;

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<Detail> getDetail() {
		return detail;
	}

	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "StaffStatistic [name=" + name + ", role=" + role + ", times=" + times + ", rank=" + rank
				+ ", department=" + department + ", detail=" + detail + "]";
	}

}

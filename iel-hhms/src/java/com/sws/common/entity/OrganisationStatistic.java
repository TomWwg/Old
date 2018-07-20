package com.sws.common.entity;

import java.util.List;

public class OrganisationStatistic {
	
	/**
	 * 医院名
	 */
	private String name;
	
	/**
	 * 手卫生总次数
	 */
	private int totalTimes;
	
	/**
	 * 各个人员类别的手卫生次数
	 */
	private List<RoleTimes> roleTimes;
	
	/**
	 * 一周执行次数变化
	 */
	private List<WeekTimes> weekTimes;
	
	/**
	 * 
	 */
	public class RoleTimes{
		
		private String role;
		
		private int times;

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
	}
	
	public class WeekTimes{
		
		private String day;
		
		private int times;

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
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

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}


	public List<RoleTimes> getRoleTimes() {
		return roleTimes;
	}

	public void setRoleTimes(List<RoleTimes> roleTimes) {
		this.roleTimes = roleTimes;
	}


	public List<WeekTimes> getWeekTimes() {
		return weekTimes;
	}

	public void setWeekTimes(List<WeekTimes> weekTimes) {
		this.weekTimes = weekTimes;
	}

	@Override
	public String toString() {
		return "OrganisationStatistic [name=" + name + ", totalTimes=" + totalTimes + ", roleTimes=" + roleTimes
				+ ", weekTimes=" + weekTimes + "]";
	}

}

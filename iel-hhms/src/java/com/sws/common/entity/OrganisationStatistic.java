package com.sws.common.entity;

public class OrganisationStatistic {
	
	private String name;
	
	private int totalTimes;
	
	private RoleTimes roleTimes;
	
	private WeekTimes weekTimes;
	
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

	public RoleTimes getRoleTimes() {
		return roleTimes;
	}

	public void setRoleTimes(RoleTimes roleTimes) {
		this.roleTimes = roleTimes;
	}

	public WeekTimes getWeekTimes() {
		return weekTimes;
	}

	public void setWeekTimes(WeekTimes weekTimes) {
		this.weekTimes = weekTimes;
	}

	@Override
	public String toString() {
		return "OrganisationStatistic [name=" + name + ", totalTimes=" + totalTimes + ", roleTimes=" + roleTimes
				+ ", weekTimes=" + weekTimes + "]";
	}

}

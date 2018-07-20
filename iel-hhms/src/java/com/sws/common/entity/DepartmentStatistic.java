package com.sws.common.entity;

public class DepartmentStatistic {
	
	private Long id;
	
	private String name;
	
	private int times;
	
	private int rank;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "DepartmentStatistic [id=" + id + ", name=" + name + ", times=" + times + ", rank=" + rank + "]";
	}

}

package com.sws.common.entity;

import java.util.List;

/**
 * 用于分页的类
 * @author wwg
 *
 * @param <E>
 */
public class PageInfo<E> {
	
	/*
	 * 结果集
	 */
	private List<E> list;
	
	/*
	 * 结果集的记录数
	 */
	private int totalRecords;
	
	/*
	 * 每页的条数
	 */
	private int pageSize;
	
	/*
	 * 第几页
	 */
	private int pageNo;
	
	public List<E> listByPage(int pageNo, int pageSize, List<E> list){
		int total = list.size();
		List<E> listPage = null;
        int size = pageSize;
        listPage = list.subList(size*(pageNo-1), ((size*pageNo) > total ? total : (pageSize*pageNo )));  
		return listPage;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "PageInfo [list=" + list + ", totalRecords=" + totalRecords
				+ ", pageSize=" + pageSize + ", pageNo=" + pageNo + "]";
	}

}

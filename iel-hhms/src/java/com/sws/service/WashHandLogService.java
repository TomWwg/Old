package com.sws.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.WashHandLog;
import com.sys.core.util.bean.Page;



public interface WashHandLogService {
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder, Date startTime, Date endTime);
	public Page pageByEvent(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public List<WashHandLog> RateCompare(QueryEntity queryEntity);
	public List<WashHandLog> EventCompare(QueryEntity queryEntity);
	public List<WashHandLog> SanitizerNum(QueryEntity queryEntity);
	public void save(WashHandLog info);
	public void saveAll(List<WashHandLog> list);
	public void proWashHand(WashHandLog washHandLog);
	
	/**
	 * 通过传入的时间查询当天的大门外手卫生事件(已分页)
	 * @param queryEntity
	 * @return
	 * @author wwg
	 */
	public List<WashHandLog> findOutDoorEventOneDay(QueryEntity queryEntity, int pageNo, int pageSize);
	
	/**
	 * 通过传入的时间段查询该时间段内每一天的大门手卫生次数
	 * @param queryEntity
	 * @return
	 * @author wwg
	 */
	public List<Integer> findNumberOfOutDoorEventByDate(QueryEntity queryEntity);
	
	/**
	 * 通过传入的时间段（开始时间和结束时间）和科室查询该科室在该时间段的人员手卫生信息
	 * @param queryEntity
	 * @return
	 */
	public Map<String, List<Integer>> findFrequencyByTimeAndDepartment(QueryEntity queryEntity);
	
	/**
	 * 重要说明：在调用此方法之前必须加入rfids，不然调用该方法查询得出的结果与预想不符
	 * 通过传入的科室和时间段（开始时间和结束时间）查询该科室在该时间段内的手卫生信息
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findByTimeAndDepartment(QueryEntity queryEntity);
	
	/**
	 * 通过开始时间、结束时间、rfids查找对应的手卫生事件
	 * @param startTime
	 * @param endTime
	 * @param rfids
	 * @return
	 */
	public List<WashHandLog> findByTimeAndRfids(Date startTime, Date endTime, List<String> rfids);
	
	/**
	 * 通过开始时间、结束时间、rfids、eventType来查找对应的手卫生时间（目前用于首页的接近患者后依从率）
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findLogsOfClosingPatients(QueryEntity queryEntity);
	
	/**
	 * 清洁或有限清洁状态下接近患者次数
	 * @param queryEntity
	 * @return
	 */
	public int findTimesOfClosePatientAfterContactWithPatient(QueryEntity queryEntity);
	
	/**
	 * 接近患者未手卫生次数
	 * @param queryEntity
	 * @return
	 */
	public int findLogsNotWashHangBeforeClosingPatient(QueryEntity queryEntity);
	
	/**
	 * 通过时间来查找符合条件的手卫生事件
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findWashHandLogsByTime(QueryEntity queryEntity);
	
	/**
	 * 通过时间查询近7天医院的手卫生次数
	 * @param queryEntity
	 * @return
	 */
	List<Integer> findNumberByDate(QueryEntity queryEntity);
	
	/**
	 * 通过时间和部门Id查询近7天医院的手卫生次数
	 * @param queryEntity
	 * @return
	 */
	List<Integer> findNumberByDateAndDepartmentId(QueryEntity queryEntity);

}

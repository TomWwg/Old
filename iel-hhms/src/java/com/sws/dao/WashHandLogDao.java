package com.sws.dao;

import java.util.Date;
import java.util.List;

import com.gk.extend.hibernate.dao.IEntityDao;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.WashHandLog;


public interface WashHandLogDao extends IEntityDao<WashHandLog> {
	public List<WashHandLog> RateCompare(QueryEntity queryEntity);
	public List<WashHandLog> EventCompare(QueryEntity queryEntity);
	public List<WashHandLog> SanitizerNum(QueryEntity queryEntity);
	
	/**
	 * 通过传入的时间查询当天的大门手卫生事件
	 * @param queryEntity
	 * @return
	 * @author wwg
	 */
	public List<WashHandLog> findOutDoorEventOneDay(QueryEntity queryEntity);

	/**
	 * 通过时间段(开始时间和结束时间)和科室查询该科室在该时间段内的手卫生事件
	 * @param queryEntity
	 * @return
	 * @author wwg
	 */
	public List<WashHandLog> findByTimeAndDepartment(QueryEntity queryEntity);
	
	/**
	 * 通过开始时间、结束时间、rfids查找对应的手卫生事件
	 * @param startTime
	 * @param endTime
	 * @param rfids
	 * @return
	 */
	public List<WashHandLog> findByTimeAndRfids(Date startTime, Date endTime,
			List<String> rfids);
	
	/**
	 * 通过开始时间、结束时间、rfids、eventType来查找对应的手卫生事件（目前用于首页的接近患者后依从率）
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findLogsOfClosingPatients(QueryEntity queryEntity);
	
	/**
	 * 接近患者后依从率的分母中使用，查找清洁或有限清洁状态下接近患者次数
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findClosePatientAfterContactWithPatient(QueryEntity queryEntity);

	/**
	 * 接触患者前未手卫生
	 * @param queryEntity
	 * @return
	 */
	public List<WashHandLog> findLogsNotWashHangBeforeClosingPatient(QueryEntity queryEntity);
}

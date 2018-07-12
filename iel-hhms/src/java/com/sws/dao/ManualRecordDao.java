package com.sws.dao;

import java.util.Date;
import java.util.List;

import com.sws.model.ManualRecord;

/**
 * 人工录入模块的接口
 * @author wwg
 *
 */
public interface ManualRecordDao {
	
	/**
	 * 新增人工录入记录
	 * @param manualRecord
	 */
	void add(ManualRecord manualRecord);
	
	/**
	 * 查找所有的人工录入的手卫生事件
	 * @return
	 */
	List<ManualRecord> findAll();
	
	/**
	 * 通过科室、时间、洗手方式、人员类别的一个或多个条件查询对应的事件，
	 * 在上一层先判断有哪些条件，完成sql语句后再传进来
	 * @return
	 */
	List<ManualRecord> findByDTWR(String hql);
	
	/**
	 * 通过部门Id，洗手时间，洗手方式查找人工录入的手卫生记录
	 * @param departId  部门Id
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @param way       洗手方式
	 * @return
	 */
	List<ManualRecord> findByDepartIdAndTimeAndWay(Long departId, Date timeStart, Date timeEnd, Short way);
	
	/**
	 * 通过洗手时间，洗手方式查找人工录入的手卫生记录（主要用于感控科的查询）
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @param way       洗手方式
	 * @return
	 */
	List<ManualRecord> findByTimeAndWay(Date timeStart, Date timeEnd, Short way);

	/**
	 * 通过部门Id，洗手时间查找人工录入的手卫生记录
	 * @param departId  部门Id
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @return
	 */
	List<ManualRecord> findByDepartIdAndTime(Long departId, Date timeStart, Date timeEnd);
	
	/**
	 * 通过洗手时间查找人工录入的手卫生记录（主要用于感控科的查询）
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @return
	 */
	List<ManualRecord> findByTime(Date timeStart, Date timeEnd);
}

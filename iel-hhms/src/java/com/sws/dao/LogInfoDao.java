package com.sws.dao;

import java.util.List;

import com.gk.extend.hibernate.dao.IEntityDao;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.LogInfo;


public interface LogInfoDao extends IEntityDao<LogInfo> {
	
	/**
	 * 通过userID删除对应的LogInfo
	 * @param userId
	 * @author wwg
	 */
	public void deleteByUserId(Long userId);
	
	/**
	 * 查找没有admin以外的日志记录
	 * @param queryEntity
	 * @param start
	 * @param limit
	 * @param sortname
	 * @param sortorder
	 * @return
	 * @author wwg
	 */
	List<LogInfo> findLogsNotAdmin(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	
}

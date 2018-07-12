package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.LogInfo;
import com.sws.model.UserInfo;
import com.sys.core.util.bean.Page;



public interface LogInfoService {
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
	public void addLog(Integer type, String remark, UserInfo userInfo);
	
	/**
	 * 当用户不是admin的时候，查询的登录日志将admin的去掉
	 * @param queryEntity
	 * @param start
	 * @param limit
	 * @param sortname
	 * @param sortorder
	 * @return
	 */
	List<LogInfo> findLogsWhileUserNotAdmin(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
}

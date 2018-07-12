package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.ScheduleTemplate;
import com.sys.core.util.bean.Page;

public interface ScheduleService {

	public ScheduleTemplate getById(Long id);
	public void save(ScheduleTemplate scheduleTemplate);
	public void update(ScheduleTemplate scheduleTemplate);
	public void deleteAll(String ids);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
}

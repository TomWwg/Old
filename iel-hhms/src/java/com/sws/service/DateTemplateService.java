package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.DateTemplate;
import com.sys.core.util.bean.Page;

public interface DateTemplateService {

	public DateTemplate getById(Long id);
	public void save(DateTemplate dateTemplate);
	public void update(DateTemplate dateTemplate);
	public void deleteAll(String ids);
	public Page page(String departIds,QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);
}

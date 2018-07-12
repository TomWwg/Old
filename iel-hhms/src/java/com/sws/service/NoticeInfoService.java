package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.NoticeInfo;
import com.sys.core.util.bean.Page;



public interface NoticeInfoService {
	public NoticeInfo getById(Long id);
	public void save(NoticeInfo info);
	public void update(NoticeInfo info);
	public void deleteAll(String ids);
	public Page page(QueryEntity queryEntity,int start, int limit,String sortname,String sortorder);

}

package com.sws.service;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sys.core.util.bean.Page;



public interface SignalLogService {
	public Page pageByEvent(QueryEntity queryEntity, int start, int limit,String sortname,String sortorder);

}

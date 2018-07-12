package com.sws.dao;

import java.util.List;

import com.gk.extend.hibernate.dao.IEntityDao;
import com.sws.model.RoleInfo;


public interface RoleInfoDao extends IEntityDao<RoleInfo> {

	List<RoleInfo> getByOrg(String org);
    

	
}

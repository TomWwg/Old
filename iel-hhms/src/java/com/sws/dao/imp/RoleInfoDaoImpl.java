package com.sws.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.RoleInfoDao;
import com.sws.model.RoleInfo;

//@Repository("staffInfoDao")
public class RoleInfoDaoImpl extends HibernateEntityDao<RoleInfo> implements RoleInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<RoleInfo> getByOrg(String org) {
		if(StringUtils.isNotBlank(org)){
			DetachedCriteria dc=DetachedCriteria.forClass(RoleInfo.class);
			if(!org.equals("1")){
				dc.add(Restrictions.ne("name", "管理员"));
			}
			return findByDetachedCriteria(dc);
		}
		return null;
	}


}

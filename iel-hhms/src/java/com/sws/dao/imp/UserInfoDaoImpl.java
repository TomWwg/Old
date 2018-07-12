package com.sws.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.UserInfoDao;
import com.sws.model.UserInfo;

//@Repository("staffInfoDao")
public class UserInfoDaoImpl extends HibernateEntityDao<UserInfo> implements UserInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<UserInfo> getByUserName(String userName) {
		if(StringUtils.isNotBlank(userName)){
			DetachedCriteria dc=DetachedCriteria.forClass(UserInfo.class);
			dc.add(Restrictions.eq("name", userName));
			List<UserInfo> userInfos=findByDetachedCriteria(dc);
			return userInfos;
		}
		return null;
	}


}

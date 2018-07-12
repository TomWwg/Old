package com.sws.dao.imp;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.ParameterInfoDao;
import com.sws.model.ParameterInfo;

//@Repository("staffInfoDao")
public class ParameterInfoDaoImpl extends HibernateEntityDao<ParameterInfo> implements ParameterInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ParameterInfo findByTypeAndKey(Integer type, String key) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(ParameterInfo.class);
		if(type!=null){
			criteria.add(Restrictions.eq("type",type));
		}
		if(key!=null){
			criteria.add(Restrictions.eq("key",key));
		}
		List <ParameterInfo> list = findByDetachedCriteria(criteria);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ParameterInfo> findByType(Integer type) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(ParameterInfo.class);
		if(type!=null){
			criteria.add(Restrictions.eq("type",type));
		}
		return findByDetachedCriteria(criteria);
	}


}

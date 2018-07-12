package com.sws.dao.imp;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.dao.PreRateStatisticsDao;
import com.sws.model.PreRateStatistics;
import com.sys.core.util.StringUtils;

//@Repository("staffInfoDao")
public class PreRateStatisticsDaoImpl extends HibernateEntityDao<PreRateStatistics> implements PreRateStatisticsDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<PreRateStatistics> queryList(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(PreRateStatistics.class);
		if(queryEntity.getInt1()!=null){//记录类型
			criteria.add(Restrictions.ne("recordType",queryEntity.getInt1()));
		}
		if(StringUtils.isNotBlank(queryEntity.getStr2())){
			criteria.add(Restrictions.ge("timeStr", queryEntity.getStr2()));
		}
		if(StringUtils.isNotBlank(queryEntity.getStr3())){
			criteria.add(Restrictions.le("timeStr", queryEntity.getStr3()));
		}
		return findByDetachedCriteria(criteria);
	}
    
}

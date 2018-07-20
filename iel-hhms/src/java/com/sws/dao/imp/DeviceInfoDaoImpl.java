package com.sws.dao.imp;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.DeviceInfoDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;

//@Repository("staffInfoDao")
public class DeviceInfoDaoImpl extends HibernateEntityDao<DeviceInfo> implements DeviceInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public DeviceInfo findByNoType(String no, String type) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeviceInfo.class);
		if(no!=null){
			criteria.add(Restrictions.eq("no",no));
		}
		if(type!=null){
			criteria.add(Restrictions.eq("type", type));
		}
		List <DeviceInfo> list = findByDetachedCriteria(criteria);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<DeviceInfo> findByDepart(List<String> manageDepart, String org) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeviceInfo.class);
		if(manageDepart!=null&&!manageDepart.contains(org)){
			criteria.add(Restrictions.eq("departId", Long.valueOf(org)));
		}
		return findByDetachedCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> findByDepartId(Long departId) {
		String hql = "from DeviceInfo where type = '40' and departId = "+departId;
		List<DeviceInfo> deviceInfos = (List<DeviceInfo>) getHibernateTemplate().find(hql);
		return deviceInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfo> findAll() {
		String hql = "from DeviceInfo";
		List<DeviceInfo> deviceInfos = getHibernateTemplate().find(hql);
		return deviceInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupTree> findByTreeId(Integer id) {
		String hql = "from GroupTree where id = " + id;
		List<GroupTree> groupTree = getHibernateTemplate().find(hql);
		return groupTree;
	}

	@Override
	public List<DeviceInfo> findByStaffId(List<Long> staffIds) {
		List<DeviceInfo> deviceInfos = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(DeviceInfo.class);
		criteria.add(Restrictions.in("staffId", staffIds));
		deviceInfos = findByDetachedCriteria(criteria);
		return deviceInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRFIDByStaffIds(List<Long> staffIds) {
		String hql = "select d.no from DeviceInfo d where d.staffId in "+staffIds;
		List<String> rfids = getHibernateTemplate().find(hql);
		return rfids;
	}


}

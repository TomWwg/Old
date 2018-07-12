package com.sws.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.GroupTreeDao;
import com.sws.model.GroupTree;

//@Repository("staffInfoDao")
public class GroupTreeDaoImpl extends HibernateEntityDao<GroupTree> implements GroupTreeDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
    
	@Override
	public List<GroupTree> findByTypes(List<Integer> types) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.in("type", types));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<GroupTree> getDepart() {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.ne("id",1L));//不是医院节点
		criteria.add(Restrictions.eq("type", 0));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<GroupTree> findByParentIds(List<Long> parentIds) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.in("parentId", parentIds));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<GroupTree> getUserDepart() {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		//criteria.add(Restrictions.ne("id",1L));//不是医院节点
		criteria.add(Restrictions.eq("type", 0));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<String> getManageDeparts() {
		// TODO Auto-generated method stub
		List<GroupTree> manageDepartList= new ArrayList<GroupTree>();
		List<String> manageDeparts=new ArrayList<String>();
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.eq("departLevel", 0));//0表示管理部门
		manageDepartList=findByDetachedCriteria(criteria);
		if(manageDepartList!=null&&manageDepartList.size()>0){
			for(GroupTree groupTree:manageDepartList){
				if(groupTree.getId()!=null){
					manageDeparts.add(String.valueOf(groupTree.getId()));
				}
			}
		}
		return manageDeparts;
	}
}

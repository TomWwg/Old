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
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.in("type", types));
		return findByDetachedCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GroupTree> getDepart() {
		String hql = "from GroupTree where type=0 and id!=1";
		List<GroupTree> groupTrees = getHibernateTemplate().find(hql);
		return groupTrees;
	}
	
	@Override
	public List<GroupTree> findByParentIds(List<Long> parentIds) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.in("parentId", parentIds));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<GroupTree> getUserDepart() {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		//criteria.add(Restrictions.ne("id",1L));//不是医院节点
		criteria.add(Restrictions.eq("type", 0));
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<String> getManageDeparts() {
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
	
	@Override
	public String getHospitalName() {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.eq("parentId", 0L));
//		String hql = "select name from GroupTree where parentId=0";
//		String name = getHibernateTemplate().find(hql).toString();
		String name = findByDetachedCriteria(criteria).get(0).getName();
		return name;
	}
	
	@Override
	public String getNameById(Long id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupTree.class);
		criteria.add(Restrictions.eq("id", id));
		List<GroupTree> groupTrees = findByDetachedCriteria(criteria);
		return groupTrees.get(0).getName();
	}
}

package com.sws.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.dao.StaffInfoDao;
import com.sws.model.StaffInfo;

//@Repository("staffInfoDao")
public class StaffInfoDaoImpl extends HibernateEntityDao<StaffInfo> implements StaffInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public List<StaffInfo> findByDepartAndType(String departIds,Long departId,List<String> types) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!departIds.equals("1")){//不是最高级别的管理员admin
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		return findByDetachedCriteria(criteria);
	}
	@Override
	/**
	 * @para departIds 当前用户的部门id
	 * @para departId  当前选中的组织树部门id
	 */
	public List<StaffInfo> findByNameAndType(String departIds, Long departId,
			String name, List<String> types) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!departIds.equals("1")){//不是最高级别的管理员admin
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(StringUtils.isNotBlank(name)){
			criteria.add(Restrictions.like("name", "%"+name+"%"));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<StaffInfo> findByDepartAndTypeAndName(String departIds, Long departId,
			List<String> types, String staffName) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!departIds.equals("1")){//不是管理部门
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		if(staffName!=null&&!staffName.equals("")){
			criteria.add(Restrictions.like("name", "%"+staffName+"%"));
		}
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<StaffInfo> findByDepart(List<String> manageDepart,String curDepartId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(manageDepart!=null&&!manageDepart.contains(curDepartId)){
			criteria.add(Restrictions.eq("groupTree.id", Long.valueOf(curDepartId)));
		}
		
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<StaffInfo> findByNameAndTypeAndDLevel(
			List<String> manageDepart, String departIds, Long departId,
			String name, List<String> types) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!manageDepart.contains(departIds)){//不是最高级别的管理员admin
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(StringUtils.isNotBlank(name)){
			criteria.add(Restrictions.like("name", "%"+name+"%"));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<StaffInfo> findByDepartAndTypeAndNameAndDLevel(
			List<String> manageDepart, String departIds, Long departId,
			List<String> types, String staffName) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!manageDepart.contains(departIds)){//不是管理部门
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		if(staffName!=null&&!staffName.equals("")){
			criteria.add(Restrictions.like("name", "%"+staffName+"%"));
		}
		return findByDetachedCriteria(criteria);
	}
	@Override
	public List<StaffInfo> findByDepartAndTypeAndDlevel(
			List<String> manageDepart, String departIds, Long departId,
			List<String> types) {
		List<Long> ids = new ArrayList<Long>();
		String[] array = departIds.split("\\*");
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		if(departId.equals(1L)){ //医院目录
			if(!manageDepart.contains(departIds)){//不是最高级别的管理员admin
				for(String dId:array){
					ids.add(Long.valueOf(dId));
				}
				criteria.add(Restrictions.in("groupTree.id", ids));
			}
		}else{
			ids.add(departId);
			criteria.add(Restrictions.in("groupTree.id", ids));
		}
		if(types!=null&&types.size()>0){
			criteria.add(Restrictions.in("category", types));
		}
		return findByDetachedCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffInfo> findByStaffNo(String staffNo) {
		String hql = "from StaffInfo where no='" + staffNo +"'";
		List<StaffInfo> staffInfos = getHibernateTemplate().find(hql);
		return staffInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findNameByRfid(String rfid) {
		String name = "";
		String hql = "from StaffInfo s WHERE s.id=(select DISTINCT d.staffId from DeviceInfo d where d.no='"+rfid+"')";
		List<StaffInfo> staffInfos = getHibernateTemplate().find(hql);
		if (staffInfos.size() > 0){
			name = staffInfos.get(0).getName();
		}
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffInfo> findByDepartmentId(Long departmentId) {
		String hql = "from StaffInfo where groupTree = '" + departmentId + "'";
		List<StaffInfo> staffInfos = getHibernateTemplate().find(hql);
		return staffInfos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffInfo> findByDepartmentIdAndStaffType(String category,
			Long departmentId) {
		List<StaffInfo> staffInfos = null;
		String hql = "from StaffInfo where category = '" + category + "' and groupTree = '" + departmentId +"'";
		staffInfos = getHibernateTemplate().find(hql);
		return staffInfos;
	}
	
	@Override
	public List<StaffInfo> findByGroupId(Long groupId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		criteria.add(Restrictions.eq("groupTree.id", groupId));
		List<StaffInfo> staffInfos = findByDetachedCriteria(criteria);
		return staffInfos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffInfo> findStaffIdByCategory(String category) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffInfo.class);
		criteria.add(Restrictions.eq("category", category));
		List<StaffInfo> staffInfos = findByDetachedCriteria(criteria);
		return staffInfos;
	}
}

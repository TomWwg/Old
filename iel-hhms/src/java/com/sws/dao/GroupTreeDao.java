package com.sws.dao;import java.util.List;import com.gk.extend.hibernate.dao.IEntityDao;import com.sws.model.GroupTree;public interface GroupTreeDao extends IEntityDao<GroupTree> {	public List<GroupTree> findByTypes(List<Integer> types);	public List<GroupTree> getDepart();	public List<GroupTree> findByParentIds(List<Long> parentIds);	public List<GroupTree> getUserDepart();	public List<String> getManageDeparts();		/**	 * 得到医院名称	 * @return	 */	public String getHospitalName();		/**	 * 根据Id得到对应的名字	 * @return	 */	public String getNameById(Long id);}
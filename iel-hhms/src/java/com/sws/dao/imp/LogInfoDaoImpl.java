package com.sws.dao.imp;

import java.util.List;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.common.until.DateUtils;
import com.sws.dao.LogInfoDao;
import com.sws.model.LogInfo;

//@Repository("staffInfoDao")
public class LogInfoDaoImpl extends HibernateEntityDao<LogInfo> implements LogInfoDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void deleteByUserId(Long userId) {
		String hql = "delete from LogInfo l where l.userInfo = ?";
		getSession().createQuery(hql).setLong(0, userId).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LogInfo> findLogsNotAdmin(QueryEntity queryEntity, int start,
			int limit, String sortname, String sortorder) {
		String str1 = DateUtils.date2String(queryEntity.getStartTime());
		String str2 = DateUtils.date2String(queryEntity.getEndTime());
		String hql = "";
		if(queryEntity.getInt1() != null){
			if(sortorder.toLowerCase().equals("asc")){
				hql = "from LogInfo l where l.userInfo != " + 1 + " and l.createTime between '"
						+ str1 + "' and '" + str2 + "' and type = " + queryEntity.getInt1() + " order by createTime asc";
			} else {
				hql = "from LogInfo l where l.userInfo != " + 1 + " and l.createTime between '"
						+ str1 + "' and '" + str2 + "' and type = " + queryEntity.getInt1() + " order by createTime desc";
			}
		} else {
			if(sortorder.toLowerCase().equals("asc")){
				hql = "from LogInfo l where l.userInfo != " + 1 + " and l.createTime between '"
						+ str1 + "' and '" + str2 + "' order by createTime asc";
			} else {
				hql = "from LogInfo l where l.userInfo != " + 1 + " and l.createTime between '"
						+ str1 + "' and '" + str2 + "' order by createTime desc";
			}
		}
		List<LogInfo> logInfos = getHibernateTemplate().find(hql);
		return logInfos;
	}

}

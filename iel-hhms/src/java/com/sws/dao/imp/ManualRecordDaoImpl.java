package com.sws.dao.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.sws.common.until.DateUtils;
import com.sws.dao.ManualRecordDao;
import com.sws.model.ManualRecord;

@SuppressWarnings("rawtypes")
public class ManualRecordDaoImpl extends HibernateEntityDao implements ManualRecordDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void add(ManualRecord manualRecord) {
		getHibernateTemplate().save(manualRecord);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findAll() {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		String hql = "from ManualRecord";
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findByDTWR(String hql) {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findByDepartIdAndTimeAndWay(Long departId,
			Date timeStart, Date timeEnd, Short way) {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		String start = DateUtils.date2String(timeStart);
		String end = DateUtils.date2String(timeEnd);
		String hql = "from ManualRecord where departId=" + departId + " and time >= '" + start + "' and time < '" + end + "' and way=" + way;
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findByTimeAndWay(Date timeStart,
			Date timeEnd, Short way) {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		String start = DateUtils.date2String(timeStart);
		String end = DateUtils.date2String(timeEnd);
		String hql = "from ManualRecord where time >= '" + start + "' and time < '" + end + "' and way = " + way;
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findByDepartIdAndTime(Long departId,
			Date timeStart, Date timeEnd) {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		String start = DateUtils.date2String(timeStart);
		String end = DateUtils.date2String(timeEnd);
		String hql = "from ManualRecord where departId=" + departId + " and time >= '" + start + "' and time < '" + end + "'";
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManualRecord> findByTime(Date timeStart, Date timeEnd) {
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		String start = DateUtils.date2String(timeStart);
		String end = DateUtils.date2String(timeEnd);
		String hql = "from ManualRecord where time >= '" + start + "' and time < '" + end + "'";
		manualRecords = getHibernateTemplate().find(hql);
		return manualRecords;
	}

}

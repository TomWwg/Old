package com.sws.dao.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.gk.extend.hibernate.dao.HibernateEntityDao;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.dao.WashHandLogDao;
import com.sws.model.WashHandLog;

//@Repository("staffInfoDao")
public class WashHandLogDaoImpl extends HibernateEntityDao<WashHandLog>
		implements WashHandLogDao {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;

	// 依从率计算
	private DetachedCriteria rateCriteria(DetachedCriteria criteria) {
		List<Integer> rfidStatus = new ArrayList<Integer>();
		rfidStatus.add(5);
		rfidStatus.add(7);
		rfidStatus.add(8);
		rfidStatus.add(11);
		rfidStatus.add(13);
		List<String> event = new ArrayList<String>();
		event.add("0003"); //有效洗手次数
		//event.add("0007");//进入病区
		//event.add("0008");//离开病区
		event.add("0102"); //离开病区未洗手
		event.add("0103"); //接触病床前未洗手
		event.add("0110"); //离开病床未洗手
		criteria.add(Restrictions.in("eventType", event));
		//criteria.add(Restrictions.not(Restrictions.in("rfidStatus", rfidStatus)));
		return criteria;
	}

	// 事件比较计算
	private DetachedCriteria eventCompareCriteria(DetachedCriteria criteria) {
		List<Integer> rfidStatus = new ArrayList<Integer>();
		rfidStatus.add(6);// 离开病区提醒
		rfidStatus.add(7);// 进入病区未手卫生
		rfidStatus.add(8);// 离开病区未手卫生
		rfidStatus.add(9);// 短时接近病床提醒
		rfidStatus.add(10);// 长时接近病床提醒
		rfidStatus.add(11);//接触病床前未手卫生
		rfidStatus.add(12);//短时离开病床提醒
		rfidStatus.add(13);//离开病床未手卫生
		criteria.add(Restrictions.eq("eventType", "0003"));
		criteria.add(Restrictions.in("rfidStatus", rfidStatus));
		return criteria;
	}

	@Override
	public List<WashHandLog> RateCompare(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		rateCriteria(criteria);
		return findByDetachedCriteria(criteria);
	}

	@Override
	public List<WashHandLog> EventCompare(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		eventCompareCriteria(criteria);
		return findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<WashHandLog> SanitizerNum(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.add(Restrictions.eq("eventType", "0003"));
		return findByDetachedCriteria(criteria);
	}

	@Override
	public List<WashHandLog> findOutDoorEventOneDay(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		//0004为外门液瓶识别器不带胸牌的手卫生事件
		criteria.add(Restrictions.eq("eventType", "0004"));
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.addOrder(Order.desc("createTime"));
		List<WashHandLog> washHandLogs = findByDetachedCriteria(criteria);
		return washHandLogs;
	}

	@Override
	public List<WashHandLog> findByTimeAndDepartment(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		//0003为手卫生事件
		criteria.add(Restrictions.eq("eventType", "0003"));
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.addOrder(Order.desc("createTime"));
		List<WashHandLog> washHandLogs = findByDetachedCriteria(criteria);
		return washHandLogs;
	}

	@Override
	public List<WashHandLog> findByTimeAndRfids(Date startTime, Date endTime,
			List<String> rfids) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WashHandLog.class);
		criteria.add(Restrictions.eq("eventType", "0003"));
		if(rfids != null && rfids.size() > 0){
			criteria.add(Restrictions.in("rfid", rfids));
		}
		criteria.add(Restrictions.between("createTime", startTime, endTime));
		List<WashHandLog> washHandLogs = findByDetachedCriteria(criteria);
		return washHandLogs;
	}

	@Override
	public List<WashHandLog> findLogsOfClosingPatients(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.add(Restrictions.eq("eventType", "0018"));
		return findByDetachedCriteria(criteria);
	}

	@Override
	public List<WashHandLog> findClosePatientAfterContactWithPatient(
			QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		List<Integer> rfidStatus = new ArrayList<Integer>();
		rfidStatus.add(1);//清洁
		rfidStatus.add(2);//有限清洁
		criteria.add(Restrictions.eq("eventType", "0018"));
		criteria.add(Restrictions.in("rfidStatus", rfidStatus));
		return findByDetachedCriteria(criteria);
	}

	@Override
	public List<WashHandLog> findLogsNotWashHangBeforeClosingPatient(
			QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			criteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.add(Restrictions.eq("eventType", "0103"));
		return findByDetachedCriteria(criteria);
	}

	@Override
	public List<WashHandLog> findWashHandLogsByTime(QueryEntity queryEntity) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(WashHandLog.class);
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			criteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		criteria.add(Restrictions.eq("eventType", "0003"));
		return findByDetachedCriteria(criteria);
	}
	
}

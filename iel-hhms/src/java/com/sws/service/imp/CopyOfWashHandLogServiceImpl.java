package com.sws.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.gk.extend.hibernate.entity.DataStatusType;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.gk.extend.hibernate.template.PageHibernateTemplate;
import com.sws.common.until.BeanUtils;
import com.sws.dao.SignalLogDao;
import com.sws.dao.WashHandLogDao;
import com.sws.model.SignalLog;
import com.sws.model.WashHandLog;
import com.sws.service.WashHandLogService;
import com.sys.core.util.StringUtils;
import com.sys.core.util.bean.Page;

//@Service("staffInfoService")
public class CopyOfWashHandLogServiceImpl implements WashHandLogService {
    
	@Autowired
    private WashHandLogDao washHandLogDao;
	@Autowired
    private SignalLogDao signalLogDao;
	@Autowired
	private PageHibernateTemplate pageHibernateTemplate;
	
	@Override
	public Page page(QueryEntity queryEntity, int start, int limit,
			String sortname, String sortorder, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		List<String> paramList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(queryEntity.getStr1())){
			paramList.add("no");
			valueList.add(queryEntity.getStr1());
		}
		if(StringUtils.isNotBlank(queryEntity.getStr2())){
			paramList.add("name");
			valueList.add(queryEntity.getStr2());
		}
		 Page page = washHandLogDao.page(paramList.toArray(new String[paramList.size()]), valueList.toArray(),start, limit, sortname,
				 sortorder.toLowerCase().equals("asc")?true:false, startTime, endTime);
		return page;
	}
	@Override
	public Page pageByEvent(QueryEntity queryEntity, int start, int limit,String sortname,String sortorder) {
		// TODO Auto-generated method stub
		if(sortname.equals("eventTypeName")){
			sortname="eventType";
		}else if(sortname.equals("rfidStatusName")){
			sortname="rfidStatus";
		} //排序名称转变		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WashHandLog.class);
		detachedCriteria.add(Restrictions.eq("status", DataStatusType.NORMAL.getNumber()));
		List<String> event = new ArrayList<String>();
		List<Integer> rfidStatus = new ArrayList<Integer>();
		if (queryEntity.getRfidList() != null
				&& queryEntity.getRfidList().size() > 0) {
			detachedCriteria.add(Restrictions.in("rfid", queryEntity.getRfidList()));
		}
		if (queryEntity.getStr1() != null) {
			String[] eventArray = queryEntity.getStr1().split(",");// 事件类型
			for (String info : eventArray) {
				event.add(info);
			}
			detachedCriteria.add(Restrictions.in("eventType", event));
		}
		if (queryEntity.getStr2() != null) {
			String[] rfidStatusArray = queryEntity.getStr2().split(",");// 标签状态
			for (String info : rfidStatusArray) {
				rfidStatus.add(Integer.valueOf(info));
			}
			detachedCriteria.add(Restrictions.in("rfidStatus", rfidStatus));
		}
		if (queryEntity.getStartTime() != null
				&& queryEntity.getEndTime() != null) {
			detachedCriteria.add(Restrictions.between("createTime",
					queryEntity.getStartTime(), queryEntity.getEndTime()));
		}
		if (sortorder.toLowerCase().equals("asc")) {
			detachedCriteria.addOrder(Order.asc(sortname));
		} else {
			detachedCriteria.addOrder(Order.desc(sortname));
		};
		Page page = pageHibernateTemplate.pageByDetachedCriteria(detachedCriteria, start, limit);
		return page;
	}
	@Override
	public List<WashHandLog> RateCompare(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return washHandLogDao.RateCompare(queryEntity);
	}
	
	@Override
	public List<WashHandLog> SanitizerNum(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return washHandLogDao.SanitizerNum(queryEntity);
	}

	@Override
	public List<WashHandLog> EventCompare(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return washHandLogDao.EventCompare(queryEntity);
	}
    @Override
    public void save(WashHandLog info) {
        // TODO Auto-generated method stub
        washHandLogDao.save(info);
        
    }
    @Override
    public void saveAll(List<WashHandLog> list) {
        // TODO Auto-generated method stub
        washHandLogDao.saveAll(list);
    }
    @Override
    public void proWashHand(WashHandLog washHandLog) {
        // TODO Auto-generated method stub
        int rfid=0,deviceNo=0;
        String sql="";
        List<Object[]> listObject;
        SignalLog log = new SignalLog();
        BeanUtils.copyProperties(washHandLog, log);
        if(washHandLog.getType()!=null){
            switch(Integer.parseInt(washHandLog.getType())){
            case 1000:  //洗手命令
                Object[] vs = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM wash_hand_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    save(washHandLog);
                    signalLogDao.save(log);
                } 
                break;
            case 2000://欠压报警
                Object[] vs0 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs0);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    rfid =Integer.parseInt(washHandLog.getRfid());
                    deviceNo =Integer.parseInt(washHandLog.getDeviceNo());
                    if(rfid==0){//设备器欠压
                        sql="Update device_info set device_status='2',update_time='"+washHandLog.getTimeStr()+"' Where type='"+washHandLog.getDeviceType()+"' and no='"+washHandLog.getDeviceNo()+"'";
                    }else if(deviceNo==0){//胸牌欠压
                        sql="Update device_info set device_status='2',update_time='"+washHandLog.getTimeStr()+"' Where type='40' and no='"+washHandLog.getRfid()+"'";
                        pageHibernateTemplate.executeSql(sql);              
                    }
                    signalLogDao.save(log);
                } 
                break;
            case 2001://欠压恢复
                Object[] vs1 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs1);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    rfid =Integer.parseInt(washHandLog.getRfid());
                    deviceNo =Integer.parseInt(washHandLog.getDeviceNo());
                    if(rfid==0){//设备器欠压恢复
                        sql="Update device_info set device_status='0',update_time='"+washHandLog.getTimeStr()+"' Where type='"+washHandLog.getDeviceType()+"' and no='"+washHandLog.getDeviceNo()+"'";
                    }else if(deviceNo==0){//胸牌欠压恢复
                        sql="Update device_info set device_status='0',update_time='"+washHandLog.getTimeStr()+"' Where type='40' and no='"+washHandLog.getRfid()+"'";
                        pageHibernateTemplate.executeSql(sql);              
                    }
                    signalLogDao.save(log);
                } 
                break;
            case 3000: //校时命令
                Object[] vs2 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs2);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    signalLogDao.save(log);
                } 
                break;
            case 4000://上报Ap编号
                Object[] vs3 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs3);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    sql="Update ap_set set no='"+washHandLog.getApNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                    pageHibernateTemplate.executeSql(sql);
                    signalLogDao.save(log);
                } 
                break;
            case 4001://上报Ap编码
                Object[] vs4 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs4);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    sql="Update ap_set set post_code='"+washHandLog.getPostCode()+"',hospital_no='"+washHandLog.getHospitalNo()+"',depart_no='"+washHandLog.getDeviceNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                    pageHibernateTemplate.executeSql(sql); 
                    signalLogDao.save(log);
                } 
                break;
            case 5000://心跳包
                Object[] vs5 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '45 second'";
                listObject= pageHibernateTemplate.createQueryBySql(sql, vs5);
                if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                    sql="Update ap_set set no='"+washHandLog.getApNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                    pageHibernateTemplate.executeSql(sql);
                    signalLogDao.save(log);
                } 
                break;
            }
        }
       
    }
	@Override
	public List<Integer> findNumberOfOutDoorEventByDate(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<WashHandLog> findOutDoorEventOneDay(QueryEntity queryEntity,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<WashHandLog> findByTimeAndDepartment(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, List<Integer>> findFrequencyByTimeAndDepartment(
			QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<WashHandLog> findByTimeAndRfids(Date startTime, Date endTime,
			List<String> rfids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<WashHandLog> findLogsOfClosingPatients(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int findTimesOfClosePatientAfterContactWithPatient(
			QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int findLogsNotWashHangBeforeClosingPatient(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<WashHandLog> findWashHandLogsByTime(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Integer> findNumberByDate(QueryEntity queryEntity) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
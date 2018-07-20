package com.sws.service.imp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.gk.extend.hibernate.entity.DataStatusType;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.gk.extend.hibernate.template.PageHibernateTemplate;
import com.sws.common.entity.PageInfo;
import com.sws.common.until.BeanUtils;
import com.sws.common.until.DateUtils;
import com.sws.dao.ApSetDao;
import com.sws.dao.DeviceInfoDao;
import com.sws.dao.SignalLogDao;
import com.sws.dao.StaffInfoDao;
import com.sws.dao.WashHandLogDao;
import com.sws.model.ApSet;
import com.sws.model.DeviceInfo;
import com.sws.model.SignalLog;
import com.sws.model.StaffInfo;
import com.sws.model.WashHandLog;
import com.sws.service.WashHandLogService;
import com.sys.core.util.StringUtils;
import com.sys.core.util.bean.Page;

//@Service("staffInfoService")
public class WashHandLogServiceImpl implements WashHandLogService {
    
	@Autowired
    private WashHandLogDao washHandLogDao;
	@Autowired
    private SignalLogDao signalLogDao;
	@Autowired
	private PageHibernateTemplate pageHibernateTemplate;
	@Autowired
    private ApSetDao apSetDao;
	@Autowired
	private StaffInfoDao staffInfoDao;
	@Autowired
	private DeviceInfoDao deviceInfoDao;
	
	
	@Override
	public Page page(QueryEntity queryEntity, int start, int limit,
			String sortname, String sortorder, Date startTime, Date endTime) {
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
		if (StringUtils.isNotBlank(queryEntity.getStr2())) {//胸牌状态
			String[] rfidStatusArray = queryEntity.getStr2().split(",");// 标签状态
			for (String info : rfidStatusArray) {
				rfidStatus.add(Integer.valueOf(info));
				if(info.equals("1")){//清洁状态选择把5-13塞进list中查询
					for(Integer i=5;i<=13;i++ ){
						rfidStatus.add(i);
					}
				}
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
		return washHandLogDao.RateCompare(queryEntity);
	}
	
	@Override
	public List<WashHandLog> SanitizerNum(QueryEntity queryEntity) {
		return washHandLogDao.SanitizerNum(queryEntity);
	}

	@Override
	public List<WashHandLog> EventCompare(QueryEntity queryEntity) {
		return washHandLogDao.EventCompare(queryEntity);
	}
    @Override
    public void save(WashHandLog info) {
        washHandLogDao.save(info);
        
    }
    @Override
    public void saveAll(List<WashHandLog> list) {
        washHandLogDao.saveAll(list);
    }
  //重复性报文的情况说明：涉及到胸牌转发的都是三次，ap直接上报（rfid=0）的一次：校时，心跳，ap编号编码修改上报、识别器的欠压欠压恢复
    @Override
    public void proWashHand(WashHandLog washHandLog) {
        int rfid=0,deviceNo=0;
        String sql="";
        List<Object[]> listObject;
        SignalLog log = new SignalLog();
        BeanUtils.copyProperties(washHandLog, log);
        if(washHandLog.getType()!=null){
            switch(Integer.parseInt(washHandLog.getType())){
            case 1000:  //洗手命令
                Object[] vs = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType(),washHandLog.getDeviceType(),washHandLog.getDeviceNo()};
                if(washHandLog.getEventType().equals("0004")){
                	sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM wash_hand_log where rfid=? and rfid_status=? and event_type=? and device_type=? and device_no=? order by id Desc limit 1)<interval '3 second'";
                    listObject= pageHibernateTemplate.createQueryBySql(sql, vs);
                    if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                        save(washHandLog);
                        signalLogDao.save(log);
                    }
                } else {
                	sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM wash_hand_log where rfid=? and rfid_status=? and event_type=? and device_type=? and device_no=? order by id Desc limit 1)<interval '20 second'";
                    listObject= pageHibernateTemplate.createQueryBySql(sql, vs);
                    if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                        save(washHandLog);
                        signalLogDao.save(log);
                    } 
                }
                break;
            case 2000://欠压报警
                rfid =Integer.parseInt(washHandLog.getRfid());
                deviceNo =Integer.parseInt(washHandLog.getDeviceNo());
                if(rfid==0){//识别器欠压--就一条
                    sql="Update device_info set device_status='2',update_time='"+washHandLog.getTimeStr()+"' Where type='"+washHandLog.getDeviceType()+"' and no='"+washHandLog.getDeviceNo()+"'";
                    pageHibernateTemplate.executeSql(sql); 
                    signalLogDao.save(log);
                }else if(deviceNo==0){//胸牌欠压
                    Object[] vs0 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                    sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '20 second'";
                    listObject= pageHibernateTemplate.createQueryBySql(sql, vs0);
                    if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                        sql="Update device_info set device_status='2',update_time='"+washHandLog.getTimeStr()+"' Where type='40' and no='"+washHandLog.getRfid()+"'";
                        pageHibernateTemplate.executeSql(sql); 
                        log.setDeviceType("40");  //是胸牌自己上报的
                        log.setDeviceNo(washHandLog.getRfid());
                        signalLogDao.save(log);
                    }
                }
                break;
            case 2001://欠压恢复
                rfid =Integer.parseInt(washHandLog.getRfid());
                deviceNo =Integer.parseInt(washHandLog.getDeviceNo());
                if(rfid==0){//设备器欠压恢复--就一条
                    sql="Update device_info set device_status='0',update_time='"+washHandLog.getTimeStr()+"' Where type='"+washHandLog.getDeviceType()+"' and no='"+washHandLog.getDeviceNo()+"'";
                    pageHibernateTemplate.executeSql(sql); 
                    signalLogDao.save(log);
                }else if(deviceNo==0){//胸牌欠压恢复
                    Object[] vs1 = {washHandLog.getRfid(),washHandLog.getRfidStatus(),washHandLog.getEventType()};
                    sql="select 1 as a where to_timestamp('"+ washHandLog.getTimeStr()+"', 'yyyy-mm-dd hh24:mi:ss')-(select create_time FROM signal_log where rfid=? and rfid_status=? and event_type=? order by id Desc limit 1)<interval '20 second'";
                    listObject= pageHibernateTemplate.createQueryBySql(sql, vs1);
                    if(listObject!=null&&listObject.size()==0){//大于时间间隔就插入 目前45S
                        sql="Update device_info set device_status='0',update_time='"+washHandLog.getTimeStr()+"' Where type='40' and no='"+washHandLog.getRfid()+"'";
                        pageHibernateTemplate.executeSql(sql);  
                        log.setDeviceType("40");  //是胸牌自己上报的
                        log.setDeviceNo(washHandLog.getRfid());
                        signalLogDao.save(log);
                    }                          
                } 
                break;
            case 3000: //校时命令
            	log.setDeviceType("38");  //是AP自己上报的
                log.setDeviceNo(log.getApNo());
                signalLogDao.save(log);
                break;
            case 4000://上报Ap编号--修改
                sql="Update ap_set set no='"+washHandLog.getApNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                pageHibernateTemplate.executeSql(sql);
              //  signalLogDao.save(log);
                break;
            case 4001://上报Ap编码--修改医院编号上报
                sql="Update ap_set set post_code='"+washHandLog.getPostCode()+"',hospital_no='"+washHandLog.getHospitalNo()+"',depart_no='"+washHandLog.getDepartmentNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                pageHibernateTemplate.executeSql(sql); 
               // signalLogDao.save(log);
                break;
            case 5000://心跳包---每分钟一次   
                sql="Update ap_set set no='"+washHandLog.getApNo()+"',update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
                pageHibernateTemplate.executeSql(sql);
                log.setDeviceType("38");  //是AP自己上报的
                log.setDeviceNo(log.getApNo());
                signalLogDao.save(log);
                break;
            case 6000://AP登录包  
            	ApSet ap =apSetDao.findUniqueBy("ip", washHandLog.getIp());
            	if(ap!=null&&ap.getIp()!=null){
            		sql="Update ap_set set ap_status=0,update_time='"+washHandLog.getTimeStr()+"' Where ip='"+washHandLog.getIp()+"'";
            		pageHibernateTemplate.executeSql(sql);
            	}else{
                	ApSet apSet = new ApSet();
                	apSet.setNo(washHandLog.getApNo());
                	apSet.setIp(washHandLog.getIp());
                	apSet.setUpdateTime(new Date());
                	apSet.setApStatus(0);
                	apSet.setStatus(0L);            
                	apSetDao.save(apSet);
                }
                break;
            }
        }
    }
    
	@Override
	public List<WashHandLog> findOutDoorEventOneDay(QueryEntity queryEntity, int pageNo, int pageSize) {
		PageInfo<WashHandLog> pageInfo = new PageInfo<WashHandLog>();
		List<WashHandLog> list = washHandLogDao.findOutDoorEventOneDay(queryEntity);
		List<WashHandLog> listByPage = pageInfo.listByPage(pageNo, pageSize, list);
		return listByPage;
	}
	
	@Override
	public List<Integer> findNumberOfOutDoorEventByDate(QueryEntity queryEntity) {
		List<Integer> counts = new ArrayList<Integer>();
		List<WashHandLog> washHandLogs = washHandLogDao.findOutDoorEventOneDay(queryEntity);
		Date startDate = queryEntity.getStartTime();
		//对开始时间取整
		String startStr = DateUtils.dateToString(startDate);
		//将String类型的时间转回Date
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			startDate = sf.parse(startStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = queryEntity.getEndTime();
		int days = DateUtils.daysOfTwo(startDate, endDate);
		//days+1是因为要显示多一天的
		for (int i = 0; i < days + 1; i++) {
			int count = 0;
			for (int j = 0; j < washHandLogs.size(); j++) {
				WashHandLog washHandLog = washHandLogs.get(j);
				//数据库中的事件时间
				Date dateEvent = washHandLog.getUpdateTime();
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(startDate);
			    //把日期往后增加i天.整数往后推,负数往前移动
			    calendar.add(calendar.DATE, i);
			    Date dateAddOneBefore = calendar.getTime();
			    calendar.add(calendar.DATE, 1); 
			    Date dateAddOne = calendar.getTime();
				if(dateEvent.before(dateAddOne) && dateEvent.after(dateAddOneBefore)){
					count++;
				}
			}
			counts.add(count);
		}
		return counts;
	}
	
	@Override
	public Map<String, List<Integer>> findFrequencyByTimeAndDepartment(
			QueryEntity queryEntity) {
		Map<String, List<Integer>> frequencyByStaff = new HashMap<String, List<Integer>>();
		Set<String> rfids = new HashSet<String>();
		//通过部门先把这个部门所有的人查出来
		Long departmentId = Long.parseLong(queryEntity.getStr1());
		List<DeviceInfo> deviceInfos = deviceInfoDao.findByDepartId(departmentId);
		if(deviceInfos == null || deviceInfos.size() == 0){
			return null;
		}
		List<Long> staffIds = new ArrayList<Long>();
		for (int i = 0; i < deviceInfos.size(); i++) {
			//将DeviceInfo中的staffId取出放到staffIds中
			staffIds.add(deviceInfos.get(i).getStaffId());
			rfids.add(deviceInfos.get(i).getNo());
		}
		List<String> rfidtemp = new ArrayList<String>();
		for (String str : rfids){
			rfidtemp.add(str);
		}
		queryEntity.setRfidList(rfidtemp);
		List<WashHandLog> washHandLogs = washHandLogDao.findByTimeAndDepartment(queryEntity);
		//暂时未用到，下次优化使用
		List<StaffInfo> staffInfos = staffInfoDao.findByDepartmentId(departmentId);
		for (int i = 0; i < washHandLogs.size(); i++) {
			rfids.add(washHandLogs.get(i).getRfid());
		}
		for (String rfid : rfids){
			Integer count = 0;
			//数据库查出来的手卫生事件集合中的RFID
			String RFID;
			//用于判断手卫生事件是
			int rfidStatus = 0;
			//科室下面员工的洗手总次数，不清洁，有限清洁，清洁的次数（注意按照顺序排列）
			List<Integer> allcounts = new ArrayList<Integer>();
			Integer notclean = 0, limitedclean = 0, clean = 0;
			WashHandLog washHandLog = new WashHandLog();
			for (int i = 0; i < washHandLogs.size(); i++){
				washHandLog = washHandLogs.get(i);
				RFID = washHandLog.getRfid();
				if (rfid.equals(RFID)) {
					count++;
					rfidStatus = washHandLog.getRfidStatus();
					//2表示有限清洁
					if (rfidStatus == 2){
						limitedclean++;
					} else if (rfidStatus == 1){//1表示清洁
						clean++;
					} else {//其余的都算不清洁
						notclean++;
					}
				}
			}
			allcounts.add(count);
			allcounts.add(notclean);
			allcounts.add(limitedclean);
			allcounts.add(clean);
			//未来可能优化：一次性查询出所有的staff对象，然后根据staff对象的id和上面staffInfos中遍历出来的相比较
			String name = staffInfoDao.findNameByRfid(rfid);
			frequencyByStaff.put(name, allcounts);
		}
		return frequencyByStaff;
	}
	
	@Override
	public List<WashHandLog> findByTimeAndDepartment(QueryEntity queryEntity) {
		//通过部门先把这个部门所有的人查出来(得到rfid)
		Long departmentId = Long.parseLong(queryEntity.getStr1());
		//这里查出来的设备是胸牌，设备类型为40
		List<DeviceInfo> deviceInfos = deviceInfoDao.findByDepartId(departmentId);
		if (deviceInfos == null || deviceInfos.size() == 0){
			return null;
		}
		List<String> rfids = new ArrayList<String>();
		for (int i = 0; i < deviceInfos.size(); i++) {
			//将DeviceInfo中的staffId取出放到staffIds中
			rfids.add(deviceInfos.get(i).getNo());
		}
		queryEntity.setRfidList(rfids);
		List<WashHandLog> washHandLogs = washHandLogDao.findByTimeAndDepartment(queryEntity);
		return washHandLogs;
	}
	
	@Override
	public List<WashHandLog> findByTimeAndRfids(Date startTime, Date endTime,
			List<String> rfids) {
		List<WashHandLog> washHandLogs = new ArrayList<WashHandLog>();
		washHandLogs = washHandLogDao.findByTimeAndRfids(startTime, endTime, rfids);
		return washHandLogs;
	}
	
	@Override
	public List<WashHandLog> findLogsOfClosingPatients(QueryEntity queryEntity) {
		List<WashHandLog> washHandLogs = new ArrayList<WashHandLog>();
		washHandLogs = washHandLogDao.findLogsOfClosingPatients(queryEntity);
		return washHandLogs;
	}
	
	@Override
	public int findTimesOfClosePatientAfterContactWithPatient(
			QueryEntity queryEntity) {
		int times = washHandLogDao.findClosePatientAfterContactWithPatient(queryEntity).size();
		return times;
	}
	
	@Override
	public int findLogsNotWashHangBeforeClosingPatient(QueryEntity queryEntity) {
		int times = washHandLogDao.findLogsNotWashHangBeforeClosingPatient(queryEntity).size();
		return times;
	}
	
	@Override
	public List<WashHandLog> findWashHandLogsByTime(QueryEntity queryEntity) {
		List<WashHandLog> list = washHandLogDao.findWashHandLogsByTime(queryEntity);
		return list;
	}
	
	@Override
	public List<Integer> findNumberByDate(QueryEntity queryEntity) {
		List<Integer> counts = new ArrayList<Integer>();
		List<WashHandLog> washHandLogs = washHandLogDao.findWashHandLogsByTime(queryEntity);
		Date startDate = queryEntity.getStartTime();
		//对开始时间取整
		String startStr = DateUtils.dateToString(startDate);
		//将String类型的时间转回Date
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			startDate = sf.parse(startStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = queryEntity.getEndTime();
		int days = DateUtils.daysOfTwo(startDate, endDate);
		//days+1是因为要显示多一天的
		for (int i = 0; i < days + 1; i++) {
			int count = 0;
			for (int j = 0; j < washHandLogs.size(); j++) {
				WashHandLog washHandLog = washHandLogs.get(j);
				//数据库中的事件时间
				Date dateEvent = washHandLog.getUpdateTime();
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(startDate);
			    //把日期往后增加i天.整数往后推,负数往前移动
			    calendar.add(calendar.DATE, i);
			    Date dateAddOneBefore = calendar.getTime();
			    calendar.add(calendar.DATE, 1); 
			    Date dateAddOne = calendar.getTime();
				if(dateEvent.before(dateAddOne) && dateEvent.after(dateAddOneBefore)){
					count++;
				}
			}
			counts.add(count);
		}
		return counts;
	}
	
	@Override
	public List<Integer> findNumberByDateAndDepartmentId(QueryEntity queryEntity) {
		List<Integer> counts = new ArrayList<Integer>();
		List<WashHandLog> washHandLogs = washHandLogDao.findByTimeAndDepartment(queryEntity);
		Date startDate = queryEntity.getStartTime();
		//对开始时间取整
		String startStr = DateUtils.dateToString(startDate);
		//将String类型的时间转回Date
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			startDate = sf.parse(startStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = queryEntity.getEndTime();
		int days = DateUtils.daysOfTwo(startDate, endDate);
		//days+1是因为要显示多一天的
		for (int i = 0; i < days + 1; i++) {
			int count = 0;
			for (int j = 0; j < washHandLogs.size(); j++) {
				WashHandLog washHandLog = washHandLogs.get(j);
				//数据库中的事件时间
				Date dateEvent = washHandLog.getUpdateTime();
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(startDate);
			    //把日期往后增加i天.整数往后推,负数往前移动
			    calendar.add(calendar.DATE, i);
			    Date dateAddOneBefore = calendar.getTime();
			    calendar.add(calendar.DATE, 1); 
			    Date dateAddOne = calendar.getTime();
				if(dateEvent.before(dateAddOne) && dateEvent.after(dateAddOneBefore)){
					count++;
				}
			}
			counts.add(count);
		}
		return counts;
	}
	
}
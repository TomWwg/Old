package com.sws.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import com.sws.common.AjaxUtil;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.entity.EventCompare;
import com.sws.common.entity.StaffEntity;
import com.sws.common.entity.StaffWork;
import com.sws.common.entity.TypeRateEntity;
import com.sws.common.entity.WashHandMoment;
import com.sws.common.entity.WashTimes;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.DateUtils;
import com.sws.common.until.StringUtil;
import com.sws.dao.GroupTreeDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;
import com.sws.model.StaffInfo;
import com.sws.model.WashHandLog;
import com.sws.service.ManualRecordService;
import com.sws.service.StaffInfoService;
import com.sws.service.WashHandLogService;
import com.sys.core.util.JsonUtils;
import com.sys.core.util.bean.Page;

public class WashHandLogAction extends BaseAction<WashHandLog> {
	
	private static final long serialVersionUID = -2868312613376378663L;
	@Autowired
    private GroupTreeDao groupTreeDao;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private WashHandLogService washHandLogService;
	@Autowired
	private ManualRecordService manualRecordService;
	
	private List<WashHandLog> washHandList = new ArrayList<WashHandLog>();
	private Map<String,Integer> mapRight = new HashMap<String,Integer>();//正常----或者接触前
	private Map<String,Integer> mapWrong = new HashMap<String,Integer>();//违规----或者接触后
	private List<StaffEntity> staffEntityList = new ArrayList<StaffEntity>();
	private List<EventCompare>	eventCompareList = new ArrayList<EventCompare>();//事件比较
	private List<TypeRateEntity> typeRateEntityList = new ArrayList<TypeRateEntity>();
	private String staffName;
	private String jobType;
	private String washHandStatus;
	
	public Boolean hideControllerDepart;
	
	public Boolean getHideControllerDepart() {
		return hideControllerDepart;
	}
	public void setHideControllerDepart(Boolean hideControllerDepart) {
		this.hideControllerDepart = hideControllerDepart;
	}
	
	public void findFrequencyByStaffPro(){
		Double single = Double.parseDouble(parameterInfoDao.findByTypeAndKey(3, "single").getValue());
		if(StringUtil.isNotBlank(queryEntity.getTreeId())){
			queryEntity.setStr1(queryEntity.getTreeId());
		}
		Map<String, List<Integer>> frequencyByStaff = washHandLogService.findFrequencyByTimeAndDepartment(queryEntity);
		List<WashTimes> waList = new ArrayList<WashTimes>();
		if(frequencyByStaff == null){
			JSONObject obj = new JSONObject();
			obj.put("dataList", "");
			writeResponse(obj.toString());
		} else {
			for (String key : frequencyByStaff.keySet()) {
				WashTimes washTimes = new WashTimes();
				washTimes.setDocName(key.toString());
				List<Integer> value = frequencyByStaff.get(key);
				/**
				 * 0表示总次数；1表示不清洁的次数；2表示有限清洁的次数；3表示有效清洁的次数
				 */
				Double totalLiquid = value.get(0) * single;
				Double notcleanLiquid = value.get(1) * single;
				Double limitcleanLiquid = value.get(2) * single;
				Double cleanLiquid = value.get(3) * single;
				washTimes.setTotalWashTimes(totalLiquid.toString());
				washTimes.setNotcleanTimes(notcleanLiquid.toString());
				washTimes.setLimitcleanTimes(limitcleanLiquid.toString());
				washTimes.setCleanTimes(cleanLiquid.toString());
				waList.add(washTimes);
			}
			JSONObject obj = new JSONObject();
			obj.put("dataList", waList);
			writeResponse(obj.toString());
		}
		
	}
	
	/**
	 * 一定时间段内该科室下的不同人员类别的手卫生次数统计
	 * @return
	 */
	public String washTimesOfWashHandLineDrawing(){
		return "washTimesOfWashHandLineDrawing";
	}
	
	public void findWashTimesLineDrawing(){
		Double single = Double.parseDouble(parameterInfoDao.findByTypeAndKey(3, "single").getValue());
		Date startDate = DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		Map<String, Integer> dataMap = new HashMap<String, Integer>();
		Map<String, String> staffTypes = getJobTypeMap();
		List<StaffInfo> staffInfos = null;
		for(String key : staffTypes.keySet()){
			List<Long> staffIds = new ArrayList<Long>();
			staffInfos = staffInfoDao.findByDepartmentIdAndStaffType(key, Long.parseLong(depart));
			for(int i = 0; i < staffInfos.size(); i++){
				staffIds.add(staffInfos.get(i).getId());
			}
			List<DeviceInfo> deviceInfos = null;
			if(staffIds != null && staffIds.size() > 0){
				deviceInfos = deviceInfoDao.findByStaffId(staffIds);
			}
			List<String> rfids = new ArrayList<String>();
			Integer number = 0;
			if(deviceInfos != null){
				for(int i = 0; i < deviceInfos.size(); i++){
					rfids.add(deviceInfos.get(i).getNo());
				}
				if(rfids != null && rfids.size() > 0){
					number = washHandLogService.findByTimeAndRfids(startDate, endDate, rfids).size();
				}
			}
			dataMap.put(staffTypes.get(key), number);
		}
		JSONObject obj = new JSONObject();
		obj.put("dataMap", dataMap);
		obj.put("single", single);
		writeResponse(obj.toString());
	}

	/**
	 * 洗手液用量统计
	 * 通过时间段、部门、单次洗手液量统计
	 */
	public String washTimesStatistic_pro(){
		return "washTimesStatistic_pro";
	}
	
	public String washTimesStatistic(){
		return "washTimesStatistic";
	}
	public void findLiquidByTimeAndDepart(){
		Double single = Double.parseDouble(parameterInfoDao.findByTypeAndKey(3, "single").getValue());
		int capacity = Integer.parseInt(parameterInfoDao.findByTypeAndKey(3, "capacity").getValue());
		Double total = 0d;
		Date startDate = DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		List<WashHandLog> washHandLogs = washHandLogService.findByTimeAndDepartment(queryEntity);
		Integer count;
		if (washHandLogs == null){
			count = 0;
		} else {
			count = washHandLogs.size();
		}
		total = single * count;
		Double number = new BigDecimal(total/capacity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		JSONObject obj = new JSONObject();
		obj.put("dataList", count);
		obj.put("single", single);
		obj.put("capacity", capacity);
		obj.put("total", total);
		obj.put("number", number);
		writeResponse(obj.toString());
	}
	
	/**
	 * 科室人员手卫生次数统计
	 * 通过科室，开始时间，结束时间查询该科室在该时间段内人员的手卫生次数信息
	 */
	public void findFrequencyByStaff(){
		if(StringUtil.isNotBlank(queryEntity.getTreeId())){
			queryEntity.setStr1(queryEntity.getTreeId());
		}
		Map<String, List<Integer>> frequencyByStaff = washHandLogService.findFrequencyByTimeAndDepartment(queryEntity);
		List<WashTimes> waList = new ArrayList<WashTimes>();
		JSONObject obj = new JSONObject();
		if(frequencyByStaff == null){
			obj.put("dataList", "");
		} else {
			for (String key : frequencyByStaff.keySet()) {
				WashTimes washTimes = new WashTimes();
				washTimes.setDocName(key.toString());
				List<Integer> value = frequencyByStaff.get(key);
				/**
				 * 0表示总次数；1表示不清洁的次数；2表示有限清洁的次数；3表示有效清洁的次数
				 */
				washTimes.setTotalWashTimes(value.get(0).toString());
				washTimes.setNotcleanTimes(value.get(1).toString());
				washTimes.setLimitcleanTimes(value.get(2).toString());
				washTimes.setCleanTimes(value.get(3).toString());
				waList.add(washTimes);
			}
			Collections.sort(waList, new Comparator<WashTimes>() {
				@Override
				public int compare(WashTimes o1, WashTimes o2) {
					return (Integer.parseInt(o2.getTotalWashTimes()) - Integer.parseInt(o1.getTotalWashTimes()));
				}
	        });
			for(int i = 0,length = waList.size(); i < length; i++) {
				waList.get(i).setRank(i+1);
			}
			obj.put("dataList", waList);
		}
		writeResponse(obj.toString());}
	
	private void mapProcess() {
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null) {
				if (wash.getEventType().equals("0003")) { // 有效洗手数
					/**
					 * 修改，从有效手卫生中删除rfidStatus为2，即清洁状态下的手卫生
					 * 删除的代码：
					 * wash.getRfidStatus().equals(2)||
					 * update by wwg 2018/1/17
					 */
					if(wash.getRfidStatus().equals(2)||wash.getRfidStatus().equals(3)||wash.getRfidStatus().equals(6)||wash.getRfidStatus().equals(9)||
							wash.getRfidStatus().equals(10)||wash.getRfidStatus().equals(12)){
						if (mapRight.get(wash.getRfid()) == null) {
							mapRight.put(wash.getRfid(), 1);
						} else {
							mapRight.put(wash.getRfid(),mapRight.get(wash.getRfid()) + 1);// 只会更新value的值
						}
					}
				} else {
					if (mapWrong.get(wash.getRfid()) == null) {
						mapWrong.put(wash.getRfid(), 1);
					} else {
						mapWrong.put(wash.getRfid(),mapWrong.get(wash.getRfid()) + 1);// 只会更新value的值
					}
				}
			}
		}
	}
	
	/****************************************************
	 * 尝试添加今日事件
	 * adder:yxnne
	 */
	public String todayEventList(){
		ActionContext.getContext().put("eventTypeMap", SysStatics.eventTypeMap);
		Map<String, String> typeMap = getJobTypeMap();
		ActionContext.getContext().put("jobTypeMap", typeMap);
		String typeStr="";
		for (Map.Entry<String,String> entry : typeMap.entrySet()) {
			typeStr+=entry.getKey()+",";
		}
		queryEntity.setStr3(typeStr);
		ActionContext.getContext().put("washHandStatusMap", SysStatics.washHandStatusMap);
		return "todayEventList";
	}
	
	/***********************************事件查询(类型、标签）******************************/
	/**
	 * 进入列表
	 * 
	 */
	public String eventList() {
		ActionContext.getContext().put("eventTypeMap", SysStatics.eventTypeMap);
		Map<String, String> typeMap = getJobTypeMap();
		ActionContext.getContext().put("jobTypeMap", typeMap);
		String typeStr="";
		for (Map.Entry<String,String> entry : typeMap.entrySet()) {
			typeStr+=entry.getKey()+",";
		}
		queryEntity.setStr3(typeStr);
		ActionContext.getContext().put("washHandStatusMap", SysStatics.washHandStatusMap);
		return "eventList";
	}
	
	/**
	 * 列表显示
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void eventListAjax() {
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<WashHandLog> logLists;
		List<String> typeList = new ArrayList<String>();
		if(StringUtil.isNotBlank(jobType)){
			String[] staffTypeArray = jobType.split(",");//人员类别名称
			for(String staffType:staffTypeArray){
				typeList.add(staffType);
			}
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				staffList= staffInfoDao.findByNameAndTypeAndDLevel(getManageDepart(),getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()), staffName, typeList);
			}
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		//人员类别必须要选择，否则无数据
		queryEntity.setRfidList(rfidList);
		if(StringUtils.isNotBlank(washHandStatus)){
			queryEntity.setStr2(washHandStatus);//手卫生状态带进查询条件
		}
		if(rfidList.size()>0){
			page = washHandLogService.pageByEvent(queryEntity, _page, rowNum, sortname,sortorder);
			logLists=(List<WashHandLog>)page.getResult();
			for(WashHandLog log:logLists){
				if(log.getRfid()!=null){
					StaffInfo staffInfo=getStaff(log.getRfid());
					if(null!=staffInfo){
						log.setDocName(staffInfo.getName());
						//log.setStaffTypeName(SysStatics.jobTypeMap.get(staffInfo.getCategory()));
						log.setStaffTypeName(getJobTypeMap().get(staffInfo.getCategory()));
						GroupTree groupTree=groupTreeService.getById(staffInfo.getGroupTree().getId());
						if(null!=groupTree){
							log.setDepartName(groupTree.getName());
						}
						log.setSex(staffInfo.getSex());
					}
					log.setDocName(staffInfo.getName());
					log.setStaffTypeName(getJobTypeMap().get(staffInfo.getCategory()));
				}
				if(log.getRfidStatus()!=null){
					Integer rfidStatus=log.getRfidStatus();
					if(rfidStatus==1||rfidStatus==2||rfidStatus==3||rfidStatus==4){//手卫生状态
						log.setRfidStatusName(SysStatics.rfidStatusMap.get(log.getRfidStatus().toString()));
					}else{//洗手时机
						log.setRemark(SysStatics.rfidStatusMap.get(log.getRfidStatus().toString()));
						//log.setRfidStatus(1);
						log.setRfidStatusName("清洁");
					}
				}
				if(log.getEventType()!=null&&log.getRfidStatus()!=null){
					if(log.getDeviceNo()!=null&&log.getDeviceType()!=null){
						//log.setRemark(log.getDeviceNo()+SysStatics.deviceTypeMap.get(log.getDeviceType()));
						log.setDeviceInfo(SysStatics.deviceTypeMap.get(log.getDeviceType())+"-"+log.getDeviceNo());
						log.setLocation(getDeviceLocation(log));		
					}
					if(SysStatics.eventTypeMap.get(log.getEventType()).equals("")){
						log.setEventTypeName(log.getEventType());
					}else{
						log.setEventTypeName(SysStatics.eventTypeMap.get(log.getEventType()));
					}
				}
			}	
			page = new Page(_page, rowNum, logLists,  page.getTotal());
		}else{
			page = new Page(0, rowNum, null,  0);
		}
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	private String getDeviceLocation(WashHandLog log){
		String deviceLocation="";
		DeviceInfo device=null;
		if(log!=null){
			 device=getDeviceInfoByNoType(log.getDeviceNo(), log.getDeviceType());
			 if(device!=null){
					if(device.getDepartId()!=null){
						deviceLocation+=getGroupTreeNameByID(device.getDepartId());
					}
					if(device.getRoomId()!=null){
						deviceLocation+="-"+getGroupTreeNameByID(device.getRoomId());
					}
					if(device.getBedId()!=null){
						deviceLocation+="-"+getGroupTreeNameByID(device.getBedId());
					}
				}else{
					deviceLocation="该设备尚未添加";
				}
		}
		return deviceLocation;
	}
	
	/***********************************人员依从率查询******************************/
	/**
	 * 进入列表
	 * 
	 */
	public String rateByStaffList() {
		String typeStr="";
		Map<String, String> typeMap = getJobTypeMap();
		ActionContext.getContext().put("jobTypeMap", typeMap);
		for (Map.Entry<String,String> entry : typeMap.entrySet()) {
			typeStr+=entry.getKey()+",";
		}
		queryEntity.setStr1(typeStr);
		return "rateByStaffList";
	}
	/**
	 * 列表显示
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void rateByStaffListAjax() {
		String rfid;
		StaffWork staffWork = null;
		GroupTree departTree = null;
		List<StaffWork> list = new ArrayList<StaffWork>();
		List<StaffInfo>	staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<Long> staffIds = new ArrayList<Long>();
		Map<String, String> typeMap = getJobTypeMap();
		List<String> typeList = new ArrayList<String>();
		if(queryEntity.getStr1() != null && !queryEntity.getStr1().equals("")){
			String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String staffType : staffTypeArray){
				typeList.add(staffType);
			}
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				//staffList= staffInfoDao.findByDepartAndTypeAndName(getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList,staffName);
				staffList= staffInfoDao.findByDepartAndTypeAndNameAndDLevel(getManageDepart(),getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList,staffName);
			}
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
				staffIds.add(info.getId());
			}
			queryEntity.setRfidList(rfidList);
		}
		
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
			mapProcess();//map数据封装
		}
		if(staffIds.size()>0){
			page = staffInfoService.page(staffIds, _page, rowNum, sortname,sortorder);
			for(StaffInfo staff : (List<StaffInfo>) page.getResult()){
				staffWork = new StaffWork();
				rfid=getRfidByStaffId(staff.getId());
				if (staff.getName() != null) {
					staffWork.setDocName(staff.getName());
				}
				if(staff.getCategory()!=null){
					staffWork.setDocType(typeMap.get(staff.getCategory()));
				}
				if(staff.getGroupTree().getId()!=null){
					departTree = groupTreeService.getById(staff.getGroupTree().getId());
					if(departTree!=null&&departTree.getName()!=null){
						staffWork.setDepartName(departTree.getName());
					}
				}
				staffWork.setRfid(rfid);
				if(mapRight.get(rfid)!=null){
					staffWork.setNormalNum(mapRight.get(rfid));
				}else{
					staffWork.setNormalNum(0);
				}
				if(mapWrong.get(rfid)!=null){
					staffWork.setErrorNum(mapWrong.get(rfid));
				}else{
					staffWork.setErrorNum(0);
				}
				staffWork.setRate(getPercent(staffWork.getNormalNum(),staffWork.getNormalNum()+staffWork.getErrorNum()));
				list.add(staffWork);
			}
			page = new Page(_page, rowNum, list, page.getTotal());
		}else{
			page = new Page(0, rowNum, null,  0);
		}
		
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	/***********************************人员类别依从率查询******************************/
	private void rateByStaffType(){
		String rfidStr;
		StaffEntity staffEntity=null;
		Integer ringtCount=null,wrongCount=null;
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		Map<String, String> typeMap = getJobTypeMap();
		if(StringUtil.isNotBlank(queryEntity.getStr1())){
			String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String staffType:staffTypeArray){
				typeList.add(staffType);
			}
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				//staffList= staffInfoDao.findByDepartAndType(getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
				staffList= staffInfoDao.findByDepartAndTypeAndDlevel(getManageDepart(),getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
			}
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
		}
		mapProcess();//map数据封装
		for(String staffType:typeList){
			staffEntity = new StaffEntity();
			staffEntity.setName(typeMap.get(staffType));//人员类别名称
			for(StaffInfo staff:staffList){
				if(staff.getCategory()!=null&&staff.getCategory().equals(staffType)){
					staffEntity.setPerpleNum(staffEntity.getPerpleNum()+1);
					rfidStr=getRfidByStaffId(staff.getId());
					if(!rfidStr.equals("")){
						ringtCount=mapRight.get(rfidStr);
						wrongCount=mapWrong.get(rfidStr);
						if(ringtCount==null){
							ringtCount=0;
						}
						if(wrongCount==null){
							wrongCount=0;
						}
						staffEntity.setRightCount(staffEntity.getRightCount()+ringtCount);
						staffEntity.setWrongCount(staffEntity.getWrongCount()+wrongCount);
					}
				}
			}
			staffEntity.setRate(getPercent(staffEntity.getRightCount(),staffEntity.getRightCount()+staffEntity.getWrongCount()));
			staffEntity.setType(staffEntity.getName() + staffEntity.getRate()+"%");
			staffEntityList.add(staffEntity);
		}
	}
	
	/**
	 * 进入列表
	 * 
	 */
	public String rateByStaffTypeList() {
		String typeStr="";
		Map<String, String> typeMap = getJobTypeMap();
		ActionContext.getContext().put("jobTypeMap", typeMap);
		for (Map.Entry<String,String> entry : typeMap.entrySet()) {
			typeStr+=entry.getKey()+",";
		}
		queryEntity.setStr1(typeStr);
		return "rateByStaffTypeList";
	}
	
	/**
	 * 列表显示
	 * @return
	 */
	public void rateByStaffTypeListAjax() {
		rateByStaffType();
		page = new Page(_page, rowNum, staffEntityList, staffEntityList.size());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	/**
	 * 图表显示
	 * @return
	 */
	public void rateChartByStaffType() {
		if(StringUtil.isNotBlank(treeId)){
			queryEntity.setTreeId(treeId);
		}
		if(StringUtil.isNotBlank(staffType)){
			queryEntity.setStr1(staffType);
		}
		if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
			queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
		}
		rateByStaffType();
		JSONObject obj = new JSONObject();
		obj.put("dataList", staffEntityList);
		writeResponse(obj.toString());
	}	
	
	/***********************************外门统计模块********************************/
	private int dayCounts;
	public int getDayCounts() {
		return dayCounts;
	}
	
	public void setDayCounts(int dayCounts) {
		this.dayCounts = dayCounts;
	}
	
	public String eventOutDoorStatistic() {
		String departStr="";
		List<GroupTree>	departList = getPartList();
		ActionContext.getContext().put("departList", departList);
		for(GroupTree depart:departList){
			departStr+=depart.getId()+",";
		}
		queryEntity.setStr1(departStr);
		return "eventOutDoorStatistic";
	}
	
	/**事件列表：响应请求的方法*/
	public void outDoorEventList(){
		//查询时间确定
		Date startDate =  DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		List<WashHandLog> washHandLogs = washHandLogService.findOutDoorEventOneDay(queryEntity, _page, rowNum);
		for (int i = 0; i < washHandLogs.size(); i++) {
			WashHandLog washHandLog = washHandLogs.get(i);
			String deviceType = washHandLog.getDeviceType();
			washHandLog.setDeviceType(SysStatics.deviceTypeMap.get(deviceType));
			String eventType = washHandLog.getEventType();
			washHandLog.setEventType(SysStatics.eventTypeMap.get(eventType));
			washHandLog.setDeviceNo(washHandLog.getDeviceNo());
			Date date = washHandLog.getCreateTime();
			if(date != null){
				String updateTimeString = dateToString(date);
				washHandLog.setUpdateTimeString(updateTimeString);
			}
		}
		JSONObject obj = new JSONObject();
		obj.put("dataList", washHandLogs);
		//序号从一开始
		obj.put("pageNo", 1);
		writeResponse(obj.toString());
	}
	
	/**
	 * 针对折线图（大门外手卫生次数）
	 */
	public void outDoorTimesAsDaysList() {

		Date startDate =  DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		List<Integer> list = washHandLogService.findNumberOfOutDoorEventByDate(queryEntity);
		//返回结果
		JSONObject obj = new JSONObject();
		obj.put("dataList", list);
		writeResponse(obj.toString());
	}
	
	
	/***********************************科室依从率查询******************************/
	private void rateByDepart() {
		String rfidStr,rate;
		GroupTree departTree =null;
		TypeRateEntity typeRateEntity=null;
		Integer rightCount=null,wrongCount=null,pepleNum=null,rightSum=0,wrongSum=0;
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<Long> departList = new ArrayList<Long>();
		Map<String, String> typeMap = getJobTypeMap();
		Map<String,Integer> mapTypeRight = null;//正常----或者接触前
		Map<String,Integer> mapTypeWrong = null;//违规----或者接触后
		Map<String,Integer> mapPeple = null; //部门下面的人员数目
		if(StringUtil.isNotBlank(queryEntity.getStr1())){   //科室
			String[] departArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String depart:departArray){
				departList.add(Long.valueOf(depart));
			}
			staffList= staffInfoDao.findAllEntitysBy("groupTree.id", departList);
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
		}
		mapProcess();//map数据
		for(Long departId:departList){
			mapTypeRight = new TreeMap<String,Integer>();
			mapTypeWrong = new TreeMap<String,Integer>();
			mapPeple = new TreeMap<String,Integer>();//初始化map值
			typeRateEntity = new TypeRateEntity();
			departTree=groupTreeService.getById(departId);
			if(departTree!=null){
				typeRateEntity.setDepart(departTree.getName());
			}
			staffList= staffInfoDao.findBy("groupTree.id", departId);
			for(StaffInfo staff:staffList){
				if(staff.getCategory()!=null){
					rfidStr = getRfidByStaffId(staff.getId());
					if(!rfidStr.equals("")){
						rightCount=mapRight.get(rfidStr);
						wrongCount=mapWrong.get(rfidStr);
						if(rightCount==null){
							rightCount=0;
						}
						if(wrongCount==null){
							wrongCount=0;
						}
						if (mapTypeRight.get(staff.getCategory()) == null) {
							mapTypeRight.put(staff.getCategory(), rightCount);
						}else{
							mapTypeRight.put(staff.getCategory(),mapTypeRight.get(staff.getCategory()) + rightCount);// 只会更新value的值
						}
						if (mapTypeWrong.get(staff.getCategory()) == null) {
							mapTypeWrong.put(staff.getCategory(), wrongCount);
						}else{
							mapTypeWrong.put(staff.getCategory(),mapTypeWrong.get(staff.getCategory()) + wrongCount);// 只会更新value的值
						}
						if (mapPeple.get(staff.getCategory()) == null) {
							mapPeple.put(staff.getCategory(), 1);
						} else {
							mapPeple.put(staff.getCategory(),mapPeple.get(staff.getCategory()) + 1);// 只会更新value的值
						}
					}
				}
			}
			rightSum=0;
			wrongSum=0;
			for (Map.Entry<String,String> entry : typeMap.entrySet()) {
				pepleNum=mapPeple.get(entry.getKey());
				rightCount=mapTypeRight.get(entry.getKey());
				wrongCount=mapTypeWrong.get(entry.getKey());
				if(pepleNum==null){
					pepleNum=0;
				}
				if(rightCount==null){
					rightCount=0;
				}
				if(wrongCount==null){
					wrongCount=0;
				}
				rate=getPercent(rightCount,rightCount+wrongCount);
				rightSum+=rightCount;
				wrongSum+=wrongCount;
				typeRateEntity.getStaffCountList().add(pepleNum);//人数
				typeRateEntity.getRightCountList().add(rightCount);
				typeRateEntity.getWrongCountList().add(wrongCount);
				typeRateEntity.getRateList().add("人数:"+pepleNum+"个,正常:"+rightCount+"次,依从率:"+rate+"%");
				typeRateEntity.getNameList().add(typeMap.get(entry.getKey()));
			}
			typeRateEntity.setRightCount(rightSum);
			typeRateEntity.setWrongCount(wrongSum);
			typeRateEntity.setRate(typeRateEntity.getDepart() + getPercent(rightSum,rightSum+wrongSum) + "%");
			typeRateEntityList.add(typeRateEntity);
		}
	}
	/**
	 * 进入列表
	 * 
	 */
	public String rateByDepartList() {
		String departStr="";
		List<GroupTree>	departList = getPartList();
		ActionContext.getContext().put("departList", departList);
		for(GroupTree depart:departList){
			departStr+=depart.getId()+",";
		}
		queryEntity.setStr1(departStr);
		return "rateByDepartList";
	}
	
	/**
	 * 列表、图表显示
	 * @return
	 */
	public void rateChartByDepart() {
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
			queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
		}
		rateByDepart();
		JSONObject obj = new JSONObject();
		obj.put("dataList", typeRateEntityList);
		writeResponse(obj.toString());
	}

	/***********************************事件比较查询(接触前、接触后)******************************/
	private void eventCompare(){
		String rfidStr;
		GroupTree departTree =null;
		EventCompare eventCompare = null;
		Integer evenBefore=null,evenAfter=null;
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<Long> departList = new ArrayList<Long>();
		if(StringUtil.isNotBlank(queryEntity.getStr1())){   //科室
			String[] departArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String depart:departArray){
				departList.add(Long.valueOf(depart));
			}
			staffList= staffInfoDao.findAllEntitysBy("groupTree.id", departList);
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			washHandList = washHandLogService.EventCompare(queryEntity);
		}
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null) {
				if (wash.getRfidStatus().equals(4)|| wash.getRfidStatus().equals(5)|| wash.getRfidStatus().equals(10)) { // 接触前
					if (mapRight.get(wash.getRfid()) == null) {
						mapRight.put(wash.getRfid(), 1);
					} else {
						mapRight.put(wash.getRfid(),mapRight.get(wash.getRfid()) + 1);// 只会更新value的值
					}
				} else {
					if (mapWrong.get(wash.getRfid()) == null) {
						mapWrong.put(wash.getRfid(), 1);
					} else {
						mapWrong.put(wash.getRfid(),mapWrong.get(wash.getRfid()) + 1);// 只会更新value的值
					}
				}
			}
		}
		for(Long departId:departList){
			eventCompare = new EventCompare();
			departTree=groupTreeService.getById(departId);
			if(departTree!=null){
				eventCompare.setName(departTree.getName());
			}
			staffList= staffInfoDao.findBy("groupTree.id", departId);
			for(StaffInfo staff:staffList){
				rfidStr=getRfidByStaffId(staff.getId());
				evenBefore=mapRight.get(rfidStr);
				evenAfter=mapWrong.get(rfidStr);
				if(evenBefore==null){
					evenBefore=0;
				}
				if(evenAfter==null){
					evenAfter=0;
				}
				eventCompare.setEvenBefore(eventCompare.getEvenBefore()+evenBefore);
				eventCompare.setEvenAfter(eventCompare.getEvenAfter()+evenAfter);
			}
			eventCompare.setRate(getPercent(eventCompare.getEvenBefore(),eventCompare.getEvenBefore()+eventCompare.getEvenAfter()));
			eventCompare.setType(eventCompare.getName()+":比例"+eventCompare.getRate()+"%");
			eventCompareList.add(eventCompare);
		}
	}
	private List<GroupTree> getPartList(){
		String departIds = getCommSession().getOrg();
		String[] array = departIds.split("\\*");
		List<GroupTree>	departList = new ArrayList<GroupTree>();
		if(getManageDepart().contains(departIds)){//管理部门
			departList = groupTreeService.getDepart();
		}else{
			List<Long> ids = new ArrayList<Long>();
			for(String dId:array){
				ids.add(Long.valueOf(dId));
			}
			departList = groupTreeDao.findAllEntitysByIds(ids);
		}
		return departList;
	}
	/**
	 * 进入列表
	 * 
	 */
	public String eventCompareList() {
		String departStr="";
		List<GroupTree>	departList = getPartList();
		ActionContext.getContext().put("departList", departList);
		for(GroupTree depart:departList){
			departStr+=depart.getId()+",";
		}
		queryEntity.setStr1(departStr);
		return "eventCompareList";
	}
	/**
	 * 列表显示
	 * @return
	 */
	public void eventCompareListAjax() {
		eventCompare();
		page = new Page(_page, rowNum, eventCompareList, eventCompareList.size());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
	/**
	 * 图表显示
	 * @return
	 */
	public void eventCompareChart() {
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
			queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
		}
		eventCompare();
		//图标显示
		JSONObject obj = new JSONObject();
		obj.put("dataList", eventCompareList);
		writeResponse(obj.toString());
	}
	
	//private List<WashHandLog> washHandList = new ArrayList<WashHandLog>();
	private List<WashHandLog> washHandMomentList = new ArrayList<WashHandLog>();//洗手时机统计用数据
	
	/**
	 * add by yxnne 2017 - 06 - 06
	 * 原来的事件比较 基本上已经废弃了，现在是洗手时机，核心计算方法和loginAction当中的保持一致
	 */
	//洗手时机列表，里面存放的对象是一个部门的洗手时机entity
	private List<WashHandMoment> washHandMomentCompareList = new ArrayList<WashHandMoment>();
	
	/**
	 * 手卫生时机依从率（执行手卫生时机统计）
	 * 前端的请求方法 for Chart
	 */
	public void eventCompareChart2() {
		//计算手卫生时机
		eventWashTimeCalc(hideControllerDepart);
		//返回结果
		JSONObject obj = new JSONObject();
		obj.put("dataList", washHandMomentCompareList);
		writeResponse(obj.toString());
	}
	//前端的请求方法 for Grid
	public void eventCompareListAjax2() {
		//计算手卫生时机
		eventWashTimeCalc(hideControllerDepart);
		//返回结果
		page = new Page(_page, rowNum, washHandMomentCompareList, washHandMomentCompareList.size());
		AjaxUtil.ajaxWrite(JsonUtils.object2Json(page));
	}
	
		
	/**
	 * 手卫生时机计算
	 */
	private void eventWashTimeCalc(Boolean isNeedHideControllerDepart) {
		washHandMomentCompareList = new ArrayList<WashHandMoment>();
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
			queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
		}
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<Long> departList = new ArrayList<Long>();
		//得到科室
		if(StringUtil.isNotBlank(queryEntity.getStr1())){   //科室
			String[] departArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String depart:departArray){
				departList.add(Long.valueOf(depart));
			}
			staffList= staffInfoDao.findAllEntitysBy("groupTree.id", departList);
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		//closePatientAfterContactWithPatient:接触患者后的分母中，接近患者次数（清洁|有限清洁状态）
		int cpiacwp = washHandLogService.findTimesOfClosePatientAfterContactWithPatient(queryEntity);
		washHandList = washHandLogService.RateCompare(queryEntity);//查询数据
		//closingPatientNotWashHand接近患者未手卫生
		int cpnw = washHandLogService.findLogsNotWashHangBeforeClosingPatient(queryEntity);
		washHandMomentList=washHandLogService.EventCompare(queryEntity);//洗手时机对比数据
		//洗手时机 和rfid映射 :接触病人前洗手
		Map<String,Integer> washTimesBeforeCloseNick = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesBeforeCloseNick = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :无菌操作前洗手
		Map<String,Integer> washTimesBeforeAsepticOperation = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesBeforeAsepticOperation = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :接触患者后洗手
		Map<String,Integer> washTimesAfterCloseNick = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesAfterCloseNick = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :接触患者环境后洗手
		Map<String,Integer> washTimesAfterCloseNickEnvri = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesafterCloseNickEnvri = new HashMap<String,Integer>();
		for (WashHandLog wash : washHandMomentList) {
			if (wash.getRfidStatus() != null) {
				if (wash.getRfidStatus() == 9|| wash.getRfidStatus() == 10) { 
					// 接触病人前洗手
					if(washTimesBeforeCloseNick.get(wash.getRfid()) == null ){
						washTimesBeforeCloseNick.put(wash.getRfid(), 1);
					}else{
						washTimesBeforeCloseNick.put(wash.getRfid(), 
								washTimesBeforeCloseNick.get(wash.getRfid())+1);
					}
				} else if(wash.getRfidStatus()== 11) {//接触病人前未洗手
					if(notWashTimesBeforeCloseNick.get(wash.getRfid()) == null ){
						notWashTimesBeforeCloseNick.put(wash.getRfid(), 1);
					}else{
						notWashTimesBeforeCloseNick.put(wash.getRfid(), 
								notWashTimesBeforeCloseNick.get(wash.getRfid() )+1);
					}
				}else if(wash.getRfidStatus() == 1000){//无菌操作前洗手,假设值,暂时用不上
					if(washTimesBeforeAsepticOperation.get(wash.getRfid()) == null ){
						washTimesBeforeAsepticOperation.put(wash.getRfid(), 1);
					}else{
						washTimesBeforeAsepticOperation.put(wash.getRfid(), 
								washTimesBeforeAsepticOperation.get(wash.getRfid() )+1);
					}
				}else if(wash.getRfidStatus()== 10000 ){//无菌操作前未洗手,假设值,暂时用不上
					if(notWashTimesBeforeAsepticOperation.get(wash.getRfid()) == null ){
						notWashTimesBeforeAsepticOperation.put(wash.getRfid(), 1);
					}else{
						notWashTimesBeforeAsepticOperation.put(wash.getRfid(), 
								notWashTimesBeforeAsepticOperation.get(wash.getRfid())+1);
					}
				}else if(wash.getRfidStatus() == 12){//接触病人后洗手（当前是短时离开患者提醒）
					if(washTimesAfterCloseNick.get(wash.getRfid()) == null ){
						washTimesAfterCloseNick.put(wash.getRfid(), 1);
					}else{
						washTimesAfterCloseNick.put(wash.getRfid(), 
								washTimesAfterCloseNick.get(wash.getRfid())+ 1 );
					}
					//注意，接触患者后未手卫生的分母中加上事件（接近患者未手卫生）,该值在循环外计算（number）
					//原来代码wash.getRfidStatus() == 13,number=1
				}else if(wash.getRfidStatus() == 9 || wash.getRfidStatus() == 10){//接触患者后未洗手
					if(notWashTimesAfterCloseNick.get(wash.getRfid()) == null){
						notWashTimesAfterCloseNick.put(wash.getRfid(), 1);
					}else{
						notWashTimesAfterCloseNick.put(wash.getRfid(), 
								notWashTimesAfterCloseNick.get(wash.getRfid()) + 1 );
					}
				}else if(wash.getRfidStatus()== 6){//接触病人环境后洗手
					if(washTimesAfterCloseNickEnvri.get(wash.getRfid()) == null ){
						washTimesAfterCloseNickEnvri.put(wash.getRfid(), 1);
					}else{
						washTimesAfterCloseNickEnvri.put(wash.getRfid(), 
								washTimesAfterCloseNickEnvri.get(wash.getRfid()) + 1);
					}
				}else if(wash.getRfidStatus() == 8){//接触病人环境后未洗手
					if(notWashTimesafterCloseNickEnvri.get(wash.getRfid()) == null ){
						notWashTimesafterCloseNickEnvri.put(wash.getRfid(), 1);
					}else{
						notWashTimesafterCloseNickEnvri.put(wash.getRfid(), 
								notWashTimesafterCloseNickEnvri.get(wash.getRfid()) + 1 );
					}
				}
			}
		}
		GroupTree departTree =null;
		//是否需要去掉感控科
		if(isNeedHideControllerDepart != null && 
				isNeedHideControllerDepart.booleanValue() == true){
			//需要隐藏感控科 132
			departList.remove(132L);
		}
		for(Long departId:departList){
			WashHandMoment washHandMoment = new WashHandMoment();
			//eventCompare = new EventCompare();
			departTree=groupTreeService.getById(departId);
			if(departTree!=null){
				washHandMoment.setName(departTree.getName());
			}
			staffList= staffInfoDao.findBy("groupTree.id", departId);
			
			//遍历部门下staff
			for(StaffInfo staff:staffList){
				String rfidStr = getRfidByStaffId(staff.getId());
				
				Integer washBCloseNick = washTimesBeforeCloseNick.get(rfidStr);
				Integer noWashBCloseNick = notWashTimesBeforeCloseNick.get(rfidStr);
				
				Integer washBAsepticOperation = washTimesBeforeAsepticOperation.get(rfidStr);
				Integer noWashBAsepticOperation = notWashTimesBeforeAsepticOperation.get(rfidStr);
				
				Integer washACloseNick = washTimesAfterCloseNick.get(rfidStr);
				Integer noWashACloseNick = notWashTimesAfterCloseNick.get(rfidStr);
				
				Integer washACloseNickEnvri = washTimesAfterCloseNickEnvri.get(rfidStr);
				Integer noWashACloseNickEnvri = notWashTimesafterCloseNickEnvri.get(rfidStr);
				// 为 washHandMoment 对象赋值
				if(washBCloseNick != null){
					washHandMoment.setWashBeforeCloseNickTimes(
							washHandMoment.getWashBeforeCloseNickTimes() + washBCloseNick);
				}else{
					washHandMoment.setWashBeforeCloseNickTimes(washHandMoment.getWashBeforeCloseNickTimes() + 0);
				}
				if(noWashBCloseNick != null){
					washHandMoment.setNotWashBeforeCloseNickTimes(
							washHandMoment.getNotWashBeforeCloseNickTimes() + noWashBCloseNick);
				}else{
					washHandMoment.setNotWashBeforeCloseNickTimes(washHandMoment.getNotWashBeforeCloseNickTimes() + 0);
				}
				if(washBAsepticOperation != null){
					washHandMoment.setWashBeforeAsepticOperationTimes(
							washHandMoment.getWashBeforeAsepticOperationTimes() + washBAsepticOperation);
				}else{
					washHandMoment.setWashBeforeAsepticOperationTimes(washHandMoment.getWashBeforeAsepticOperationTimes() + 0);
				}
				if(noWashBAsepticOperation != null){
					washHandMoment.setNotWashBeforeAsepticOperationTimes(
							washHandMoment.getNotWashBeforeAsepticOperationTimes() + noWashBAsepticOperation);
				}else{
					washHandMoment.setNotWashBeforeAsepticOperationTimes(washHandMoment.getNotWashBeforeAsepticOperationTimes() + 0);
				}
				if(washACloseNick != null){
					washHandMoment.setWashAfterCloseNickTimes(
							washHandMoment.getWashAfterCloseNickTimes() + washACloseNick);
				}else{
					washHandMoment.setWashAfterCloseNickTimes(washHandMoment.getWashAfterCloseNickTimes() + 0);
				}
				if(noWashACloseNick != null ){
					washHandMoment.setNotWashAfterCloseNickTimes(
							washHandMoment.getNotWashAfterCloseNickTimes() + noWashACloseNick);
				}else{
					washHandMoment.setNotWashAfterCloseNickTimes(washHandMoment.getNotWashAfterCloseNickTimes() + 0);
				}
				if(washACloseNickEnvri != null){
					washHandMoment.setWashAfterCloseNickEnvriTimes(
							washHandMoment.getWashAfterCloseNickEnvriTimes() + washACloseNickEnvri);
				}else{
					washHandMoment.setWashAfterCloseNickEnvriTimes(washHandMoment.getWashAfterCloseNickEnvriTimes() + 0);
				}
				if(noWashACloseNickEnvri != null){
					washHandMoment.setNotWashAfterCloseNickEnvriTimes(
							washHandMoment.getNotWashAfterCloseNickEnvriTimes() + noWashACloseNickEnvri);
				}else{
					washHandMoment.setNotWashAfterCloseNickEnvriTimes(washHandMoment.getNotWashAfterCloseNickEnvriTimes() + 0);
				}
				
			}
			//设置该部门对应的些值
			int washBCloseNickTotal = 
					washHandMoment.getWashBeforeCloseNickTimes() + washHandMoment.getNotWashBeforeCloseNickTimes();
			
			int washBAsepticOperationTotal = 
					washHandMoment.getWashBeforeAsepticOperationTimes() + washHandMoment.getNotWashBeforeAsepticOperationTimes();

			int washACloseNickTotal = washHandMoment.getNotWashAfterCloseNickTimes() + cpiacwp + cpnw;

			int washACloseNickEnvriTotal =  
					washHandMoment.getWashAfterCloseNickEnvriTimes() + washHandMoment.getNotWashAfterCloseNickEnvriTimes();
			
			if(washHandMoment.getWashBeforeCloseNickTimes() != 0 ){
				washHandMoment.setRateWashBeforeCloseNick(getPercent(washHandMoment.getWashBeforeCloseNickTimes(),
						washBCloseNickTotal));
			}else{
				washHandMoment.setRateWashBeforeCloseNick("0.0");
			}
			
			if(washHandMoment.getWashBeforeAsepticOperationTimes() != 0 ){
				washHandMoment.setRateWashBeforeAsepticOperation(getPercent(washHandMoment.getWashBeforeAsepticOperationTimes(),
						washBAsepticOperationTotal));
			}else{
				washHandMoment.setRateWashBeforeAsepticOperation("100.0");
			}
			double closingPatientsPersent = 0;
			int numberOfClosingPatients = washHandLogService.findLogsOfClosingPatients(queryEntity).size();
			if(washACloseNickTotal == 0){
				closingPatientsPersent = 100;
			} else {
				closingPatientsPersent = new BigDecimal((float)(washHandMoment.getWashAfterCloseNickTimes()) * 100/washACloseNickTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			washHandMoment.setRateWashAfterCloseNick(String.valueOf(closingPatientsPersent));
			if(washHandMoment.getWashAfterCloseNickEnvriTimes() != 0 ){
				washHandMoment.setRateWashAfterCloseNickEnvri(getPercent(washHandMoment.getWashAfterCloseNickEnvriTimes(), 
						washACloseNickEnvriTotal));
			}else{
				washHandMoment.setRateWashAfterCloseNickEnvri("0.0");
			}
			washHandMomentCompareList.add(washHandMoment);
		}
		
	}
	
	public List<WashHandLog> getWashHandList() {
		return washHandList;
	}
	public void setWashHandList(List<WashHandLog> washHandList) {
		this.washHandList = washHandList;
	}
	public Map<String, Integer> getMapRight() {
		return mapRight;
	}
	public void setMapRight(Map<String, Integer> mapRight) {
		this.mapRight = mapRight;
	}
	public Map<String, Integer> getMapWrong() {
		return mapWrong;
	}
	public void setMapWrong(Map<String, Integer> mapWrong) {
		this.mapWrong = mapWrong;
	}
	public List<StaffEntity> getStaffEntityList() {
		return staffEntityList;
	}
	public void setStaffEntityList(List<StaffEntity> staffEntityList) {
		this.staffEntityList = staffEntityList;
	}
	public List<EventCompare> getEventCompareList() {
		return eventCompareList;
	}
	public void setEventCompareList(List<EventCompare> eventCompareList) {
		this.eventCompareList = eventCompareList;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getWashHandStatus() {
		return washHandStatus;
	}
	public void setWashHandStatus(String washHandStatus) {
		this.washHandStatus = washHandStatus;
	}
	
	/**
	 * 时间戳转换工具
	 * @param date
	 * @return
	 * @author wwg
	 */
	public static String dateToString(Date date){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res = simpleDateFormat.format(date);
        return res;
    }
	
}
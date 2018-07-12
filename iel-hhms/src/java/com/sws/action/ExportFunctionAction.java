package com.sws.action;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import com.sws.common.entity.DepartRateEntity;
import com.sws.common.entity.EventCompare;
import com.sws.common.entity.PageInfo;
import com.sws.common.entity.StaffEntity;
import com.sws.common.entity.StaffWork;
import com.sws.common.entity.TypeRateEntity;
import com.sws.common.entity.WashHandMoment;
import com.sws.common.entity.WashTimes;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.DateUtils;
import com.sws.common.until.ExcelAction;
import com.sws.common.until.StringUtil;
import com.sws.dao.GroupTreeDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;
import com.sws.model.LogInfo;
import com.sws.model.SignalLog;
import com.sws.model.StaffInfo;
import com.sws.model.UserInfo;
import com.sws.model.WashHandLog;
import com.sws.service.LogInfoService;
import com.sws.service.SignalLogService;
import com.sws.service.WashHandLogService;
import com.sys.core.util.bean.Page;

public class ExportFunctionAction extends ExcelAction<WashHandLog>{
	private static final long serialVersionUID = -2868312613376378663L;
	//private Logger log = LoggerFactory.getLogger(WashHandLogAction.class);
	@Autowired
    private GroupTreeDao groupTreeDao;
	@Autowired
	private WashHandLogService washHandLogService;
	@Autowired
	private SignalLogService signalLogService;
	@Autowired
	private LogInfoService logInfoService;
	private List<WashHandLog> washHandList = new ArrayList<WashHandLog>();
	private List<LogInfo> logInfoList = new ArrayList<LogInfo>();
	private Map<String,Integer> mapRight = new HashMap<String,Integer>();//正常----或者接触前
	private Map<String,Integer> mapWrong = new HashMap<String,Integer>();//违规----或者接触后
	private List<StaffEntity> staffEntityList = new ArrayList<StaffEntity>();
	private List<StaffWork> staffWorkList = new ArrayList<StaffWork>();//人员依从性计算
	private List<UserInfo> userInfoList = new ArrayList<UserInfo>();
	private String eventType;
	private String rfidStatus;
	private String timeType;
	private String washHandStatus;
	private String jobType;
	private List<DepartRateEntity> departSanitizerList = new ArrayList<DepartRateEntity>();
	Map<String,String>mapTitle = new HashMap<String, String>();
	private List<TypeRateEntity> typeRateEntityList = new ArrayList<TypeRateEntity>();
	private List<EventCompare>	eventCompareList = new ArrayList<EventCompare>();//事件比较
	
	/**
	 * 手卫生次数+出液量的导出
	 * @author wwg
	 * @throws Exception 
	 */
	public void timesOfWashHandPro() throws Exception{
		int single = Integer.parseInt(parameterInfoDao.findByTypeAndKey(3, "single").getValue());
		if(StringUtil.isNotBlank(queryEntity.getTreeId())){
			queryEntity.setStr1(queryEntity.getTreeId());
		}
		Map<String, List<Integer>> frequencyByStaff = washHandLogService.findFrequencyByTimeAndDepartment(queryEntity);
		List<WashTimes> waList = new ArrayList<WashTimes>();
		if(frequencyByStaff != null){
			for (String key : frequencyByStaff.keySet()) {
				WashTimes washTimes = new WashTimes();
				washTimes.setDocName(key.toString());
				List<Integer> value = frequencyByStaff.get(key);
				/**
				 * 0表示总次数；1表示不清洁的次数；2表示有限清洁的次数；3表示有效清洁的次数
				 */
				Integer totalLiquid = value.get(0) * single;
				Integer notcleanLiquid = value.get(1) * single;
				Integer limitcleanLiquid = value.get(2) * single;
				Integer cleanLiquid = value.get(3) * single;
				washTimes.setTotalWashTimes(totalLiquid.toString());
				washTimes.setNotcleanTimes(notcleanLiquid.toString());
				washTimes.setLimitcleanTimes(limitcleanLiquid.toString());
				washTimes.setCleanTimes(cleanLiquid.toString());
				waList.add(washTimes);
			}
		}
		int columns = 1;
		String fileName = "科室人员洗手液用量统计" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("timesOfWashHandPro", 0);
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if(waList != null && waList.size() > 0){
			int rows = 3;
			for (WashTimes washTimes : waList) {
				setRow(rows);
				columns=0;
				setCellValue(columns++, washTimes.getDocName());
				setCellValue(columns++, washTimes.getTotalWashTimes());
				setCellValue(columns++, washTimes.getNotcleanTimes());
				setCellValue(columns++, washTimes.getLimitcleanTimes());
				setCellValue(columns++, washTimes.getCleanTimes());
				rows++;
			}
		}
		outputFile(fileName);
	}
	
	/**
	 * 手卫生次数的导出
	 * @author wwg
	 * @throws Exception 
	 */
	public void timesOfWashHand() throws Exception{
		if(StringUtil.isNotBlank(queryEntity.getTreeId())){
			queryEntity.setStr1(queryEntity.getTreeId());
		}
		Map<String, List<Integer>> frequencyByStaff = washHandLogService.findFrequencyByTimeAndDepartment(queryEntity);
		List<WashTimes> waList = new ArrayList<WashTimes>();
		if(frequencyByStaff != null){
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
		}
		int columns = 1;
		String fileName = "科室手卫生次数统计" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("timesOfWashHand", 0);
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if(waList != null && waList.size() > 0){
			int rows = 3;
			for (WashTimes washTimes : waList) {
				setRow(rows);
				columns=0;
				setCellValue(columns++, washTimes.getDocName());
				setCellValue(columns++, washTimes.getTotalWashTimes());
				setCellValue(columns++, washTimes.getNotcleanTimes());
				setCellValue(columns++, washTimes.getLimitcleanTimes());
				setCellValue(columns++, washTimes.getCleanTimes());
				rows++;
			}
		}
		outputFile(fileName);
	}
	
	/**
	 * 大门外手卫生事件的导出
	 * @author wwg
	 */
	public void outDoorExport() throws Exception{
		Date startDate = DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		rowNum = 1000;
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
				String updateTimeString = DateUtils.dateToString(date);
				washHandLog.setUpdateTimeString(updateTimeString);
			}
		}
		int columns = 1;
		String fileName = "外门手卫生事件" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("outDoorEvent", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		if (washHandLogs != null && washHandLogs.size() > 0) {
		int rows = 2;
			for(WashHandLog log:washHandLogs){
				setRow(rows);
					columns=0;
					setCellValue(columns++, log.getDeviceType());
					setCellValue(columns++, log.getDeviceNo());
					setCellValue(columns++, log.getEventType());
					setCellValue(columns++, log.getCreateTime());
				rows++;
			}
		}
		outputFile(fileName);
	}
	
	/***************************************事件查询导出************************************/
	@SuppressWarnings("unchecked")
	public void exportEventByTime() throws Exception{
		String deviceStr=""; 
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		//ajax查询条件组装
		eventAjaxPar();
		if(queryEntity.getInt1() != null && !queryEntity.getInt1().equals(-1)){
			rfidList.add(getRfidByStaffId(Long.valueOf(queryEntity.getInt1())));
		}else{
			if(StringUtil.isNotBlank(jobType)){
				String[] staffTypeArray = jobType.split(",");//人员类别名称
				for(String staffType:staffTypeArray){
					typeList.add(staffType);
				}
				if(StringUtil.isNotBlank(queryEntity.getTreeId())){
					staffList= staffInfoDao.findByDepartAndType(getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
				}
				for(StaffInfo info:staffList){
					rfidList.add(getRfidByStaffId(info.getId()));
				}
			}
		}
		queryEntity.setRfidList(rfidList);
		if(StringUtils.isNotBlank(washHandStatus)){
			queryEntity.setStr2(washHandStatus);//手卫生状态带进查询条件
		}
		if(rfidList.size()>0){
			page = washHandLogService.pageByEvent(queryEntity, 1, 1000, "createTime", "desc");
			for(WashHandLog log:(List<WashHandLog>)page.getResult()){
				if(log.getRfid()!=null){
					StaffInfo staffInfo=getStaff(log.getRfid());
					log.setDocName(getDocByRfid(log.getRfid()));
					log.setDepartName(getDepartNameByRfid(log.getRfid()));
					log.setStaffTypeName(getJobTypeMap().get(staffInfo.getCategory()));
				}
				if(log.getRfidStatus()!=null){
					log.setRfidStatusName(SysStatics.rfidStatusMap.get(log.getRfidStatus().toString()));
				}
				if(log.getEventType()!=null&&log.getRfidStatus()!=null){
					if(log.getDeviceNo()!=null&&log.getDeviceType()!=null){
						deviceStr="("+log.getDeviceNo()+SysStatics.deviceTypeMap.get(log.getDeviceType())+")";
					}else{
						deviceStr="";
					}
					
					log.setEventTypeName(SysStatics.eventTypeMap.get(log.getEventType()));
					if(log.getEventType().equals("0007")){
						if(log.getRfidStatus().equals(1)){//不清洁
							log.setRemark("进入病区未手卫生"+deviceStr);
					    }else{
					    	log.setRemark("正常进入病区"+deviceStr);
					    }
					}
					else if(log.getEventType().equals("0008")){
						if(log.getRfidStatus().equals(1)){//不清洁
							log.setRemark("离开病区未手卫生"+deviceStr);
					    }else{
					    	log.setRemark("正常离开病区"+deviceStr);
					    }
					}else{
						log.setRemark(log.getEventTypeName()+"_"+log.getRfidStatusName()+deviceStr);
					}
				}
				washHandList.add(log);
			}
			
		}
		
		int columns=1;
		String fileName = "事件" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("eventByTime", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		if (washHandList != null && washHandList.size() > 0) {
			int rows = 1;
			String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
			String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
			String timeStr = startTime + "—" + endTime;
			setRow(rows);
			setCellValue(0, "起止时间：");
			setCellValue(1, timeStr);
			rows = 3;
			for(WashHandLog washHand:washHandList){
				setRow(rows);
					columns = 0;
					setCellValue(columns++, washHand.getDocName());
					setCellValue(columns++, washHand.getDepartName());
					setCellValue(columns++, washHand.getStaffTypeName());
					setCellValue(columns++, washHand.getRfid());
					setCellValue(columns++, washHand.getEventTypeName());
					setCellValue(columns++, washHand.getRfidStatusName());
					setCellValue(columns++, washHand.getRemark());
					setCellValue(columns++, washHand.getCreateTime());
				rows++;
			}
			
		}
		outputFile(fileName);
	 }
	
    public void eventAjaxPar(){
    	if(eventType!=null && !"".equals(eventType)){
			queryEntity.setStr1(eventType);
		}
		if(treeId!=null && !"".equals(treeId)){
			queryEntity.setTreeId(treeId);
		}
		if(startTime!=null && !"".equals(startTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
		}
		if(endTime!=null && !"".equals(endTime)){
			queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
		}
		if(staffId!=null && !"".equals(staffId)){
			queryEntity.setInt1(Integer.valueOf(staffId));
		}
		if(rfidStatus!=null && !"".equals(rfidStatus)){
			queryEntity.setStr2(rfidStatus);
		}
    }
	/******************************日志查询*********************************/
    
    /**
	 * @return
     * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void SignalLogSearch() throws Exception {
		page = signalLogService.pageByEvent(queryEntity, 1, 1000, "createTime", "desc");
    	List<SignalLog> logs=(List<SignalLog>)page.getResult();
		int columns=1;
		String fileName = "日志" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("signalLog", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if (logs != null && logs.size() > 0) {
		int rows = 3;
			for(SignalLog log:logs){
				setRow(rows);
					columns=0;
					if(log.getRfid().equals("000000")){
	    				log.setRfid("NC");
	    			}
	    			if(log.getRfid().equals(log.getDeviceNo())){
	    				log.setDeviceNo("NC");
	    			}
	    			if(log.getEventType()!=null){
						log.setEventTypeName(SysStatics.logEventTypeMap.get(log.getEventType()));	
					}
	    			if(log.getDeviceType()!=null){
	    				log.setDeviceTypeName(SysStatics.logDeviceTypeMap.get(log.getDeviceType()));
	    			}
					setCellValue(columns++, log.getRfid());
					setCellValue(columns++, log.getDeviceNo());
					setCellValue(columns++, log.getDeviceTypeName());
					setCellValue(columns++, log.getEventTypeName());
					setCellValue(columns++, log.getCreateTime());
					setCellValue(columns++, log.getContent());
				rows++;
			}
			
		}
		outputFile(fileName);
	}
	
	/******************************登录日志*********************************/
	/**
	 * @return
     * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void ExportForLogCheck() throws Exception {
		List<LogInfo> logInfos = null;
		long total = 0;
		Long currentUserId = Long.valueOf(getCommSession().getUserId());
		if(currentUserId == 1){
			page = logInfoService.page(queryEntity, 1, 1000, "createTime","desc");
			logInfos = (List<LogInfo>) page.getResult();
			total = page.getTotal();
		} else {
			logInfos = logInfoService.findLogsWhileUserNotAdmin(queryEntity, 1, 1000, "createTime","desc");
			total = logInfos.size();
			PageInfo<LogInfo> pageInfo = new PageInfo<LogInfo>();
			logInfos = pageInfo.listByPage(1, 1000, logInfos);
		}
		page = new Page(_page, rowNum, logInfos, total);
		
//		page = logInfoService.page(queryEntity, 1, 1000, "createTime","desc");
//		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		for(LogInfo log:logInfos){
			if(log.getUserInfo()!=null&&log.getUserInfo().getName()!=null){
				log.setOptUser(log.getUserInfo().getName());
			}
			logInfoList.add(log);
		}
		
		int columns=1;
		String fileName = "登录日志" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("logInfoCheck", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if (logInfoList != null && logInfoList.size() > 0) {
		int rows = 3;
			for(LogInfo log:logInfoList){
				setRow(rows);
					columns=0;
					setCellValue(columns++, logType(log.getType()));
					setCellValue(columns++, log.getOptUser());
					setCellValue(columns++, log.getRemark());
					setCellValue(columns++, log.getCreateTime());
				rows++;
			}
		}
		outputFile(fileName);
	}
	
    public String logType(Integer i){
    	String [] LogStaus ={"用户登录","用户退出","设备添加"};
    	String staus ="";
    	staus=LogStaus[i-1];
    	return staus;
    }
    
    /******************************人员依从性*********************************/
    
	/**
	 * 列表显示
	 * @return
	 * @throws Exception 
	 */
	public void ExportForRateByStaff() throws Exception {
		StaffWork staffWork = null;
		GroupTree departTree =null;
		List<StaffInfo>	staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		if(queryEntity.getInt1()!=null&&!queryEntity.getInt1().equals(-1)){
			rfidList.add(getRfidByStaffId(Long.valueOf(queryEntity.getInt1())));
		}else{
			List<String> typeList = new ArrayList<String>();
			if(queryEntity.getStr1()!=null){
				String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
				for(String staffType:staffTypeArray){
					typeList.add(staffType);
				}
				if(StringUtil.isNotBlank(queryEntity.getTreeId())){
					//导出的是本科室的
					//staffList= staffInfoDao.findByDepartAndType(getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
					//导出的是和分配的一致的（当前科室或者全院科室）
					staffList= staffInfoDao.findByDepartAndTypeAndDlevel(getManageDepart(),getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
				}
				for(StaffInfo info:staffList){
					rfidList.add(getRfidByStaffId(info.getId()));
				}
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
		}
		mapProcess();//map数据封装
		for(String rfid:rfidList){
			staffWork = new StaffWork();
			DeviceInfo device = deviceInfoDao.findUniqueBy("no", rfid);
			if(device!=null){
				if(device.getStaffId()!=null){
					StaffInfo staffInfo = staffInfoDao.findUniqueBy("id", device.getStaffId());
					if (staffInfo.getName() != null) {
						staffWork.setDocName(staffInfo.getName());
					}
					if(staffInfo.getCategory()!=null){
						staffWork.setDocType(SysStatics.jobTypeMap.get(staffInfo.getCategory()));
					}
					if(staffInfo.getGroupTree().getId()!=null){
						departTree  = groupTreeService.getById(staffInfo.getGroupTree().getId());
						if(departTree!=null&&departTree.getName()!=null){
							staffWork.setDepartName(departTree.getName());
						}
					}
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
			staffWorkList.add(staffWork);
		}
		int columns=1;
		String fileName = "人员依从率" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("rateByStaff", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if (staffWorkList != null && staffWorkList.size() > 0) {
		int rows = 3;
			for(StaffWork staffWorkDemo:staffWorkList){
				setRow(rows);
					columns=0;
					setCellValue(columns++, staffWorkDemo.getDocName());
					setCellValue(columns++, staffWorkDemo.getDepartName());
					setCellValue(columns++, staffWorkDemo.getDocType());
					setCellValue(columns++, staffWorkDemo.getRfid());
					setCellValue(columns++, staffWorkDemo.getNormalNum());
					setCellValue(columns++, staffWorkDemo.getErrorNum());
					setCellValue(columns++, staffWorkDemo.getRate());
				rows++;
			}
		}
		outputFile(fileName);
	}
	/*
	private void mapProcess() {
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null) {
				if (wash.getEventType().equals("0003")) { // 有效洗手数
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
	}
	*/
	
	private void mapProcess() {
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null) {
				if (wash.getEventType().equals("0003")) { // 有效洗手数
					if(wash.getRfidStatus().equals(2)||wash.getRfidStatus().equals(3)||wash.getRfidStatus().equals(3)||wash.getRfidStatus().equals(6)||wash.getRfidStatus().equals(9)||
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
	/***********************************用户管理导出*********************************/
	/**
	 * @author 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void ExportForUserList() throws Exception {
		page = userInfoService.page(getCommSession().getOrg(),queryEntity, 1, 1000, "updateTime","desc");
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		for(UserInfo info:(List<UserInfo>)page.getResult()){
			if(info.getGroupTree()!=null&&info.getGroupTree().getName()!=null){
				info.setStrRev1(info.getGroupTree().getName());
			}
			if(info.getRoleInfo()!=null&&info.getRoleInfo().getName()!=null){
				info.setStrRev2(info.getRoleInfo().getName());
			}
			//过期时间改掉expireTime就都是null了，下面if条件不会进入
			if(info.getUserStatus()!=SysStatics.USER_STATUS_EXPRIE&&info.getExpireTime()!=null
					&&!info.getName().equals("admin")){
				if(DateUtils.compareByYMD(new Date(), info.getExpireTime())>0){
					info.setUserStatus(SysStatics.USER_STATUS_EXPRIE);//过期
//					userInfoService.update(info);
				}
			}
			userInfoList.add(info);
		}
		
		int columns=1;
		String fileName = "用户管理" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("userList", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		if (userInfoList != null && userInfoList.size() > 0) {
		int rows =2	;
			for(UserInfo userInfo:userInfoList){
				setRow(rows);
					columns=0;
					setCellValue(columns++, userInfo.getName());
					setCellValue(columns++, userStaus(userInfo.getUserStatus()));
					setCellValue(columns++, userInfo.getStrRev1());
					setCellValue(columns++, userInfo.getStrRev2());
					setCellValue(columns++, userInfo.getLoginCounts());
					setCellValue(columns++, userInfo.getExpireTime());
					setCellValue(columns++, userInfo.getUpdateTime());
					setCellValue(columns++, userInfo.getLastLoginTime());
					setCellValue(columns++, userInfo.getRemark());
				rows++;
			}
			
		}
		outputFile(fileName);
	}
	
	public String userStaus(Integer i){
		String staus="";
		String [] stausArray = {"正常","过期","停用"};
		staus = stausArray[i];
		return staus;
	}
	
	/*********************************** 洗手液使用量统计By部门  ******************************/
	public String sanitizerByDeptList(){
		List<GroupTree>	departList = new ArrayList<GroupTree>();
		UserInfo user = userInfoService.getById(Long.valueOf(getCommSession().getUserId()));
		if(user.getGroupTree()!=null){
			if(!user.getGroupTree().getId().equals(1L)){
				GroupTree group = groupTreeDao.findUniqueBy("id", user.getGroupTree().getId());
				departList.add(group);
			}else{
				departList = groupTreeService.getDepart();
			}
		}else{
			departList = groupTreeService.getDepart();
		}
		ActionContext.getContext().put("departList", departList);
		return "sanitizerByDeptList";
	}
	public void querySanitizerByTime(){
		sanitizerByTime();
		JSONObject obj = new JSONObject();
		obj.put("departSanitizerList", departSanitizerList);
		writeResponse(obj.toString());
	}
	
	public void sanitizerByTime(){
		String rfid;
		Map<Long,String> staffRfid = new HashMap<Long,String>();//staffId,rfid
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<Long> departList = new ArrayList<Long>();
		if(StringUtil.isNotBlank(depart)){
			queryEntity.setStr1(depart);
		}
		if(StringUtil.isNotBlank(timeType)){
			queryEntity.setInt1(Integer.valueOf(timeType));
		}
		if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
			queryEntity.setStartTime(DateUtils.str2DateByYM(startTime));
			queryEntity.setEndTime(DateUtils.str2DateByYM(endTime));
		}  //数据封装
		if(StringUtil.isNotBlank(queryEntity.getStr1())){   //科室
			String[] departArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String departId:departArray){
				departList.add(Long.valueOf(departId));
			}
			staffList= staffInfoDao.findAllEntitysBy("groupTree.id", departList);
			for(StaffInfo info:staffList){
				rfid=getRfidByStaffId(info.getId());
				rfidList.add(rfid);
				staffRfid.put(info.getId(), rfid);
			}
		}
		queryEntity.setRfidList(rfidList);	
		if(queryEntity.getInt1()!=null){
			switch(queryEntity.getInt1()){ //时间统计类型
			case 1:
				queryEntity.setStartTime(DateUtils.getStartYear(DateUtils.getYear(queryEntity.getStartTime()))); 
				queryEntity.setEndTime(DateUtils.getEndYear(DateUtils.getYear(queryEntity.getEndTime()))); 
				if(rfidList.size()>0){
					washHandList = washHandLogService.SanitizerNum(queryEntity);
				}
				mapProcessYear();
				break;
			case 2:
				queryEntity.setStartTime(DateUtils.getStartMonth(DateUtils.getYear(queryEntity.getStartTime()),DateUtils.getMonth(queryEntity.getStartTime()))); 
				queryEntity.setEndTime(DateUtils.getEndMonth(DateUtils.getYear(queryEntity.getEndTime()),DateUtils.getMonth(queryEntity.getEndTime())));
				if(rfidList.size()>0){
					washHandList = washHandLogService.SanitizerNum(queryEntity);
				}
				mapProcessMonth();
				break;
			}
		}
		mapCompute(staffRfid,staffList,departList);	
	}
	
	private void mapProcessYear() {
		String mapStr;
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null&&wash.getCreateTime()!=null) {
				mapStr=wash.getRfid()+","+String.valueOf(DateUtils.getYear(wash.getCreateTime()));
				if (wash.getEventType().equals("0003")) { // 有效洗手数
					if (mapRight.get(mapStr) == null) {
						mapRight.put(mapStr, 1);
					} else {
						mapRight.put(mapStr,mapRight.get(mapStr) + 1);// 只会更新value的值
					}
				} 
			}
		}
		
	}
	
	private void mapProcessMonth() {
		String mapStr;
		for (WashHandLog wash : washHandList) {
			if (wash.getRfid() != null&&wash.getCreateTime()!=null) {
				mapStr = wash.getRfid()+","+DateUtils.date2StrByYM(wash.getCreateTime());
				if (wash.getEventType().equals("0003")) { // 有效洗手数
					if (mapRight.get(mapStr) == null) {
						mapRight.put(mapStr, 1);
					} else {
						mapRight.put(mapStr,mapRight.get(mapStr) + 1);// 只会更新value的值
					}
				} 
			}
		}
	}
	
	private void mapCompute(Map<Long,String> staffRfid,List<StaffInfo> staffList,List<Long> departList){
		String mapStr;
		DepartRateEntity  departRate=null;
		Set<String> setTime	= new TreeSet<String>();
		Integer rightCount=0,i=0;
		List<StaffInfo> departStaffList = new ArrayList<StaffInfo>();//一个部门下的人员列表
		Date tempDate =null;
		if(queryEntity.getInt1()!=null){ //年统计
			if(queryEntity.getInt1().equals(1)){
				for(i=DateUtils.getYear(queryEntity.getStartTime());i<=DateUtils.getYear(queryEntity.getEndTime());i++){
					setTime.add(i.toString());
				}
				
			}else{ 
				for(i=1,tempDate=queryEntity.getStartTime();queryEntity.getEndTime().compareTo(tempDate)>0;i++){
					setTime.add(DateUtils.date2StrByYM(tempDate));
					tempDate=DateUtils.addMouth(queryEntity.getStartTime(), i);
					
				}
			}
		}
		//setTime排序封装
		for(String time:setTime){
			departRate= new DepartRateEntity();
			departRate.setTime(time);
			for(Long departId:departList){
				rightCount=0;
				departRate.getNameList().add(groupTreeService.getById(departId).getName());//部门名称
				departStaffList = departStaff(staffList,departId);
				for(StaffInfo info:departStaffList){
					mapStr=staffRfid.get(info.getId())+","+time;
					if(mapRight.get(mapStr)!=null){
						rightCount+=mapRight.get(mapStr);
					}
				}
				departRate.getRightCountList().add(rightCount);
				departRate.getRateList().add(getDeptSanitizerNum(rightCount));
			}
			departSanitizerList.add(departRate);
		}
	}
	
	private List<StaffInfo> departStaff(List<StaffInfo> staffList,Long departId){
		List<StaffInfo> list = new ArrayList<StaffInfo>();
		for(StaffInfo info:staffList){
			if(info.getGroupTree()!=null&&info.getGroupTree().getId()!=null){
				if(info.getGroupTree().getId().equals(departId)){
					list.add(info);
				}
			}
		}
		return list;
	}
	
	public String getDeptSanitizerNum(Integer washNum){
		String DeptSanitizerNum ="";
		DeptSanitizerNum=washNum*3+"ml";
		return DeptSanitizerNum;
	}
	
	//导出
	public void exportByDeptAndTime()throws Exception {
		
		sanitizerByTime();
		int columns=1;
		String fileName = "sanitizerByTime_" + DateUtils.date2StrByYMDHMS(new Date());
		//excel导出
	    loadFileAndSheet("sanitizerByTime", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		if (departSanitizerList != null && departSanitizerList.size() > 0) {
			if(departSanitizerList.get(0).getNameList()!=null){
				setRow(1); 
				setCellValue(0, "时间"); 
				columns=1;
				for(String name:departSanitizerList.get(0).getNameList()){
					setCellValue(columns++, name);
				}
			}//列头需要重写的话加上，一般末班的列头定义好就行了
			for (int i = 0; i < departSanitizerList.size(); i++) {
				columns=1;
				setRow(i+2);
				setCellValue(0, departSanitizerList.get(i).getTime());
				for(String rate:departSanitizerList.get(i).getRateList()){
					setCellValue(columns++, rate);
				}
			}
			
		}
		mapTitle.put("type", "sanitizerByDept");
		mapTitle.put("fileName", fileName);
		mapTitle.put("title", "洗手液使用量统计By部门");
		mapTitle.put("xStr", "时间");
		mapTitle.put("yStr", "洗手液使用量");
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		CategoryDataset dataset = sanitizerGetDataSet();
		//生成图片并存入本地
		jfreeTest(mapTitle,dataset);
		//读取图片并插入excel
		
		outputFileAddPic(mapTitle);
//		PicToExcel(mapTitle);
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
				staffList= staffInfoDao.findByDepartAndType(getCommSession().getOrg(),Long.valueOf(queryEntity.getTreeId()),typeList);
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
			staffEntity.setType(staffEntity.getName()+":依从率"+staffEntity.getRate()+"%");
			staffEntityList.add(staffEntity);
		}
	}
	
	/**
	 * 列表显示
	 * @return
	 * @throws Exception 
	 */
	public void ExportRateByStaffType() throws Exception {
		//处理组装数据
		rateByStaffType();
		int columns=1;
		String fileName = "人员类别依从率" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("rateByStaffType", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if (staffEntityList != null && staffEntityList.size() > 0) {
		int rows = 3;
			for(StaffEntity staffEntity:staffEntityList){
				setRow(rows);
					columns=0;
					setCellValue(columns++, staffEntity.getName());
					setCellValue(columns++, staffEntity.getPerpleNum());
					setCellValue(columns++, staffEntity.getRightCount());
					setCellValue(columns++, staffEntity.getWrongCount());
					setCellValue(columns++, staffEntity.getRate());
				rows++;
			}
			
		}
		mapTitle.put("fileName", fileName);
		mapTitle.put("title", "人员类型依从率");
		mapTitle.put("xStr","类别");
		mapTitle.put("yStr", "次数");
		//得到jFreeChart所需数据
		CategoryDataset dataset = rateByStaffTypeData();
		jfreeTest(mapTitle,dataset);
		outputFileAddPic(mapTitle);
	}
	
			/***********************************事件比较******************************/	
	/**
	 * 列表、图表显示
	 * @return
	 * @throws Exception 
	 * @author wwg
	 */
	public void ExportEventCompare() throws Exception {

		//计算手卫生时机
		eventWashTimeCalc();
		int columns=1;
		String fileName = "手卫生时机依从率" + DateUtils.date2StrByYMDHMS(new Date());
		loadFileAndSheet("eventCompare", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
		String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		String timeStr = startTime + "—" + endTime;
		setRow(1);
		setCellValue(0, "起止时间：");
		setCellValue(1, timeStr);
		if (washHandMomentCompareList != null && washHandMomentCompareList.size() > 0) {
		int rows = 3;
			for(WashHandMoment eventCompare:washHandMomentCompareList){
				setRow(rows);
					columns=0;
					setCellValue(columns++, eventCompare.getName());
					setCellValue(columns++, eventCompare.getRateWashBeforeAsepticOperation());
					setCellValue(columns++, eventCompare.getRateWashBeforeCloseNick());
					setCellValue(columns++, eventCompare.getRateWashAfterCloseNick());
					setCellValue(columns++, eventCompare.getRateWashAfterCloseNickEnvri());
				rows++;
			}
			
		}
		mapTitle.put("fileName", fileName);
		mapTitle.put("title", "事件比较");
		mapTitle.put("xStr","类别");
		mapTitle.put("yStr", "次数");
		//得到jFreeChart所需数据
		CategoryDataset dataset = eventCompareData();
		jfreeTest(mapTitle,dataset);
		outputFileAddPic(mapTitle);
		
	}
	
	public CategoryDataset eventCompareData() {  
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	        	if (eventCompareList != null && eventCompareList.size() > 0) {
	        		for(EventCompare eventCompare : eventCompareList){
	        			dataset.addValue(Integer.valueOf(eventCompare.getEvenBefore()), "接触前(次)", eventCompare.getType()); 
	        			dataset.addValue(Integer.valueOf(eventCompare.getEvenAfter()), "接触后(次)", eventCompare.getType());
	        		}
	        	}
	        return dataset;  
	    } 
	
	        /***********************************科室依从率查询******************************/	
	/**
	 * 列表、图表显示
	 * @return
	 * @throws Exception 
	 */
	public void ExportRateByDept() throws Exception {
		
			if(StringUtil.isNotBlank(depart)){
				queryEntity.setStr1(depart);
			}
			if(StringUtil.isNotBlank(startTime)&&StringUtil.isNotBlank(endTime)){
				queryEntity.setStartTime(DateUtils.str2DateByYMDHMS(startTime));
				queryEntity.setEndTime(DateUtils.str2DateByYMDHMS(endTime));
			}
			rateByDepart();
			int columns=1;
			String fileName = "科室依从率" + DateUtils.date2StrByYMDHMS(new Date());
			loadFileAndSheet("rateByDept", 0);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
			String startTime = DateUtils.date2String(queryEntity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
			String endTime = DateUtils.date2String(queryEntity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
			String timeStr = startTime + "—" + endTime;
			setRow(1);
			setCellValue(0, "起止时间：");
			setCellValue(1, timeStr);
			if (typeRateEntityList != null && typeRateEntityList.size() > 0) {
				setRow(2); 
				setCellValue(0, "科室"); 
				columns=1;
				for(String name:typeRateEntityList.get(0).getNameList()){
					setCellValue(columns++, name);
				}
			
				//列头需要重写的话加上，一般末班的列头定义好就行了
				for (int i = 0; i < typeRateEntityList.size(); i++) {
					columns=1;
					setRow(i+3);
					setCellValue(0, typeRateEntityList.get(i).getDepart());
					for(String columnsRate:typeRateEntityList.get(i).getRateList()){
						setCellValue(columns++, columnsRate);
					}
				}
				
			}
			mapTitle.put("fileName", fileName);
			mapTitle.put("title", "科室依从率");
			mapTitle.put("xStr","类别");
			mapTitle.put("yStr", "次数");
			//得到jFreeChart所需数据
			CategoryDataset dataset = rateByDept();
			jfreeTest(mapTitle,dataset);
			outputFileAddPic(mapTitle);
	}
	  
    /** 
     * 获取一个演示用的组合数据集对象 
     * 科室依从率
     * @return 
     */  
    public  CategoryDataset rateByDept() { 
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	if(typeRateEntityList!=null && typeRateEntityList.size()!=0){
    		for(TypeRateEntity typeRateEntity:typeRateEntityList){
    			dataset.addValue(Integer.valueOf(typeRateEntity.getRightCount()), "已执行", typeRateEntity.getRate()); 
    			dataset.addValue(Integer.valueOf(typeRateEntity.getWrongCount()), "未执行", typeRateEntity.getRate());
    		}
    	}
    	return dataset; 
    }
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
			typeRateEntity.setRate(typeRateEntity.getDepart()+ ":依从率" + getPercent(rightSum,rightSum+wrongSum) + "%");
			typeRateEntityList.add(typeRateEntity);
		}
	}
	
	/***********************************图片jfreechart导出*********************************/
	
	/**
	 * 
	 * @param mapTitle { 
	 * ("fileName",String), 文件名：类型+时间戳
	 * ("title",String), jfreeChart标题
	 * ("xStr",String),  X轴说明
	 * ("yStr",String),  Y轴说明
	 * {"location",String}}   jfreeChart生成图至本地的位置
	 * @param dataset
	 */
    public  void jfreeTest(Map<String,String>mapTitle,CategoryDataset dataset) {  
//        // 1. 得到数据  
//        CategoryDataset dataset = getDataSet();  
        // 2. 构造chart  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                mapTitle.get("title"), // 图表标题  
                mapTitle.get("xStr"), // 目录轴的显示标签--横轴  
                mapTitle.get("yStr"), // 数值轴的显示标签--纵轴  
                dataset, // 数据集  
                PlotOrientation.VERTICAL, // 图表方向：水平、  
                true, // 是否显示图例(对于简单的柱状图必须  
                false, // 是否生成工具  
                false // 是否生成URL链接  
                );  
        // 3. 处理chart中文显示问题  
        processChart(chart);  
  
        // 4. chart输出图片  
        writeChartAsImage(chart);  
  
        // 5. chart 以swing形式输出  
//        ChartFrame pieFrame = new ChartFrame("XXX", chart);  
//        pieFrame.pack();  
//        pieFrame.setVisible(true);  
  
    }  
  
    /** 
     * 获取一个演示用的组合数据集对象 
     * 洗手液使用量统计By部门
     * @return 
     */  
    public  CategoryDataset sanitizerGetDataSet() {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        if(mapTitle.get("type")!=null){
        	if("sanitizerByDept".equals(mapTitle.get("type"))){
        		if (departSanitizerList != null && departSanitizerList.size() > 0) {
        			for(DepartRateEntity departRateEntity : departSanitizerList){
        				    int i=0;
        				for(String nameStr : departRateEntity.getNameList()){
        					String rateStr = departRateEntity.getRateList().get(i);
        					rateStr = rateStr.replace("ml", "");
        					//dataset.addValue(data,series,category)
    						dataset.addValue(Integer.valueOf(rateStr), nameStr, departRateEntity.getTime());  
        				    i++;
        				}
        			}
        			
        		}
        		
        	}
        }
        return dataset;  
    }  
    
    /** 
     * 获取一个演示用的组合数据集对象 
     * 人员依从性
     * @return 
     */  
    public  CategoryDataset rateByStaffTypeData() {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        	if (staffEntityList != null && staffEntityList.size() > 0) {
        		for(StaffEntity staffEntity : staffEntityList){
        			dataset.addValue(Integer.valueOf(staffEntity.getRightCount()), "已执行", staffEntity.getName()); 
        			dataset.addValue(Integer.valueOf(staffEntity.getWrongCount()), "未执行", staffEntity.getName());
        		}
        	}
        return dataset;  
    } 
  
  
    /** 
     * 解决图表汉字显示问题 
     *  
     * @param chart 
     */  
    public  void processChart(JFreeChart chart) {  
        CategoryPlot plot = chart.getCategoryPlot();  
        CategoryAxis domainAxis = plot.getDomainAxis();  
        ValueAxis rAxis = plot.getRangeAxis();  
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,  
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);  
        TextTitle textTitle = chart.getTitle();  
        textTitle.setFont(new Font("宋体", Font.PLAIN, 20));  
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));  
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));  
        rAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));  
        rAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));  
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));  
        // renderer.setItemLabelGenerator(new LabelGenerator(0.0));  
        // renderer.setItemLabelFont(new Font("宋体", Font.PLAIN, 12));  
        // renderer.setItemLabelsVisible(true);  
    }  
  
    /** 
     * 输出图片 
     *  
     * @param chart 
     */  
    public void writeChartAsImage(JFreeChart chart) {  
        FileOutputStream fos_jpg = null;  
        String folderPath = setUpFolder();
        try {  
        	String fileNameStr =  java.net.URLEncoder.encode(mapTitle.get("fileName") + ".jpg", "UTF-8");
        	mapTitle.put("location",folderPath+fileNameStr );
            fos_jpg = new FileOutputStream(folderPath+fileNameStr);  
            ChartUtilities.writeChartAsJPEG(fos_jpg, 1, chart, 400, 600, null);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fos_jpg.close();  
            } catch (Exception e) {  
            }  
        }  
    } 
    
    //项目目录下创建存放jfreechart图片的文件夹
    public  String setUpFolder(){
		String webpath=getRequest().getSession().getServletContext().getRealPath("/"); 
//		System.out.println(webpath);
//		System.out.println("目录："+webpath);
		File filePath=new File(webpath+"\\upLoadChart");
		if(!filePath.exists()){
			 File file=new File(webpath+"/upLoadChart/"+File.separator);
			 file.mkdir() ; // 创建文件夹
		}
		else{
			
		}
		 
		return webpath+"upLoadChart\\";
	 }
    
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public void setRfidStatus(String rfidStatus) {
		this.rfidStatus = rfidStatus;
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
	public List<StaffWork> getStaffWorkList() {
		return staffWorkList;
	}
	public void setStaffWorkList(List<StaffWork> staffWorkList) {
		this.staffWorkList = staffWorkList;
	}

	public List<WashHandLog> getWashHandList() {
		return washHandList;
	}

	public void setWashHandList(List<WashHandLog> washHandList) {
		this.washHandList = washHandList;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
   
	//洗手时机列表，里面存放的对象是一个部门的洗手时机entity
	private List<WashHandMoment> washHandMomentCompareList = new ArrayList<WashHandMoment>();
	private List<WashHandLog> washHandMomentList = new ArrayList<WashHandLog>();//洗手时机统计用数据
	
	/**
	 * 计算手卫生时机
	 */
	private void eventWashTimeCalc() {
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
		washHandList = washHandLogService.RateCompare(queryEntity);//查询数据
		washHandMomentList=washHandLogService.EventCompare(queryEntity);//洗手时机对比数据
		//compareWashHandMoment();
		//洗手时机 和rfid映射 :接触病人前洗手
		Map<String,Integer> washTimesBeforeCloseNick = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesBeforeCloseNick = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :无菌操作前洗手
		Map<String,Integer> washTimesBeforeAsepticOperation = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesBeforeAsepticOperation = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :接触病人后洗手
		Map<String,Integer> washTimesAfterCloseNick = new HashMap<String,Integer>();
		Map<String,Integer> notWashTimesAfterCloseNick = new HashMap<String,Integer>();
		//洗手时机 和rfid映射 :接触病人环境后洗手
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
					//notWashBeforeCloseNick++;
				}else if(wash.getRfidStatus() == 1000){//无菌操作前洗手,假设值,暂时用不上
					if(washTimesBeforeAsepticOperation.get(wash.getRfid()) == null ){
						washTimesBeforeAsepticOperation.put(wash.getRfid(), 1);
					}else{
						washTimesBeforeAsepticOperation.put(wash.getRfid(), 
								washTimesBeforeAsepticOperation.get(wash.getRfid() )+1);
					}
					//washBeforeAsepticOperation++;
				}else if(wash.getRfidStatus()== 10000 ){//无菌操作前未洗手,假设值,暂时用不上
					
					if(notWashTimesBeforeAsepticOperation.get(wash.getRfid()) == null ){
						notWashTimesBeforeAsepticOperation.put(wash.getRfid(), 1);
					}else{
						notWashTimesBeforeAsepticOperation.put(wash.getRfid(), 
								notWashTimesBeforeAsepticOperation.get(wash.getRfid())+1);
					}
					//notWashBeforeAsepticOperation++;
				}else if(wash.getRfidStatus() == 12){//接触病人后洗手
					
					if(washTimesAfterCloseNick.get(wash.getRfid()) == null ){
						washTimesAfterCloseNick.put(wash.getRfid(), 1);
					}else{
						washTimesAfterCloseNick.put(wash.getRfid(), 
								washTimesAfterCloseNick.get(wash.getRfid())+ 1 );
					}
					
					//washAfterCloseNick++;
				}else if(wash.getRfidStatus() == 13){//接触病人后未洗手
					if(notWashTimesAfterCloseNick.get(wash.getRfid()) == null ){
						notWashTimesAfterCloseNick.put(wash.getRfid(), 1);
					}else{
						notWashTimesAfterCloseNick.put(wash.getRfid(), 
								notWashTimesAfterCloseNick.get(wash.getRfid()) + 1 );
					}
					
					//notWashAfterCloseNick++;
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
				
				if(noWashACloseNick  != null ){
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
				
				if(noWashACloseNickEnvri  != null ){
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

			int washACloseNickTotal = 
					washHandMoment.getWashAfterCloseNickTimes() + washHandMoment.getNotWashAfterCloseNickTimes();

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
				washHandMoment.setRateWashBeforeAsepticOperation("0.0");
			}
			
			if(washHandMoment.getWashAfterCloseNickTimes() != 0){
				washHandMoment.setRateWashAfterCloseNick(getPercent(washHandMoment.getWashAfterCloseNickTimes(),
						washACloseNickTotal));
			}else{
				washHandMoment.setRateWashAfterCloseNick("0.0");
			}
			
			if(washHandMoment.getWashAfterCloseNickEnvriTimes() != 0 ){
				washHandMoment.setRateWashAfterCloseNickEnvri(getPercent(washHandMoment.getWashAfterCloseNickEnvriTimes(), 
						washACloseNickEnvriTotal));
			}else{
				washHandMoment.setRateWashAfterCloseNickEnvri("0.0");
			}

			washHandMomentCompareList.add(washHandMoment);
		}
		
	}

	public String getWashHandStatus() {
		return washHandStatus;
	}

	public void setWashHandStatus(String washHandStatus) {
		this.washHandStatus = washHandStatus;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
}
	

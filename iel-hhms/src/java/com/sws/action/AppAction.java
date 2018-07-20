package com.sws.action;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.gk.essh.util.tree.TreeNode;
import com.gk.essh.www.session.GkWebSession;
import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.entity.AllManualResults;
import com.sws.common.entity.DepartInfo;
import com.sws.common.entity.DepartmentStatistic;
import com.sws.common.entity.EventCompare;
import com.sws.common.entity.OrganisationStatistic;
import com.sws.common.entity.StaffEntity;
import com.sws.common.entity.StaffStatistic;
import com.sws.common.entity.StaffWork;
import com.sws.common.entity.TypeRateEntity;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.CalculationOfRate;
import com.sws.common.until.DateUtils;
import com.sws.common.until.MessageDigestUtils;
import com.sws.common.until.StringUtil;
import com.sws.dao.DeviceInfoDao;
import com.sws.dao.StaffInfoDao;
import com.sws.dao.UserInfoDao;
import com.sws.model.DeviceInfo;
import com.sws.model.GroupTree;
import com.sws.model.ManualRecord;
import com.sws.model.ParameterInfo;
import com.sws.model.StaffInfo;
import com.sws.model.UserInfo;
import com.sws.model.WashHandLog;
import com.sws.service.AppNewsService;
import com.sws.service.DeviceInfoService;
import com.sws.service.ManualRecordService;
import com.sws.service.StaffInfoService;
import com.sws.service.WashHandLogService;
import com.sys.core.util.bean.Page;
import com.sys.core.util.cm.ConfigManager;

public class AppAction extends BaseAction<DeviceInfo>{

	private static final long serialVersionUID = 6231651804739519037L;
	private Map<String,Object> dataMap = new HashMap<String, Object>();
	@Autowired
	private WashHandLogService washHandLogService;
	@Autowired
	private DeviceInfoService deviceInfoService;
	@Autowired
	private AppNewsService appNewsService;
	@Autowired
    private StaffInfoDao staffInfoDao;
	@Autowired
    private DeviceInfoDao deviceInfoDao;
	@Autowired
    private UserInfoDao userInfoDao;
	@Autowired
	private ManualRecordService manualRecordService;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	private Long oneDay = 1*24*3600*1000L;
	
	private int _rowNum = 30;

	private List<WashHandLog> washHandList = new ArrayList<WashHandLog>();
	private Map<String,Integer> mapRight = new HashMap<String,Integer>();//正常----或者接触前
	private Map<String,Integer> mapWrong = new HashMap<String,Integer>();//违规----或者接触后
	
	//add by yxnne 增加几个表示洗手时机的
	private Map<String,Integer> mapWash0007 = new HashMap<String,Integer>();//时机0007
	private Map<String,Integer> mapWash0003 = new HashMap<String,Integer>();//时机0003
	private Map<String,Integer> mapWash0103 = new HashMap<String,Integer>();//时机0103
	private Map<String,Integer> mapWash0110 = new HashMap<String,Integer>();//时机0110
	private Map<String,Integer> mapWash0008 = new HashMap<String,Integer>();//时机0008
	//add by yxnne 增加洗手  "两前两后" 的
	//接触病人前洗手
	private Map<String,Integer> washTimesBeforeCloseNick = new HashMap<String,Integer>();
	private Map<String,Integer> notWashTimesBeforeCloseNick = new HashMap<String,Integer>();
	//无菌操作前洗手
	private Map<String,Integer> washTimesBeforeAsepticOperation = new HashMap<String,Integer>();
	private Map<String,Integer> notWashTimesBeforeAsepticOperation = new HashMap<String,Integer>();
	//接触病人后洗手
	private Map<String,Integer> washTimesAfterCloseNick = new HashMap<String,Integer>();
	private Map<String,Integer> notWashTimesAfterCloseNick = new HashMap<String,Integer>();
	//接触病人环境后洗手
	private Map<String,Integer> washTimesAfterCloseNickEnvri = new HashMap<String,Integer>();
	private Map<String,Integer> notWashTimesafterCloseNickEnvri = new HashMap<String,Integer>();
	private DecimalFormat df = new DecimalFormat("#.0");		
	
	private List<StaffWork> staffWorkList = new ArrayList<StaffWork>();//人员依从性计算
	private List<StaffEntity> staffEntityList = new ArrayList<StaffEntity>();
	private List<EventCompare>	eventCompareList = new ArrayList<EventCompare>();//事件比较
	private List<TypeRateEntity> typeRateEntityList = new ArrayList<TypeRateEntity>();
	
	private String userName;
	private String passWord;
	private String newPassWord;
	private String rfid;
	private String pageNum;
	private String treeId;
	private String staffId;
	private String eventType;
	private String rfidStatus;
	private String startTime;
	private String endTime;
	private String groupTreeId;
	private String depart;
	private String number;
	private String name;
	private String type;
	private String departIds;
	private String jobType;
	private List<WashHandLog> washHandMomentList;
	
	//人工录入手卫生记录接口所需参数
	private ManualRecord manualRecord = new ManualRecord();
	private String departId;
	private String observerName;
	private String time;
	private String occassion;
	private String way;
	private String isRight;
	private String roleId;
	private String observerNumber;
	private String isWashHand;
	private String timeStart;
	private String timeEnd;
	
	private String departmentId;
	
	private List<String> departmentIds;
	
	public List<String> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<String> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * 一定时间段内该科室下的不同人员类别的手卫生次数统计
	 * @return
	 */
	public void findWashTimesLineDrawing(){
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
		writeResponse(obj.toString());
	}
	
	private void mapProcess(){
		for(WashHandLog wash:washHandList){
			if(wash.getEventType().equals("0003")){ 
				//有效洗手数
				/**
				 * 修改，从有效手卫生中删除rfidStatus为2，即清洁状态下的手卫生
				 * 删除的代码：
				 * wash.getRfidStatus().equals(2)||
				 * update by wwg 2018/1/17
				 */
				if(wash.getRfidStatus().equals(3)||wash.getRfidStatus().equals(6)||wash.getRfidStatus().equals(9)||
						wash.getRfidStatus().equals(10)||wash.getRfidStatus().equals(12)){
					if(wash.getRfid()!=null){
						if(mapRight.get(wash.getRfid())==null){
							mapRight.put(wash.getRfid(), 1);
						}else{
							mapRight.put(wash.getRfid(),mapRight.get(wash.getRfid())+1);//只会更新value的值
						}
					}
				}
			}else{
				if(wash.getRfid()!=null){
					if(mapWrong.get(wash.getRfid())==null){
						mapWrong.put(wash.getRfid(), 1);
					}else{
						mapWrong.put(wash.getRfid(),mapWrong.get(wash.getRfid())+1);//只会更新value的值
					}
				}
			}
		}
	}

	/**
	 * 创建session信息
	 */
	private void createSession(UserInfo userInfo) {
		GkWebSession session = new GkWebSession();
		session.setAttribute(GkWebSession.KEY_SESSION_ID, getRequest().getSession().getId());	// "session"
		session.setAttribute(GkWebSession.KEY_SESSION_USER, userInfo.getId().toString());	// 
		session.setAttribute(GkWebSession.KEY_SESSION_NAME, userInfo.getName());	// "name"
		//注意字符串中的不能出现‘，’这里转成*
		session.setAttribute(GkWebSession.KEY_SESSION_ORG, userInfo.getDepartIds().replace(",", "*")); //所属科室 
		String expire = ConfigManager.getConfiguration("web-session", "session.expire");
		mobileSetSession(session, Integer.parseInt(expire.trim()) * 60 * 1000);
	}
	
	/**
	 * 登录	
	 * @return
	 */
	public String appLogin(){
		String MD5pass = MessageDigestUtils.getMD5(passWord);
		UserInfo userInfo = userInfoDao.findUniqueBy("name", userName);
		if(userInfo == null || !userInfo.getPassword().equals(MD5pass)){
			dataMap.put("success", false);
			return SUCCESS;
		}
		if(userInfo.getRfId() != null){
			DeviceInfo deviceInfo = deviceInfoService.findByNoType(userInfo.getRfId(), "40");
			dataMap.put("rfid", userInfo.getRfId());
			dataMap.put("deviceName", deviceInfo.getName());
			dataMap.put("department", deviceInfo.getGroupTree().getName());
			dataMap.put("userName", userInfo.getName());
			dataMap.put("departIds", userInfo.getDepartIds());
	        dataMap.put("success", true);	
			return SUCCESS;
		}
		dataMap.put("rfid", "");
		dataMap.put("deviceName", "");
		if(userInfo.getDepartIds().equals("1")){//全院
			dataMap.put("department","全院");
		}else{
			dataMap.put("department", userInfo.getGroupTree().getName());
		}
		
		dataMap.put("userName", userInfo.getName());
		dataMap.put("departIds", userInfo.getDepartIds());
        dataMap.put("success", true);
//        createSession(userInfo);//创建session
		return SUCCESS;
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	public String modifyPassword(){
		String MD5pass = MessageDigestUtils.getMD5(passWord);
		UserInfo userInfo = userInfoDao.findUniqueBy("name", userName);
		if(!userInfo.getPassword().equals(MD5pass)){
			dataMap.put("success", false);
			return SUCCESS;
		}
		String MD5pass1 = MessageDigestUtils.getMD5(newPassWord);
		userInfo.setPassword(MD5pass1);
		userInfoService.update(userInfo);
		dataMap.put("success", true);
		return SUCCESS;
	}
	
	/**
	 * 注册
	 * @return
	 */
	public String register(){
		DeviceInfo deviceInfo = deviceInfoService.findByNoType(rfid, "40");
		if(deviceInfo == null){
			dataMap.put("success", false);
			dataMap.put("Msg", "该胸牌号不存在。");
			return SUCCESS;
		}
		UserInfo userInfo = userInfoDao.findUniqueBy("rfId", rfid);
		if(userInfo == null){
			UserInfo newUser = new UserInfo();
			newUser.setName(userName);
			newUser.setPassword(MessageDigestUtils.getMD5(passWord));
			newUser.setRfId(rfid);
			userInfoService.save(newUser);
			dataMap.put("success", true);
			return SUCCESS;
		}
		dataMap.put("success", false);
		dataMap.put("Msg", "该胸牌已注册过。");
		return SUCCESS;
	}
	
	/**
	 * 找回密码
	 * @return
	 */
	public String retrievePassWord(){
		UserInfo userInfo = userInfoDao.findUniqueBy("rfId", rfid);
		if(userInfo ==null ){
			dataMap.put("success", false);
			dataMap.put("Msg", "您输入的胸牌号有误。");
			return SUCCESS;
		}
		if(userInfo !=null && !userInfo.getName().equals(userName)){
			dataMap.put("success", false);
			dataMap.put("Msg", "您输入的胸牌号与你的用户名不匹配。");
			return SUCCESS;
		}
		userInfo.setPassword(MessageDigestUtils.getMD5(passWord));
		userInfoService.update(userInfo);
		dataMap.put("success", true);
		return SUCCESS;
	}
	
	/**
	 * 获取所有的部门
	 * @return
	 */
	public String getDepartment(){
		treeNode = groupTreeService.getTree(getManageDepart(),departIds, true,isTreeOpen);
		Map<String,Object> departMap = new HashMap<String, Object>();
		
		for(TreeNode tn : treeNode.getChildren()){
			departMap.put(tn.getId(), tn.getLabel());
		}
		dataMap.put("departMap", departMap);
		return SUCCESS;
	}
	
	/**
	 * add by yxnne 
	 * 获取所有的部门
	 * @return
	 */
	public String getDepartments(){
		treeNode = groupTreeService.getTree(getManageDepart(),departIds, true,isTreeOpen);
		List<DepartInfo> depaertLists = new ArrayList<DepartInfo>();
		for(TreeNode tn : treeNode.getChildren()){
			//departMap.put(tn.getId(), tn.getLabel());
			DepartInfo depart = new DepartInfo();
			depart.setDepartId(tn.getId());
			depart.setDepartName(tn.getLabel());
			depaertLists.add(depart);
		}
		dataMap.put("depaertLists", depaertLists);
		return SUCCESS;
	}
	
	/**
	 * 获取部门下的医生
	 * @return
	 */
	public String getStaffList(){
		Map<String,Object> staffNameMap = new HashMap<String, Object>();
		String departmentId = ServletActionContext.getRequest().getParameter("groupTreeId");
		List<StaffInfo> staffList = staffInfoDao.findByDepartAndType(departIds,Long.valueOf(departmentId),null);
		for(StaffInfo info:staffList){
			staffNameMap.put(info.getId().toString(), info.getName());
		}
		dataMap.put("staffNameMap", staffNameMap);
		return SUCCESS;
	}
	
	/**
	 * 根据科室+人员类别得到人员
	 * @return
	 */
	public String getStaffByType(){
		Map<String,Object> staffNameMap = new HashMap<String, Object>();
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		String staffType = ServletActionContext.getRequest().getParameter("staffType");
		String departmentId = ServletActionContext.getRequest().getParameter("groupTreeId");
		if(staffType!=null){
			List<String> typeList = new ArrayList<String>();
			String[] staffTypeArray = staffType.split(",");//人员类别名称
			for(String type:staffTypeArray){
				typeList.add(type);
			}
			if(StringUtil.isNotBlank(departmentId)){
				staffList = staffInfoDao.findByDepartAndType(departIds,Long.valueOf(departmentId),typeList);
			}
			for(StaffInfo info:staffList){
				staffNameMap.put(info.getId().toString(), info.getName());
			}
			dataMap.put("staffNameMap", staffNameMap);
		}
		return SUCCESS;
	}
	
	/**
	 * 进入事件查询列表
	 * @return
	 */
	public String eventList(){
		dataMap.put("eventType", SysStatics.eventTypeMap);
		dataMap.put("rfidStatus",SysStatics.rfidStatusMap);
		return SUCCESS;
	}
	
	/**
	 * 事件查询
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String eventSelect() throws ParseException{
		if(pageNum != null){
			_page = Integer.valueOf(pageNum) ;
		}
		queryEntity.setTreeId(treeId);
		queryEntity.setInt1(Integer.valueOf(staffId));
		queryEntity.setStr1(eventType);
		queryEntity.setStr2(rfidStatus);
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		String deviceStr=""; 
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		if(queryEntity.getInt1()!=null&&!queryEntity.getInt1().equals(-1)){
			rfidList.add(getRfidByStaffId(Long.valueOf(queryEntity.getInt1())));
		}else{
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),null);
			}
			for(StaffInfo info:staffList){
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			//modified by yxnne 
			//page = washHandLogService.pageByEvent(queryEntity, _page, _rowNum, "rfid","asc");
			page = washHandLogService.pageByEvent(queryEntity, _page, _rowNum, "createTime","desc");
			for(WashHandLog log:(List<WashHandLog>)page.getResult()){
				if(log.getRfid()!=null){
					log.setDocName(getDocByRfid(log.getRfid()));
					log.setDepartName(getDepartNameByRfid(log.getRfid()));
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
			}
			page = new Page(_page, _rowNum, page.getResult(),  page.getTotal());
		}else{
			page = new Page(0, _rowNum, null,  0);
		}
		dataMap.put("page", page);
		return SUCCESS;
	}
	
	/**
	 * 进入人员依从性列表
	 * @return
	 */
	public String rateByStaffList(){
		Map<String, String> typeMap = getJobTypeMap();
		dataMap.put("jobType", typeMap);
		return SUCCESS;
	}
	
	/**
	 * 人员依从性查询
	 * @return
	 */
	public String rateByStaffSelect(){
		if(pageNum != null){
			_page = Integer.valueOf(pageNum) ;
		}
		queryEntity.setTreeId(treeId);
		queryEntity.setInt1(Integer.valueOf(staffId));
		queryEntity.setStr1(jobType);
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		StaffWork staffWork = null;
		GroupTree departTree =null;
		List<StaffInfo>	staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		Map<String, String> typeMap = getJobTypeMap();
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
					staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),typeList);
				}
				for(StaffInfo info:staffList){
					rfidList.add(getRfidByStaffId(info.getId()));
				}
			}
		}
		queryEntity.setRfidList(rfidList);
		washHandList = washHandLogService.RateCompare(queryEntity);
		//为了洗手时机依从率
		washHandMomentList=washHandLogService.EventCompare(queryEntity);//洗手时机对比数据
		mapProcess();//map数据封装
		mapMoment();//add yxnne 2017/5/3 调用这个方法，封装下时机的map数据
		mapBeforeTwoAfterTwoDepart();//add yxnne 2017/5/10 封装下洗手时机依从率需要的次数数据
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
						staffWork.setDocType(typeMap.get(staffInfo.getCategory()));
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
			//add yxnne 2017/5/3 for 洗手时机
			if(mapWash0003.get(rfid) != null){
				staffWork.setNum0003(mapWash0003.get(rfid));
			}else{
				staffWork.setNum0003(0);
			}
			
			if(mapWash0007.get(rfid) != null){
				staffWork.setNum0007(mapWash0007.get(rfid));
			}else{
				staffWork.setNum0007(0);
			}
			
			if(mapWash0110.get(rfid) != null){
				staffWork.setNum0110(mapWash0110.get(rfid));
			}else{
				staffWork.setNum0110(0);
			}
			
			if(mapWash0103.get(rfid) != null){
				staffWork.setNum0103(mapWash0103.get(rfid));
			}else{
				staffWork.setNum0103(0);
			}
			
			if(mapWash0008.get(rfid) != null){
				staffWork.setNum0008(mapWash0008.get(rfid));
			}else{
				staffWork.setNum0008(0);
			}
			
			//add yxnne for 洗手时机依从率
			
			int washBeforeCloseNick =
					washTimesBeforeCloseNick.get(rfid)==null? 0:washTimesBeforeCloseNick.get(rfid);
			int notWashBeforeCloseNick = 
					notWashTimesBeforeCloseNick.get(rfid) == null? 0 : notWashTimesBeforeCloseNick.get(rfid);
			
			int washBeforeAsepticOperation =
					washTimesBeforeAsepticOperation.get(rfid) == null ? 0 : washTimesBeforeAsepticOperation.get(rfid) ;
			int notWashBeforeAsepticOperation = 
					notWashTimesBeforeAsepticOperation.get(rfid) == null ? 0: notWashTimesBeforeAsepticOperation.get(rfid);
			
			int washAfterCloseNick  = 
					washTimesAfterCloseNick.get(rfid) == null ? 0 : washTimesAfterCloseNick.get(rfid) ;
			int notWashAfterCloseNick = 
					notWashTimesAfterCloseNick.get(rfid) == null ? 0 : notWashTimesAfterCloseNick.get(rfid) ; 
			
			int washAfterCloseNickEnvri = 
					washTimesAfterCloseNickEnvri.get(rfid) == null ? 0 : washTimesAfterCloseNickEnvri.get(rfid);
			int notWashafterCloseNickEnvri = 
					notWashTimesafterCloseNickEnvri.get(rfid) == null ? 0 :  notWashTimesafterCloseNickEnvri.get(rfid);
			
			staffWork.setRateBeforeCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeCloseNick, washBeforeCloseNick+notWashBeforeCloseNick)))));
			staffWork.setRateBeforeAsepticOperation(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeAsepticOperation, washBeforeAsepticOperation+notWashBeforeAsepticOperation)))));
			staffWork.setRateAfterCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNick, washAfterCloseNick+notWashAfterCloseNick)))));
			staffWork.setRateAfterCloseNickEnvri(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNickEnvri, washAfterCloseNickEnvri+notWashafterCloseNickEnvri)))));
		
			staffWork.setRate(getPercent(staffWork.getNormalNum(),staffWork.getNormalNum()+staffWork.getErrorNum()));
			staffWorkList.add(staffWork);
		}
		//add yxnne 2017/5/3 for staff rank by rate 
		if(staffWorkList.size() != 0){
			Collections.sort(staffWorkList);
			for(int i = 0 ; i < staffWorkList.size() ; i++){
				staffWorkList.get(i).setRank(i+1);
			}
			
		}
		
		page = new Page(_page, _rowNum, staffWorkList, staffWorkList.size());
		dataMap.put("page", page);
		return SUCCESS;
	}
	
	/**
	 * 进入人员类别依从性列表
	 * @return
	 */
	public String rateByStaffTypeList(){
		Map<String, String> typeMap = getJobTypeMap();
		dataMap.put("jobType",  typeMap);
		return SUCCESS;
	}
	
	/**
	 * 人员类别依从性查询
	 * @return
	 */
	public String rateByStaffTypeSelect(){
		rateByStaffType();
		page = new Page(_page, rowNum, staffEntityList, staffEntityList.size());
		dataMap.put("page", page);
//		dataMap.put("staffEntityList", staffEntityList);
		return SUCCESS;
	}
	
	private void rateByStaffType(){
		String rfidStr;
		StaffEntity staffEntity=null;
		Integer ringtCount=null,wrongCount=null;
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		//得到所有的员工类别
		Map<String, String> typeMap = getJobTypeMap();
		queryEntity.setTreeId(treeId);
		queryEntity.setStr1(staffType);
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		if(StringUtil.isNotBlank(queryEntity.getStr1())){
			String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String staffType:staffTypeArray){
				typeList.add(staffType);
			}
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				//根据TreeId查询对应的员工列表
				staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),typeList);
			}
			for(StaffInfo info:staffList){
				//通过员工ID得到胸牌ID，并且添加到rfidList中
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
	 * IOS端的接口，手卫生依从率的曲线图接口
	 * @author wwg
	 */
	@SuppressWarnings("static-access")
	public String rateByStaffTypeIOS(){
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		//得到所有的员工类别
		Map<String, String> typeMap = getJobTypeMap();
		queryEntity.setTreeId(treeId);
		queryEntity.setStr1(staffType);
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		if(StringUtil.isNotBlank(queryEntity.getStr1())){
			String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String staffType:staffTypeArray){
				typeList.add(staffType);
			}
			if(StringUtil.isNotBlank(queryEntity.getTreeId())){
				//根据TreeId查询对应的员工列表
				staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),typeList);
			}
			for(StaffInfo info:staffList){
				//通过员工ID得到胸牌ID，并且添加到rfidList中
				rfidList.add(getRfidByStaffId(info.getId()));
			}
		}
		queryEntity.setRfidList(rfidList);
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
		}
		Date startDate = queryEntity.getStartTime();
		//对开始时间取整
		String startStr = DateUtils.dateToString(startDate);
		//将String类型的时间转回Date
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			startDate = sf.parse(startStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = queryEntity.getEndTime();
		int days = DateUtils.daysOfTwo(startDate, endDate);
		List<WashHandLog> list1 = new ArrayList<WashHandLog>();
		List<WashHandLog> list2 = new ArrayList<WashHandLog>();
		List<WashHandLog> list3 = new ArrayList<WashHandLog>();
		List<WashHandLog> list4 = new ArrayList<WashHandLog>();
		List<WashHandLog> list5 = new ArrayList<WashHandLog>();
		List<WashHandLog> list6 = new ArrayList<WashHandLog>();
		List<WashHandLog> list7 = new ArrayList<WashHandLog>();
		//days+1是因为要显示多一天的
		for (int i = 0; i < days + 1; i++) {
			for (int j = 0; j < washHandList.size(); j++) {
				WashHandLog washHandLog = washHandList.get(j);
				//数据库中的事件时间
				Date dateEvent = washHandLog.getCreateTime();
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(startDate);
			    //把日期往后增加i天.整数往后推,负数往前移动
			    calendar.add(calendar.DATE, i);
			    Date dateAddOneBefore = calendar.getTime();
			    calendar.add(calendar.DATE, 1); 
			    Date dateAddOne = calendar.getTime();
				if(dateEvent.before(dateAddOne) && dateEvent.after(dateAddOneBefore)){
					switch (i+1) {
					case 1:
						list1.add(washHandLog);
						break;
					case 2:
						list2.add(washHandLog);
						break;
					case 3:
						list3.add(washHandLog);
						break;
					case 4:
						list4.add(washHandLog);
						break;
					case 5:
						list5.add(washHandLog);
						break;
					case 6:
						list6.add(washHandLog);
						break;
					case 7:
						list7.add(washHandLog);
						break;
					}
				}
			}
		}
		Map<String, List<StaffEntity>> map = new HashMap<String, List<StaffEntity>>();
		//进行是否依从手卫生的判定
		washHandList = list1;
		mapProcess();
		List<StaffEntity> list = new ArrayList<StaffEntity>();
		list = getstaffEntity(list1, typeList, staffList, typeMap);
		map.put("1", list);
		washHandList = list2;
		mapProcess();
		list = getstaffEntity(list2, typeList, staffList, typeMap);
		map.put("2", list);
		washHandList = list3;
		mapProcess();
		list = getstaffEntity(list3, typeList, staffList, typeMap);
		map.put("3", list);
		washHandList = list4;
		mapProcess();
		list = getstaffEntity(list4, typeList, staffList, typeMap);
		map.put("4", list);
		washHandList = list5;
		mapProcess();
		list = getstaffEntity(list5, typeList, staffList, typeMap);
		map.put("5", list);
		washHandList = list6;
		mapProcess();
		list = getstaffEntity(list6, typeList, staffList, typeMap);
		map.put("6", list);
		washHandList = list7;
		mapProcess();
		list = getstaffEntity(list7, typeList, staffList, typeMap);
		map.put("7", list);
		dataMap.put("map", map);
		return SUCCESS;
	}
	
	
	/**
	 * 通过mapProcess设置right和wrong的值，然后调用该方法计算依从率，得到依从率
	 * @param list
	 * @param typeList
	 * @param staffList
	 * @param typeMap
	 * @return
	 * @author wwg
	 */
	public List<StaffEntity> getstaffEntity(List<WashHandLog> list,
			List<String> typeList,List<StaffInfo> staffList,
			Map<String, String> typeMap){
		List<StaffEntity> iosStaffList = new ArrayList<StaffEntity>();
		String rfidStr;
		StaffEntity staffEntity=null;
		Integer ringtCount=null,wrongCount=null;
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
			iosStaffList.add(staffEntity);
		}
		return iosStaffList;
	}
	
	
	/**
	 * 部门依从率统计查询
	 * @return
	 */
	public String rateByDepartListSelect(){
		//add by yxnne for whole hospital 
		if(depart.equals("1")){
			//是 1 的时候就是全院数据
			StringBuilder sbDepart = new StringBuilder("");
			treeNode = groupTreeService.getTree(getManageDepart(),"1", true,isTreeOpen);
			for(TreeNode tn : treeNode.getChildren()){
				sbDepart.append(tn.getId());
				sbDepart.append(",");	 
			}
			String strDepart = sbDepart.substring(0, sbDepart.lastIndexOf(","));
			queryEntity.setStr1(strDepart);
		}else{
			queryEntity.setStr1(depart);
		}
		
		
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		rateByDepart();	
		page = new Page(_page, rowNum, typeRateEntityList, typeRateEntityList.size());
		dataMap.put("page", page);
		return SUCCESS;
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
		if(StringUtil.isNotBlank(queryEntity.getStr1())){   
			//科室
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
		//closingPatientNotWashHand接近患者未手卫生
		int cpnw = washHandLogService.findLogsNotWashHangBeforeClosingPatient(queryEntity);
		if(rfidList.size()>0){
			washHandList = washHandLogService.RateCompare(queryEntity);
			washHandMomentList=washHandLogService.EventCompare(queryEntity);//洗手时机对比数据
		}
		
		mapProcess();//map数据
		mapBeforeTwoAfterTwoDepart();//获得"两前两后"的次数数据
		//add by yxnne 2017-5-9 
		//当科室id是1的时候，希望得到全部医院的依从率，这时候不想按科室分类
		if(depart.equals("1")){//全院的数据
			
			calcWholeHospitalRate(staffList, cpnw, cpiacwp);
			return;
		}
		for(Long departId:departList){
			mapTypeRight = new TreeMap<String,Integer>();
			mapTypeWrong = new TreeMap<String,Integer>();
			
			//add by yxnne
			//计算洗手时机的依从率，封装到typeRateEntity对象中
			int washBeforeCloseNick = 0;
			int notWashBeforeCloseNick = 0;
			int washBeforeAsepticOperation = 0;
			int notWashBeforeAsepticOperation = 0;
			int washAfterCloseNick  = 0;
			int notWashAfterCloseNick = 0;
			int washAfterCloseNickEnvri = 0;
			int notWashafterCloseNickEnvri = 0;
			
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
						
						//add by yxnne
						//for 计算洗手时机
						washBeforeCloseNick += 
								(washTimesBeforeCloseNick.get(rfidStr) == null ? 0 : washTimesBeforeCloseNick.get(rfidStr));
						notWashBeforeCloseNick += 
								(notWashTimesBeforeCloseNick.get(rfidStr) == null ? 0 : notWashTimesBeforeCloseNick.get(rfidStr));
						
						washBeforeAsepticOperation += 
								(washTimesBeforeAsepticOperation.get(rfidStr) == null ? 0 : washTimesBeforeAsepticOperation.get(rfidStr));
						notWashBeforeAsepticOperation += 
								(notWashTimesBeforeAsepticOperation.get(rfidStr) == null ? 0 : notWashTimesBeforeAsepticOperation.get(rfidStr));
						
						washAfterCloseNick += 
								(washTimesAfterCloseNick.get(rfidStr) == null ? 0 : washTimesAfterCloseNick.get(rfidStr));
						notWashAfterCloseNick += 
								(notWashTimesAfterCloseNick.get(rfidStr) == null ? 0 :notWashTimesAfterCloseNick.get(rfidStr));
						
						washAfterCloseNickEnvri += (washTimesAfterCloseNickEnvri.get(rfidStr) == null ? 0 :washTimesAfterCloseNickEnvri.get(rfidStr));
						notWashafterCloseNickEnvri += (notWashTimesafterCloseNickEnvri.get(rfidStr) == null ? 0 :notWashTimesafterCloseNickEnvri.get(rfidStr));
						
						
					}
				}
			}
			//add  by yxnne
			//计算洗手时机
			 
			typeRateEntity.setRateBeforeCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeCloseNick, washBeforeCloseNick+notWashBeforeCloseNick)))));
			typeRateEntity.setRateBeforeAsepticOperation(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeAsepticOperation, washBeforeAsepticOperation+notWashBeforeAsepticOperation)))));
			typeRateEntity.setRateAfterCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNick, cpnw + cpiacwp + notWashAfterCloseNick)))));
			typeRateEntity.setRateAfterCloseNickEnvri(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNickEnvri, washAfterCloseNickEnvri+notWashafterCloseNickEnvri)))));
			
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
			typeRateEntity.setRate(typeRateEntity.getDepart()+ ":依从率"+ getPercent(rightSum,rightSum+wrongSum) + "%");
			
		
			typeRateEntityList.add(typeRateEntity);
		}
	}
	
	/**
	 *得到全院的数据 
	 *add by yxnne 
	 */
	private void calcWholeHospitalRate(List<StaffInfo> staffList, int cpnw, int cpiacwp) {
		//各种声明
		String rfidStr,rate;
		Integer rightCount=null,wrongCount=null,pepleNum=null,rightSum=0,wrongSum=0;
		Map<String,Integer> mapTypeRight = new TreeMap<String,Integer>();//正常----或者接触前
		Map<String,Integer> mapTypeWrong = new TreeMap<String,Integer>();//违规----或者接触后
		Map<String,Integer> mapPeple = new TreeMap<String,Integer>();//初始化map值 //部门下面的人员数目
		
		int washBeforeCloseNick = 0;
		int notWashBeforeCloseNick = 0;
		int washBeforeAsepticOperation = 0;
		int notWashBeforeAsepticOperation = 0;
		int washAfterCloseNick  = 0;
		int notWashAfterCloseNick = 0;
		int washAfterCloseNickEnvri = 0;
		int notWashafterCloseNickEnvri = 0;
		//遍历每一位员工
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
					
					//add by yxnne
					//for 计算洗手时机
					washBeforeCloseNick += 
							(washTimesBeforeCloseNick.get(rfidStr) == null ? 0 : washTimesBeforeCloseNick.get(rfidStr));
					notWashBeforeCloseNick += 
							(notWashTimesBeforeCloseNick.get(rfidStr) == null ? 0 : notWashTimesBeforeCloseNick.get(rfidStr));
					
					washBeforeAsepticOperation += 
							(washTimesBeforeAsepticOperation.get(rfidStr) == null ? 0 : washTimesBeforeAsepticOperation.get(rfidStr));
					notWashBeforeAsepticOperation += 
							(notWashTimesBeforeAsepticOperation.get(rfidStr) == null ? 0 : notWashTimesBeforeAsepticOperation.get(rfidStr));
					
					washAfterCloseNick += 
							(washTimesAfterCloseNick.get(rfidStr) == null ? 0 : washTimesAfterCloseNick.get(rfidStr));
					notWashAfterCloseNick += 
							(notWashTimesAfterCloseNick.get(rfidStr) == null ? 0 :notWashTimesAfterCloseNick.get(rfidStr));
					
					washAfterCloseNickEnvri += (washTimesAfterCloseNickEnvri.get(rfidStr) == null ? 0 :washTimesAfterCloseNickEnvri.get(rfidStr));
					notWashafterCloseNickEnvri += (notWashTimesafterCloseNickEnvri.get(rfidStr) == null ? 0 :notWashTimesafterCloseNickEnvri.get(rfidStr));
					
					
				}
			}
		}
		Map<String, String> typeMap = getJobTypeMap();
		TypeRateEntity typeRateEntity = new TypeRateEntity();
		typeRateEntity.setDepart("全院");
		typeRateEntity.setRateBeforeCloseNick(Float.valueOf(df.format(
				Float.valueOf(getPercent(washBeforeCloseNick, washBeforeCloseNick+notWashBeforeCloseNick)))));
		Float rateBeforeAsepticOperation = Float.valueOf(df.format(
				Float.valueOf(getPercent(washBeforeAsepticOperation, washBeforeAsepticOperation+notWashBeforeAsepticOperation))));
		//判断无菌操作依从率的值是否为0，是则改为100%
		if(rateBeforeAsepticOperation < 0.01) {
			typeRateEntity.setRateBeforeAsepticOperation(100);
		} else {
			typeRateEntity.setRateBeforeAsepticOperation(rateBeforeAsepticOperation);
		}
//		typeRateEntity.setRateBeforeAsepticOperation(Float.valueOf(df.format(
//				Float.valueOf(getPercent(washBeforeAsepticOperation, washBeforeAsepticOperation+notWashBeforeAsepticOperation)))));
		typeRateEntity.setRateAfterCloseNick(Float.valueOf(df.format(
				Float.valueOf(getPercent(washAfterCloseNick, notWashAfterCloseNick + cpnw + cpiacwp)))));
		typeRateEntity.setRateAfterCloseNickEnvri(Float.valueOf(df.format(
				Float.valueOf(getPercent(washAfterCloseNickEnvri, washAfterCloseNickEnvri+notWashafterCloseNickEnvri)))));
		
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
		typeRateEntity.setRate(typeRateEntity.getDepart()+ ":依从率"+ getPercent(rightSum,rightSum+wrongSum) + "%");
		typeRateEntityList.add(typeRateEntity);
	}

	/**
	 * 事件比较查询
	 * @return
	 */
	public String eventCompareListSelect(){
		queryEntity.setStr1(depart);
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		eventCompare();
		page = new Page(_page, rowNum, eventCompareList, eventCompareList.size());
		dataMap.put("page", page);
		return SUCCESS;
	}
	
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
				if (wash.getRfidStatus().equals(4)|| wash.getRfidStatus().equals(5)) { // 接触前
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
	
	/**
	 * 设备预警查询
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public String queryAlarmListSelect() throws UnsupportedEncodingException{
		if(pageNum != null){
			_page = Integer.valueOf(pageNum) ;
		}
		queryEntity.setTreeId(treeId);
		queryEntity.setStr1(number);
		if(number.equals("null")){
			queryEntity.setStr1("");
		}
		
		String name1 = new String(name.getBytes("ISO-8859-1"),"utf-8");
		queryEntity.setStr2(name1);
		if(name.equals("null")){
			queryEntity.setStr2("");
		}
		
		page = deviceInfoService.page(departIds,queryEntity, _page, _rowNum, "updateTime","asc");
		page = new Page(_page, _rowNum, page.getResult(),  page.getTotal());
		for(DeviceInfo info:(List<DeviceInfo>)page.getResult()){
			if(info.getDeviceStatus()!=null){
				if(info.getDeviceStatus()==0){
					info.setDeviceStatusName("正常");
				}else if(info.getDeviceStatus()==1){
					info.setDeviceStatusName("未启用");
				}else if(info.getDeviceStatus()==2){
					info.setDeviceStatusName("欠压");
				}else if(info.getDeviceStatus()==3){
					info.setDeviceStatusName("通讯故障");
				}
			}
		}
		dataMap.put("page", page);
		return SUCCESS;
	}
	
	/**
	 * 新闻查询
	 * @return
	 */
	public String appNews(){
		if(pageNum != null){
			_page = Integer.valueOf(pageNum) ;
		}
		rowNum  = 20;
 		page = appNewsService.page(departIds,queryEntity, _page, rowNum,"updateTime","desc");
		page = new Page(_page, rowNum, page.getResult(),  page.getTotal());
		dataMap.put("page", page);
		return SUCCESS;
	}
	
	
	/**
	 * yxnne add 2017-5-2
	 * 测试服务器信息有没有配置好
	 * @return 一段很简单的消息
	 */
	public String serverConfigTest(){
		dataMap.put("result", "ok");
		return SUCCESS;
	}
	/**
	 * yxnne add 2017-5-3
	 * 根据部门ID查询部门段时间内 洗手时机点次数
	 * @return
	 */
	public String rateMomentDepart(){
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		queryEntity.setTreeId(treeId);
		queryEntity.setStr1(staffType);
		
		List<String> rfidList = new ArrayList<String>();
		List<StaffInfo> staffList = new ArrayList<StaffInfo>();
		List<String> typeList = new ArrayList<String>();
		
		if(StringUtil.isNotBlank(queryEntity.getStr1())){
			String[] staffTypeArray = queryEntity.getStr1().split(",");//人员类别名称
			for(String staffType:staffTypeArray){
				typeList.add(staffType);
			}
		}
		
		if(StringUtil.isNotBlank(queryEntity.getTreeId())){
			staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),typeList);
		}
		int staffCount = staffList.size();
		
		for(StaffInfo info:staffList){
			rfidList.add(getRfidByStaffId(info.getId()));
		}
		
		queryEntity.setRfidList(rfidList);
		
		if(rfidList.size() == 0){
			dataMap.put("treeId", treeId);
			dataMap.put("staffCount", staffCount);
			dataMap.put("0003", 0);
			dataMap.put("0007", 0);
			dataMap.put("0008", 0);
			dataMap.put("0103", 0);
			dataMap.put("0110", 0);
			
			return SUCCESS;
		}
		
		washHandList = washHandLogService.RateCompare(queryEntity);//查询近十天的数据
		
		Integer effectWash=0,inRoom=0,outRoom=0,beforeInBed=0,longOutBed=0;
		for(WashHandLog log:washHandList){
			if(log.getEventType()!=null){
				if(log.getEventType().equals("0003")){
					++effectWash;
				}else if(log.getEventType().equals("0007")){
					++inRoom;
				}else if(log.getEventType().equals("0008")){
					++outRoom;
				}else if(log.getEventType().equals("0103")){
					++beforeInBed;
				}else if(log.getEventType().equals("0110")){
					++longOutBed;
				}
			}
		}
		
		dataMap.put("treeId", treeId);
		dataMap.put("staffCount", staffCount);
		dataMap.put("0003", effectWash);
		dataMap.put("0007", inRoom);
		dataMap.put("0008", outRoom);
		dataMap.put("0103", beforeInBed);
		dataMap.put("0110", longOutBed);
		return SUCCESS;
	}
	
	/**
	 * yxnne add 2017-5-3
	 * 在rateByStaffSelect 中被调用 ，目的是
	 * 添加员工段时间内 洗手时机点次数
	 * Times--------
	 * 0007
	 * 0003
	 * 0103
	 * 0110
	 * 0008
	 * @return
	 */
	private void mapMoment(){
		for(WashHandLog wash:washHandList){
			
			if(wash.getEventType().equals("0007")){  
				if(wash.getRfid()!=null){
					if(mapWash0007.get(wash.getRfid())==null){
						mapWash0007.put(wash.getRfid(), 1);
					}else{
						mapWash0007.put(wash.getRfid(),mapWash0007.get(wash.getRfid())+1); 
					}
				}
			}
			else if(wash.getEventType().equals("0003")){
				if(wash.getRfid()!=null){
					if(mapWash0003.get(wash.getRfid())==null){
						mapWash0003.put(wash.getRfid(), 1);
					}else{
						mapWash0003.put(wash.getRfid(),mapWash0003.get(wash.getRfid())+1); 
					}
				}
			}
			else if(wash.getEventType().equals("0103")){
				if(wash.getRfid()!=null){
					if(mapWash0103.get(wash.getRfid())==null){
						mapWash0103.put(wash.getRfid(), 1);
					}else{
						mapWash0103.put(wash.getRfid(),mapWash0103.get(wash.getRfid())+1); 
					}
				}
				
			}
			else if(wash.getEventType().equals("0110")){
				if(wash.getRfid()!=null){
					if(mapWash0110.get(wash.getRfid())==null){
						mapWash0110.put(wash.getRfid(), 1);
					}else{
						mapWash0110.put(wash.getRfid(),mapWash0110.get(wash.getRfid())+1); 
					}
					
				}
			}
			else if(wash.getEventType().equals("0008")){
				if(wash.getRfid()!=null){
					if(mapWash0008.get(wash.getRfid())==null){
						mapWash0008.put(wash.getRfid(), 1);
					}else{
						mapWash0008.put(wash.getRfid(),mapWash0008.get(wash.getRfid())+1); 
					}
				}
			}
		}
	}
	
	
	/**
	 * yxnne add 2017-5-9
	 * 在rateByStaffListSelect 中被调用 ，目的是
	 * 计算两前两前后依从率 部门内员工
	 * Times--------
	 * ---接触病人前
 			washBeforeCloseNick 
			notWashBeforeCloseNick  
	 * ---无菌操作前洗手
			washBeforeAsepticOperation  
			notWashBeforeAsepticOperation 
	 * ---接触病人后洗手
			washAfterCloseNick  
			notWashAfterCloseNick  
	 * ---接触病人环境后洗手
			washAfterCloseNickEnvri  
			notWashafterCloseNickEnvri 
	 * @return
	 */
	private void mapBeforeTwoAfterTwoDepart(){
		for(WashHandLog wash:washHandMomentList){
			if(wash.getRfidStatus() != null){
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
				}else if(wash.getRfidStatus() == 13){//接触病人后未洗手
					if(notWashTimesAfterCloseNick.get(wash.getRfid()) == null ){
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
	}
	
	
	
	/**
	 * add yxnne 2017/5/3 
	 * {
	 * "departMap":{"2":"演示间","32":"病床2","22":"病床1","12":"1"},
	 * "underDepartIds":["12","22","32"],	//部门下部门的id
	 * "department":"2"		//该部门
	 * } 
	 * @return
	 */
	public String getDepartInfo(){
		treeNode = groupTreeService.getTree(getManageDepart(),departIds, false,isTreeOpen);
		Map<String,Object> departMap = new HashMap<String, Object>();
		ArrayList<String> underDepartIds = new ArrayList<String>();
		
		for(TreeNode tn : treeNode.getChildren()){
			departMap.put(tn.getId(), tn.getLabel());
			
			if(tn.getChildren() != null){
				for(TreeNode tnc:tn.getChildren()){
					departMap.put(tnc.getId(), tnc.getLabel());
					underDepartIds.add(tnc.getId());
					//最多
					if(tnc.getChildren() != null){
						for(TreeNode tncc:tnc.getChildren()){
							departMap.put(tncc.getId(), tncc.getLabel());
							underDepartIds.add(tncc.getId());
						}
					}
				}
			}else{
				dataMap.put("child", "null");
			}
		}
		
		dataMap.put("departMap", departMap);
		dataMap.put("department", departIds);
		dataMap.put("underDepartIds", underDepartIds);
		return SUCCESS;
	}
	
	
	/**
	 * add yxnne 2017/5/3
	 * 得到部门下的所有科室的所有设备
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	public String getDepartmentDevicesSelect() throws UnsupportedEncodingException{
		if(pageNum != null){
			_page = Integer.valueOf(pageNum) ;
		}
		queryEntity.setTreeId(treeId);
		queryEntity.setStr1(number);
		if(number.equals("null")){
			queryEntity.setStr1("");
		}
		
		String name1 = new String(name.getBytes("ISO-8859-1"),"utf-8");
		queryEntity.setStr2(name1);
		if(name.equals("null")){
			queryEntity.setStr2("");
		}
		
		page = deviceInfoService.page(departIds,queryEntity, _page, _rowNum, "updateTime","asc");
		page = new Page(_page, _rowNum, page.getResult(),  page.getTotal());
		for(DeviceInfo info:(List<DeviceInfo>)page.getResult()){
			if(info.getDeviceStatus()!=null){
				if(info.getDeviceStatus()==0){
					info.setDeviceStatusName("正常");
				}else if(info.getDeviceStatus()==1){
					info.setDeviceStatusName("未启用");
				}else if(info.getDeviceStatus()==2){
					info.setDeviceStatusName("欠压");
				}else if(info.getDeviceStatus()==3){
					info.setDeviceStatusName("通讯故障");
				}
			}
		}
		dataMap.put("page", page);
		return SUCCESS;
	}
	
	/**
	 * 人员依从性查询
	 * @return
	 */
	public String rateByOneStaff(){
		if(pageNum != null){
			_page = Integer.valueOf(pageNum);
		}
		String strRfid = rfid == null?"":rfid;
		queryEntity.setTreeId(treeId);
		queryEntity.setInt1(Integer.valueOf(staffId));
		queryEntity.setStr1(jobType);
		queryEntity.setEndTime(new Date(DateUtils.str2DateByYMD(endTime).getTime()+oneDay));
		queryEntity.setStartTime(DateUtils.str2DateByYMD(startTime));
		StaffWork staffWork = null;
		GroupTree departTree =null;
		List<StaffInfo>	staffList = new ArrayList<StaffInfo>();
		List<String> rfidList = new ArrayList<String>();
		Map<String, String> typeMap = getJobTypeMap();
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
					staffList= staffInfoDao.findByDepartAndType(departIds,Long.valueOf(queryEntity.getTreeId()),typeList);
				}
				for(StaffInfo info:staffList){
					rfidList.add(getRfidByStaffId(info.getId()));
				}
			}
		}
		queryEntity.setRfidList(rfidList);
		washHandList = washHandLogService.RateCompare(queryEntity);
		washHandMomentList=washHandLogService.EventCompare(queryEntity);//洗手时机对比数据
		mapProcess();//map数据封装
		
		mapMoment();//add yxnne 2017/5/3 调用这个方法，封装下时机的map数据
	
		mapBeforeTwoAfterTwoDepart();
		
		for(String rfid:rfidList){
			if(!strRfid.equals(rfid)){
				continue;
			}
			staffWork = new StaffWork();
			DeviceInfo device = deviceInfoDao.findUniqueBy("no", rfid);
			if(device!=null){
				if(device.getStaffId()!=null){
					StaffInfo staffInfo = staffInfoDao.findUniqueBy("id", device.getStaffId());
					if (staffInfo.getName() != null) {
						staffWork.setDocName(staffInfo.getName());
					}
					if(staffInfo.getCategory()!=null){
						staffWork.setDocType(typeMap.get(staffInfo.getCategory()));
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
			//add yxnne 2017/5/3 for 洗手时机
			if(mapWash0003.get(rfid) != null){
				staffWork.setNum0003(mapWash0003.get(rfid));
			}else{
				staffWork.setNum0003(0);
			}
			
			if(mapWash0007.get(rfid) != null){
				staffWork.setNum0007(mapWash0007.get(rfid));
			}else{
				staffWork.setNum0007(0);
			}
			
			if(mapWash0110.get(rfid) != null){
				staffWork.setNum0110(mapWash0110.get(rfid));
			}else{
				staffWork.setNum0110(0);
			}
			
			if(mapWash0103.get(rfid) != null){
				staffWork.setNum0103(mapWash0103.get(rfid));
			}else{
				staffWork.setNum0103(0);
			}
			
			if(mapWash0008.get(rfid) != null){
				staffWork.setNum0008(mapWash0008.get(rfid));
			}else{
				staffWork.setNum0008(0);
			}
			
			//洗手时机依从率

			int washBeforeCloseNick =
					washTimesBeforeCloseNick.get(rfid)==null? 0:washTimesBeforeCloseNick.get(rfid);
			int notWashBeforeCloseNick = 
					notWashTimesBeforeCloseNick.get(rfid) == null? 0 : notWashTimesBeforeCloseNick.get(rfid);
			
			int washBeforeAsepticOperation =
					washTimesBeforeAsepticOperation.get(rfid) == null ? 0 : washTimesBeforeAsepticOperation.get(rfid) ;
			int notWashBeforeAsepticOperation = 
					notWashTimesBeforeAsepticOperation.get(rfid) == null ? 0: notWashTimesBeforeAsepticOperation.get(rfid);
			
			int washAfterCloseNick  = 
					washTimesAfterCloseNick.get(rfid) == null ? 0 : washTimesAfterCloseNick.get(rfid) ;
			int notWashAfterCloseNick = 
					notWashTimesAfterCloseNick.get(rfid) == null ? 0 : notWashTimesAfterCloseNick.get(rfid) ; 
			
			int washAfterCloseNickEnvri = 
					washTimesAfterCloseNickEnvri.get(rfid) == null ? 0 : washTimesAfterCloseNickEnvri.get(rfid);
			int notWashafterCloseNickEnvri = 
					notWashTimesafterCloseNickEnvri.get(rfid) == null ? 0 :  notWashTimesafterCloseNickEnvri.get(rfid);
			
			 
			staffWork.setRateBeforeCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeCloseNick, washBeforeCloseNick+notWashBeforeCloseNick)))));
			staffWork.setRateBeforeAsepticOperation(Float.valueOf(df.format(
					Float.valueOf(getPercent(washBeforeAsepticOperation, washBeforeAsepticOperation+notWashBeforeAsepticOperation)))));
			staffWork.setRateAfterCloseNick(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNick, washAfterCloseNick+notWashAfterCloseNick)))));
			staffWork.setRateAfterCloseNickEnvri(Float.valueOf(df.format(
					Float.valueOf(getPercent(washAfterCloseNickEnvri, washAfterCloseNickEnvri+notWashafterCloseNickEnvri)))));
			
			staffWork.setRate(getPercent(staffWork.getNormalNum(),staffWork.getNormalNum()+staffWork.getErrorNum()));
			staffWorkList.add(staffWork);
		}
		//add yxnne 2017/5/3 for staff rank by rate 
		if(staffWorkList.size() != 0){
			Collections.sort(staffWorkList);
			for(int i = 0 ; i < staffWorkList.size() ; i++){
				staffWorkList.get(i).setRank(i+1);
			}
			
		}
		
		page = new Page(_page, _rowNum, staffWorkList, staffWorkList.size());
		dataMap.put("page", page);	
		return SUCCESS;
	}
	
	/**
	 * 移动端的查看设备的总数量以及欠压状态的数量
	 * @param departId
	 * @return
	 * @author wwg
	 */
	public String getDeviceStatusByDepartId(){
		List<DeviceInfo> deviceInfos;
		Long departId = Long.parseLong(departIds);
		if(departId == 1){
			deviceInfos = deviceInfoService.findAll();
		} else {
			deviceInfos = deviceInfoService.findByDepartId(departId);
		}
		//通过遍历将设备类型加入set集合，这样就可以通过去重得到设备的类型数量
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < deviceInfos.size(); i++) {
			String type = deviceInfos.get(i).getType();
			set.add(type);
		}
		Map<String, List<DeviceInfo>> map = new HashMap<String, List<DeviceInfo>>();
		Map<String, List<Integer>> mapStatus = new HashMap<String, List<Integer>>();
		for (String type : set) {
			List<DeviceInfo> deviceInfos2 = new ArrayList<DeviceInfo>();
			//欠压的数量
			Integer undervoltage = 0;
			List<Integer> deviceStatusNumber = new ArrayList<Integer>();
			for (int i = 0; i < deviceInfos.size(); i++) {
				DeviceInfo deviceInfo = deviceInfos.get(i);
				if(type.equals(deviceInfo.getType())){
					deviceInfos2.add(deviceInfo);
					//2表示欠压状态
					if(deviceInfo.getDeviceStatus() == 2){
						undervoltage++;
					}
				}
			}
			Integer total = deviceInfos2.size();
			deviceStatusNumber.add(total);
			deviceStatusNumber.add(undervoltage);
			map.put(type, deviceInfos2);
			String typeName = SysStatics.deviceTypeMap.get(type);
			mapStatus.put(typeName, deviceStatusNumber);
		}
		dataMap.put("mapStatus", mapStatus);
		return SUCCESS;
	}
	
	/**
	 * 插入人工录入的手卫生记录
	 * @return
	 * @author wwg
	 */
	public String addManualRecord(){
		//未洗手时候设置洗手方式和是否正确为999
		if("0".equals(isWashHand)){
			way = "999";
			isRight = "999";
		}
		manualRecord.setDepartId(Long.parseLong(departId));
		manualRecord.setIsRight(Short.parseShort(isRight));
		manualRecord.setObserverNumber(String.valueOf(observerNumber));
		manualRecord.setOccassion(Short.parseShort(occassion));
		manualRecord.setRoleId(Long.parseLong(roleId));
		//传进来的是毫秒数，将之转化为Date类型
		manualRecord.setTime(new java.util.Date(Long.parseLong(time)));
		manualRecord.setIsWashHand(Short.parseShort(isWashHand));
		manualRecord.setWay(Short.parseShort(way));
		manualRecord.setObserverName(observerName);
		manualRecordService.add(manualRecord);
		dataMap.put("status", "success");
		return SUCCESS;
	}
	
	/**
	 * App上手工录入的信息查询返回接口
	 * @return
	 */
	public String findAllResultsOfManualRecords(){
		List<ManualRecord> manualRecords = new ArrayList<ManualRecord>();
		List<ManualRecord> recordForExecute = new ArrayList<ManualRecord>();
		Date as = new java.util.Date(Long.parseLong(timeStart));
		Date ae = new java.util.Date(Long.parseLong(timeEnd));
		
		//判断请求的是感控科还是一般科室,所有数据库的脚本中感控科Id都是132写死的;参数需要转换，传过来时候都是String类型的
		manualRecords = manualRecordService.findByDepartIdAndTimeAndWay(Long.parseLong(departId), as, ae, Short.parseShort(way));
		recordForExecute = manualRecordService.findByDepartIdAndTime(Long.parseLong(departId), as, ae);
		if("132".equals(departId)){
			manualRecords = manualRecordService.findByTimeAndWay(as, ae, Short.parseShort(way));
			recordForExecute = manualRecordService.findByTime(as, ae);
		}
		//计算依从率（第一部分图）
		String departRate = CalculationOfRate.countRate(manualRecords);
		//计算正确率（第一部分图）
		String departCorrect = CalculationOfRate.countCorrect(manualRecords);
		//计算执行率
		String departExecute = CalculationOfRate.rateOfExecute(recordForExecute);
		
		//第二部分图
		//按照洗手时机将数据分组
		Map<Short, List<ManualRecord>> occassionMap = CalculationOfRate.groupByOccassion(manualRecords);
		Map<Short, List<ManualRecord>> occassionMapForExecute = CalculationOfRate.groupByOccassion(recordForExecute);
		//新建Map，用于存放不同洗手时机的执行率和正确率
		Map<Short, String> executeMapOccassion = new HashMap<Short, String>();
		Map<Short, String> correctMapOccassion = new HashMap<Short, String>();
		//建一个List用于存放遍历的值
		List<ManualRecord> occassionManualRecords = new ArrayList<ManualRecord>();
		List<ManualRecord> occassionManualRecordsForExecute = new ArrayList<ManualRecord>();
		String occassionExecute;
		String occassionCorrect;
		//occationMap遍历map，得到所有的洗手时机和对应的记录，正确率
		for(Short key : occassionMap.keySet()){
			occassionManualRecords = occassionMap.get(key);
			occassionCorrect = CalculationOfRate.countCorrect(occassionManualRecords);
			correctMapOccassion.put(key, occassionCorrect);
		}
		//执行率
		for(Short key : occassionMapForExecute.keySet()){
			occassionManualRecordsForExecute = occassionMapForExecute.get(key);
			occassionExecute = CalculationOfRate.rateOfExecute(occassionManualRecordsForExecute);
			executeMapOccassion.put(key, occassionExecute);
		}
		
		//第三部分图
		//将数据按人员类别分
		Map<Long, List<ManualRecord>> manualRecordsGroupByStaffType = new HashMap<Long, List<ManualRecord>>();
		Map<Long, List<ManualRecord>> manualRecordsGroupByStaffTypeForExecute = new HashMap<Long, List<ManualRecord>>();
		manualRecordsGroupByStaffType = CalculationOfRate.group(manualRecords);
		manualRecordsGroupByStaffTypeForExecute = CalculationOfRate.group(recordForExecute);
		String executeStaffRate;
		String correctStaffRate;
		List<ManualRecord> manualRecordByStaff = new ArrayList<ManualRecord>();
		List<ManualRecord> manualForExecute = new ArrayList<ManualRecord>();
		Map<Long, String> staffExecuteRate = new HashMap<Long, String>();
		Map<Long, String> staffCorrectRate = new HashMap<Long, String>();
		//正确率
		for(Long key : manualRecordsGroupByStaffType.keySet()){
			manualRecordByStaff = manualRecordsGroupByStaffType.get(key);
			correctStaffRate = CalculationOfRate.countCorrect(manualRecordByStaff);
			staffCorrectRate.put(key, correctStaffRate);
		}
		//执行率
		for(Long key : manualRecordsGroupByStaffTypeForExecute.keySet()){
			manualForExecute = manualRecordsGroupByStaffTypeForExecute.get(key);
			executeStaffRate = CalculationOfRate.rateOfExecute(manualForExecute);
			staffExecuteRate.put(key, executeStaffRate);
		}
		//将所有信息封装在一个对象中
		AllManualResults allManualResults = new AllManualResults(departRate, departCorrect, departExecute, executeMapOccassion, correctMapOccassion, staffExecuteRate, staffCorrectRate);
		dataMap.put("allResults", allManualResults);
		return SUCCESS;
	}
	
	/**
	 * 简化版Web_APP
	 * @param departmentId
	 * @param startTime
	 * @param endTime
	 */
	public void getHospitalPerformanceThisWeek() {
		Date startDate = DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		if("1".equals(departmentId)) {
			//返回对象
			OrganisationStatistic os = new OrganisationStatistic();
			//规定时间内医院所有的手卫生事件
			List<WashHandLog> washHandLogList = new ArrayList<WashHandLog>();
			washHandLogList = washHandLogService.findWashHandLogsByTime(queryEntity);
			//规定时间内医院的所有手卫生事件的次数
			int totalTimes = 0;
			if(washHandLogList != null) {
				totalTimes = washHandLogList.size();
			}
			os.setTotalTimes(totalTimes);
			//医院名称
			String name = groupTreeService.getHospitalName();
			os.setName(name);
			//type为1 的是人员类别，此处查询出所有的人员类别
			List<ParameterInfo> parameterList =  parameterInfoDao.findByType(1);
			List<OrganisationStatistic.RoleTimes> roleTimes = new ArrayList<OrganisationStatistic.RoleTimes>();
			for(int i = 0, length = parameterList.size(); i < length; i++) {
				OrganisationStatistic.RoleTimes roTimes = os.new RoleTimes();
				List<StaffInfo> staffs = staffInfoDao.findStaffIdByCategory(parameterList.get(i).getKey());
				List<Long> staffIds = new ArrayList<Long>();
				if(staffs == null) {
					continue;
				}
				for(int j=0; j<staffs.size(); j++) {
					Long id = staffs.get(j).getId();
					staffIds.add(id);
				}
				if(staffIds.size() == 0) {
					continue;
				}
				List<DeviceInfo> deviceInfos = deviceInfoDao.findByStaffId(staffIds);
				List<String> rfids = new ArrayList<String>();
				for(int m=0;m<deviceInfos.size();m++) {
					rfids.add(deviceInfos.get(m).getNo());
				}
				int count = 0;
				for(int j = 0, len = rfids.size(); j < len; j++) {
					for(int k = 0, le = washHandLogList.size(); k < le; k++) {
						if(rfids.get(j).equals(washHandLogList.get(k).getRfid())) {
							count++;
						}
					}
				}
				roTimes.setRole(parameterList.get(i).getValue());
				roTimes.setTimes(count);
				roleTimes.add(roTimes);
			}
			os.setRoleTimes(roleTimes);
			//一周执行次数
			List<OrganisationStatistic.WeekTimes> weekTimes = new ArrayList<OrganisationStatistic.WeekTimes>();
			
			List<Integer> weekCounts = washHandLogService.findNumberByDate(queryEntity);
			//前七天的日期
			ArrayList<String> dateList = DateUtils.getPastNDate(7);
			for(int i = 0; i < 7; i++) {
				OrganisationStatistic.WeekTimes weTimes = os.new WeekTimes();
				weTimes.setDay(dateList.get(i));
				weTimes.setTimes(weekCounts.get(i));
				weekTimes.add(weTimes);
			}
			os.setWeekTimes(weekTimes);
			JSONObject obj = new JSONObject();
			obj.put("result", os);
			writeResponse(obj.toString());
		} else {
			queryEntity.setStr1(departmentId);
			//返回对象
			OrganisationStatistic os = new OrganisationStatistic();
			//查询该科室在该时间段内的手卫生数据
			List<WashHandLog> washHandLogs = washHandLogService.findByTimeAndDepartment(queryEntity);
			//规定时间内该科室的所有手卫生事件的次数
			int totalTimes = 0;
			if(washHandLogs != null) {
				totalTimes = washHandLogs.size();
			}
			os.setTotalTimes(totalTimes);
			//科室名称
			String name = groupTreeService.getNameById(Long.parseLong(departmentId));
			os.setName(name);
			//type为1 的是人员类别，此处查询出所有的人员类别
			List<ParameterInfo> parameterList = parameterInfoDao.findByType(1);
			List<OrganisationStatistic.RoleTimes> roleTimes = new ArrayList<OrganisationStatistic.RoleTimes>();
			for(int i = 0, length = parameterList.size(); i < length; i++) {
				OrganisationStatistic.RoleTimes roTimes = os.new RoleTimes();
				List<StaffInfo> staffs = staffInfoDao.findStaffIdByCategory(parameterList.get(i).getKey());
				List<Long> staffIds = new ArrayList<Long>();
				if(staffs.size() == 0) {
					continue;
				}
				for(int j=0; j<staffs.size(); j++) {
					Long id = staffs.get(j).getId();
					staffIds.add(id);
				}
				if(staffIds.size() == 0) {
					continue;
				}
				List<DeviceInfo> deviceInfos = deviceInfoDao.findByStaffId(staffIds);
				List<String> rfids = new ArrayList<String>();
				for(int m=0;m<deviceInfos.size();m++) {
					rfids.add(deviceInfos.get(m).getNo());
				}
				int count = 0;
				for(int j = 0, len = rfids.size(); j < len; j++) {
					for(int k = 0, le = washHandLogs.size(); k < le; k++) {
						if(rfids.get(j).equals(washHandLogs.get(k).getRfid())) {
							count++;
						}
					}
				}
				roTimes.setRole(parameterList.get(i).getValue());
				roTimes.setTimes(count);
				roleTimes.add(roTimes);
			}
			os.setRoleTimes(roleTimes);
			//一周执行次数
			List<OrganisationStatistic.WeekTimes> weekTimes = new ArrayList<OrganisationStatistic.WeekTimes>();
			List<Integer> weekCounts = washHandLogService.findNumberByDateAndDepartmentId(queryEntity);
			//前七天的日期
			ArrayList<String> dateList = DateUtils.getPastNDate(7);
			for(int i = 0; i < 7; i++) {
				OrganisationStatistic.WeekTimes weTimes = os.new WeekTimes();
				weTimes.setDay(dateList.get(i));
				weTimes.setTimes(weekCounts.get(i));
				weekTimes.add(weTimes);
			}
			os.setWeekTimes(weekTimes);
			JSONObject obj = new JSONObject();
			obj.put("result", os);
			writeResponse(obj.toString());
		}
		
	}
	
	public void getDepartmentCounts() {
		List<DepartmentStatistic> departmentStatistics = new ArrayList<DepartmentStatistic>();
		List<GroupTree> groupTrees = groupTreeService.getDepart();
		for(int i = 0, length = groupTrees.size(); i < length; i++) {
			DepartmentStatistic departmentStatistic = new DepartmentStatistic();
			departmentStatistic.setId(groupTrees.get(i).getId());
			departmentStatistic.setName(groupTrees.get(i).getName());
			departmentStatistics.add(departmentStatistic);
		}
		Date startDate =  DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		int times = 0;
		for(int i = 0, length = departmentStatistics.size(); i < length; i++) {
			queryEntity.setStr1(String.valueOf(departmentStatistics.get(i).getId()));
			//查询该科室在该时间段内的手卫生数据
			List<WashHandLog> washHandLogs = washHandLogService.findByTimeAndDepartment(queryEntity);
			if(washHandLogs != null) {
				times = washHandLogs.size();
			}
			departmentStatistics.get(i).setTimes(times);
		}
		Collections.sort(departmentStatistics, new Comparator<DepartmentStatistic>() {
			@Override
			public int compare(DepartmentStatistic d1, DepartmentStatistic d2) {
				return (d2.getTimes() - d1.getTimes());
			}
        });
		for(int i = 0, length = departmentStatistics.size(); i < length; i++) {
			departmentStatistics.get(i).setRank(i+1);
		}
		JSONObject obj = new JSONObject();
		obj.put("result", departmentStatistics);
		writeResponse(obj.toString());
	}
	
	public void getStaffCounts() {
		List<StaffStatistic> staffStatistics = new ArrayList<StaffStatistic>();
		Date startDate = DateUtils.str2DateByYMDHMS(startTime);
		Date endDate = DateUtils.str2DateByYMDHMS(endTime);
		queryEntity.setStartTime(startDate);
		queryEntity.setEndTime(endDate);
		List<DeviceInfo> deviceList = deviceInfoService.findAll();
		Map<String, String> devicePosition = new HashMap<String, String>();
		for(int i = 0, length = deviceList.size(); i < length; i++) {
			devicePosition.put(deviceList.get(i).getNo(), deviceList.get(i).getName());
		}
		//得到所有的人员类别
		List<ParameterInfo> parameterList = parameterInfoDao.findByType(1);
		Map<String, String> parameterMap = new HashMap<String, String>();
		for(int i = 0, length = parameterList.size(); i < length; i++) {
			parameterMap.put(parameterList.get(i).getKey(), parameterList.get(i).getValue());
		}
		List<GroupTree> groupTrees = groupTreeService.getDepart();
		for(int i = 0, length = departmentIds.size(); i < length; i++) {
			String departmenName = "";
			for(GroupTree gt : groupTrees) {
				if(gt.getId().equals(Long.parseLong(departmentIds.get(i)))) {
					departmenName = gt.getName();
				}
			}
			queryEntity.setStr1(departmentIds.get(i));
			//得到该科室下的所有设备信息
			List<DeviceInfo> devices = deviceInfoService.findByDepartId(Long.valueOf(departmentIds.get(i)));
			//得到该科室的所有员工信息
			List<StaffInfo> staffInfos = staffInfoService.findByGroupId(Long.valueOf(departmentIds.get(i)));
			//得到该科室下的所有手卫生记录
			List<WashHandLog> washHandLogs = washHandLogService.findByTimeAndDepartment(queryEntity);
			Map<String, Map<String, Integer>> mapOuter = new HashMap<String, Map<String,Integer>>();
			Map<String, Integer> mapInner = new HashMap<String, Integer>();
			if(washHandLogs == null) {
				continue;
			}
			for(WashHandLog log : washHandLogs) {
				//washHandLog中的rfid
				String rfid = log.getRfid();
				//washHandLog中的deviceNo
				String deviceNo = log.getDeviceNo();
				//判断map中该rfid对应的detail是否为空
				if(mapOuter.get(rfid) != null) {
					Map<String, Integer> map = mapOuter.get(rfid);
					if(map.get(deviceNo) != null) {
						int times = map.get(deviceNo) + 1;
						map.put(deviceNo, times);
					} else {
						map.put(deviceNo, 1);
					}
				} else {
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put(deviceNo, 1);
					mapOuter.put(rfid, map);
				}
			}
			for(int j = 0, len = devices.size(); j < len; j++) {
				for(String rfid : mapOuter.keySet()) {
					if(devices.get(j).getNo().equals(rfid)) {
						Long staffId = devices.get(j).getStaffId();
						for(int k = 0, l = staffInfos.size(); k < l; k++) {
							if(staffId.equals(staffInfos.get(k).getId())) {
								String name = staffInfos.get(k).getName();
								String role = parameterMap.get(staffInfos.get(k).getCategory());
								StaffStatistic staffStatistic = new StaffStatistic();
								staffStatistic.setDepartment(departmenName);
								staffStatistic.setName(name);
								staffStatistic.setRole(role);
								mapInner = mapOuter.get(rfid);
								int count = 0;
								for(Integer times : mapInner.values()) {
									count = count + times;
								}
								staffStatistic.setTimes(count);
								List<StaffStatistic.Detail> details = new ArrayList<StaffStatistic.Detail>();
								for(String dNo : mapInner.keySet()) {
									StaffStatistic.Detail detail = staffStatistic.new Detail();
									if(devicePosition.get(dNo) == null) {
										detail.setPosition("未添加设备");
									} else {
										detail.setPosition(devicePosition.get(dNo));
									}
									detail.setTimes(mapInner.get(dNo));
									details.add(detail);
								}
								staffStatistic.setDetail(details);
								staffStatistics.add(staffStatistic);
							}
						}
					}
				}
			}
		}
		Collections.sort(staffStatistics, new Comparator<StaffStatistic>() {

			@Override
			public int compare(StaffStatistic o1, StaffStatistic o2) {
				return o2.getTimes() - o1.getTimes();
			}
			
		});
		for(int i = 0, length = staffStatistics.size(); i < length; i++) {
			staffStatistics.get(i).setRank(i+1);
		}
		JSONObject obj = new JSONObject();
		obj.put("result", staffStatistics);
		writeResponse(obj.toString());
	}
	
	public void getDepartmentsForWebApp() {
		List<GroupTree> groupTrees = groupTreeService.getDepart();
		JSONObject obj = new JSONObject();
		obj.put("result", groupTrees);
		writeResponse(obj.toString());
	}
	
	public Map<String, Object> getDataMap() {  
        return dataMap;  
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getNewPassWord() {
		return newPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}  
	
	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getRfidStatus() {
		return rfidStatus;
	}

	public void setRfidStatus(String rfidStatus) {
		this.rfidStatus = rfidStatus;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getGroupTreeId() {
		return groupTreeId;
	}

	public void setGroupTreeId(String groupTreeId) {
		this.groupTreeId = groupTreeId;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepartIds() {
		return departIds;
	}

	public void setDepartIds(String departIds) {
		this.departIds = departIds;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getObserverName() {
		return observerName;
	}

	public void setObserverName(String observerName) {
		this.observerName = observerName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOccassion() {
		return occassion;
	}

	public void setOccassion(String occassion) {
		this.occassion = occassion;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getIsRight() {
		return isRight;
	}

	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getObserverNumber() {
		return observerNumber;
	}

	public void setObserverNumber(String observerNumber) {
		this.observerNumber = observerNumber;
	}

	public String getIsWashHand() {
		return isWashHand;
	}

	public void setIsWashHand(String isWashHand) {
		this.isWashHand = isWashHand;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

}

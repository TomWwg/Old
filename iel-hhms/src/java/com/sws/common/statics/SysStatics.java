package com.sws.common.statics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


public class SysStatics {
	// 用户状态
	public final static int USER_STATUS_NORMAL = 0; 
	public final static int USER_STATUS_STOP = 1; 	
	public final static int USER_STATUS_EXPRIE = 2;
	public final static String USER_STATUS_NORMAL_DES = "正常";
	public final static String USER_STATUS_LOCK_DES = "停用";
	public final static String USER_STATUS_EXPRIE_DES = "过期";
	// 操作日志类型
	public final static int OPERATION_LOG_USER_LOGIN = 1; 
	public final static int OPERATION_LOG_USER_LOGOUT = 2; 
	public final static int OPERATION_LOG_DEVICE_ADD = 3; 
	public final static String OPERATION_LOG_USER_LOGIN_DES = "用户登陆"; 
	public final static String OPERATION_LOG_USER_LOGOUT_DES = "用户登出"; 
	public final static String OPERATION_LOG_DEVICE_ADD_DES = "设备添加"; 
	
	public static String USER_INFO = "userInfo"; // 用户参数
	
	//license文件路径
	public static final String RSA_PUB_KEY_FILE = "/license/RSAPublicKey.xml";
	public static final String LICENSE_FILE = "/license/sws-license.cyt";
	public static final int LICNESE_FILE_SIZE = 10240;
	
	// 排序类型
	public final static String SORT_TYPE_ASC = "asc";
	public final static String SORT_TYPE_DESC = "desc";
	public final static Map<String, String> logEventTypeMap = new TreeMap<String, String>();
	public final static Map<String, String> alarmShowMap = new LinkedHashMap<String, String>();
	public final static Map<String, String> deviceTypeMap = new TreeMap<String, String>();
	public final static Map<String, String> rfidStatusMap = new TreeMap<String, String>();
	public final static Map<String, String> eventTypeMap = new TreeMap<String, String>();
	public final static Map<String, String> jobTypeMap = new TreeMap<String, String>();
	public final static Map<String, String> jobTitleMap = new TreeMap<String, String>();
	public final static Map<String, String> parameterMap = new TreeMap<String, String>();
	public final static Map<String, String> serverMap = new TreeMap<String, String>();
	public final static Map<String, String> washHandStatusMap = new TreeMap<String, String>();//手卫生状态
	public final static Map<String, String> logDeviceTypeMap = new TreeMap<String, String>();
	public final static Map<String, String> departLevelMap = new TreeMap<String, String>();
	
	
	static {
		
		departLevelMap.put("0", "全院科室");
		departLevelMap.put("1", "当前科室");
		
		logEventTypeMap.put("0003", "手卫生完成");
		logEventTypeMap.put("0007", "进入病区");
		logEventTypeMap.put("0008", "离开病区");
		logEventTypeMap.put("0018", "接近患者");
		logEventTypeMap.put("0102", "离开病区未手卫生");
		logEventTypeMap.put("0103", "接触患者前未手卫生");
		logEventTypeMap.put("0110", "离开患者后未手卫生");
		logEventTypeMap.put("0108", "欠压报警");
		logEventTypeMap.put("0109", "欠压恢复");
		logEventTypeMap.put("0011", "校时命令");
		logEventTypeMap.put("9006", "心跳包命令");
		logEventTypeMap.put("0045", "接近仪器");
		//事件类型
		alarmShowMap.put("38", "无线接入点");
		alarmShowMap.put("40", "智能胸牌");
		alarmShowMap.put("09", "门外发射器");
		alarmShowMap.put("0a", "门内发射器");
		alarmShowMap.put("10", "床信号识别器");
		alarmShowMap.put("29", "液瓶感应器");
		alarmShowMap.put("18", "污染区识别器");
		alarmShowMap.put("20", "无菌区识别器");
		alarmShowMap.put("2b", "外门液瓶识别器");
		alarmShowMap.put("48", "仪器识别器");
		alarmShowMap.put("2a", "水洗区液瓶识别器");
		alarmShowMap.put("30", "水洗区识别器");
		deviceTypeMap.put("38", "无线接入点"); //有用
		deviceTypeMap.put("40", "智能胸牌");//有用
		deviceTypeMap.put("09", "门外发射器");//有用
		deviceTypeMap.put("0a", "门内发射器");//有用
		deviceTypeMap.put("10", "床信号识别器");//有用
		deviceTypeMap.put("29", "液瓶识别器"); //有用
		deviceTypeMap.put("18", "污染区识别器");//.......
		deviceTypeMap.put("20", "无菌区识别器");
		deviceTypeMap.put("2b", "外门液瓶识别器");
		deviceTypeMap.put("48", "仪器识别器");//8.11添加
		deviceTypeMap.put("2a", "水洗区液瓶识别器");
		deviceTypeMap.put("30", "水洗区识别器");   //设备类型
		logDeviceTypeMap.put("38", "无线接入点"); //该集合设备类型是与胸牌交互的那些设备,该集合中没有胸牌本身和AP
		logDeviceTypeMap.put("40", "智能胸牌");
		logDeviceTypeMap.put("09", "门外发射器");//有用
		logDeviceTypeMap.put("0a", "门内发射器");//有用
		logDeviceTypeMap.put("10", "床信号识别器");//有用
		logDeviceTypeMap.put("29", "液瓶识别器"); //有用
		logDeviceTypeMap.put("18", "污染区识别器");
		logDeviceTypeMap.put("20", "无菌区识别器");
		logDeviceTypeMap.put("2a", "水洗区液瓶识别器");
		logDeviceTypeMap.put("2b", "外门液瓶识别器");
		logDeviceTypeMap.put("48", "仪器识别器");//8.11添加
		logDeviceTypeMap.put("30", "水洗区识别器");   //设备类型
		washHandStatusMap.put("1", "清洁");
		washHandStatusMap.put("2", "有限清洁");
		washHandStatusMap.put("3", "待手卫生");
		washHandStatusMap.put("4", "未执行");//..............手卫生状态 
		//进行手卫生之前的状态
		rfidStatusMap.put("1", "清洁");
		rfidStatusMap.put("2", "有限清洁");
		rfidStatusMap.put("3", "待手卫生");
		rfidStatusMap.put("4", "未执行");//..............手卫生状态
		rfidStatusMap.put("5", "短时进入病区");
		rfidStatusMap.put("6", "短时离开病区");
		rfidStatusMap.put("7", "进入病区未手卫生");//无效洗手
		rfidStatusMap.put("8", "离开病区未手卫生");//无效洗手
		rfidStatusMap.put("9", "短时接触患者提醒");
		rfidStatusMap.put("10", "长时接触患者提醒");
		rfidStatusMap.put("11", "接触患者未手卫生");//无效洗手
		rfidStatusMap.put("12", "短时离开患者提醒");
		rfidStatusMap.put("13", "离开患者未手卫生"); //无效洗手..............洗手时机
		//eventTypeMap.put("0001", "进入识别区");
		eventTypeMap.put("0003", "手卫生完成");
		eventTypeMap.put("0004", "外门手卫生完成");
		eventTypeMap.put("0007", "进入病区");
		eventTypeMap.put("0008", "离开病区");
		eventTypeMap.put("0018", "接近患者");
		eventTypeMap.put("0102", "离开病区未手卫生");
		eventTypeMap.put("0103", "接触患者前未手卫生");
		eventTypeMap.put("0045", "接近仪器");
		//eventTypeMap.put("0109", "违规接近清洁物");
		eventTypeMap.put("0110", "离开患者后未手卫生");//事件类型
		/************老事件类型--马上去掉了**********
		eventTypeMap.put("0019", "超时接近病床");
		eventTypeMap.put("0020", "短时接近病床未手卫生");
		eventTypeMap.put("0021", "长时接近病床未手卫生");
		eventTypeMap.put("0022", "短时离开病床未手卫生");
		************老事件类型--马上去掉了**********/
//		jobTypeMap.put("1", "医生");
//		jobTypeMap.put("2", "护师");
//		jobTypeMap.put("3", "护士");
//		jobTypeMap.put("4", "护工");
//		jobTypeMap.put("5", "保洁"); //类别
//		jobTypeMap.put("6", "培训生");
//		jobTypeMap.put("7", "轮转生");
//		jobTypeMap.put("8", "实习生");
//		jobTypeMap.put("9", "规划生");
//		jobTitleMap.put("1", "高级");
//		jobTitleMap.put("2", "中级");
//		jobTitleMap.put("3", "初级");
//		jobTitleMap.put("4", "其他"); //职称
		parameterMap.put("1", "人员类别");
		parameterMap.put("2", "服务器设置");//参数设置
		parameterMap.put("3", "液瓶参数设置");
		serverMap.put("ip", "127.0.0.1");
		serverMap.put("port", "8067");
	}

}
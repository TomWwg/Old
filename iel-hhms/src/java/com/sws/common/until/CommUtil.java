package com.sws.common.until;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class CommUtil {
	
	private static final Log log = LogFactory.getLog(CommUtil.class);
	
	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object... objs) {
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}
			if (obj instanceof CharSequence) {
				return ((CharSequence) obj).length() == 0;
			}
			if (obj instanceof Collection) {
				return ((Collection) obj).isEmpty();
			}
			if (obj instanceof Map) {
				return ((Map) obj).isEmpty();
			}
			if (obj.getClass().isArray()) {
				return Array.getLength(obj) == 0;
			}
		}
		return false;
	}
	
	public static boolean isNotNullOrEmpty(Object... obj) {
		return !isNullOrEmpty(obj);
	}
	/**
	 * 过滤字符串中不是数字的字符
	 * @param str
	 * @return
	 */
	public static String checkStringNum(String str){
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();     
	}
	
	/**
	 * 过滤字符串中所有特殊字符
	 * @param str
	 * @return
	 */
	public static String checkFileName(String str){
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();     
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void printMap(Map map){
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			System.out.println("#"+key+"#");
			System.out.println("*"+val+"*");		
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void sortMap(Map map){
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
				//排序前
				for (int i = 0; i < infoIds.size(); i++) {
				String id = infoIds.get(i).toString();
				System.out.println(id);
				}
				//排序
				Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
						return (Integer.valueOf(o1.getKey()) - Integer.valueOf(o2.getKey()));
					}
				});
				System.out.println("排序后");
				//排序后
				for (int i = 0; i < infoIds.size(); i++) {
				String id = infoIds.get(i).toString();
				System.out.println(id);
				} 
	}
	
	/**
	 * 获取当前时间字符串，格式："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return 时间字符串
	 */
	public static String getCurrentDatetimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis()); // 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 把8859-1编码转换成UTF-8编码
	 * 
	 * @param str
	 * @return
	 */
	public static String convertStringEncoding(String str) {
		try {
			return new String(str.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			return "Error";
		}
	}
	
	/**
	 * 把UTF-8编码转换成8859-1编码
	 * 
	 * @param str
	 * @return
	 */
	public static String convertStringDecoding(String str) {
		try {
			return new String(str.getBytes("UTF-8"), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage() ,e);
			return "Error";
		}
	}
	
	public static String dateToStr(Object date){
		if (date == null) {
			return "";
		}
		Date formatDate = new Date();
		if (date instanceof Date) {
			formatDate.setTime(((Date) date).getTime());
		}else if (date instanceof Calendar) {
			formatDate = ((Calendar)date).getTime();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(formatDate);
	}
	
	public static String dateTimeToStr(Object date){
		if (date == null) {
			return "";
		}
		Date formatDate = new Date();
		if (date instanceof Date) {
			formatDate.setTime(((Date) date).getTime());
		}else if (date instanceof Calendar) {
			formatDate = ((Calendar)date).getTime();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(formatDate);
	}
	
	public static Date strToDate(String date){
		return strToDate(date, false);
	}
	
	/**
	 * 将字符串格式时间转换为Date
	 * @param date
	 * @param isEnd 如果字符串格式为 yyyy-MM-dd HH:mm:ss，此参数无用。如果格式为yyyy-MM-dd，isEnd:true=补上“23:59:59”， isEnd:false=补上“00:00:00”
	 * @return
	 */
	public static Date strToDate(String date, boolean isEnd){
		if (date == null) {
			return new Date(System.currentTimeMillis());
		}
		//判断date 格式是否符合 yyyy-MM-dd HH:mm:ss
		if (date.indexOf(" ") == -1) {
			if(isEnd){
				date += " 23:59:59";
			}else{
				date += " 00:00:00";
			}
		}else{//判断 若date 为 yyyy-MM-dd 00:00:00
			if(isEnd){
				int nIndex = date.indexOf(" ");
				date = date.substring(0, nIndex);
				date += " 23:59:59";
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * 判断角色是否过期
	 * @param role
	 * @return
	 */
//	public static boolean isTimeOutRole(RoleInfo role){
//		if (role == null) {
//			return true;
//		}
//		if(role != null && role.getExpireTime() != null){
//			Date dt= DateTimeUtil.getDateTime(System.currentTimeMillis());
//			long l=role.getExpireTime().getTime();
//			
//			if(dt.getTime() < l){
//				return false;
//			}
//		}else{
//			return false;
//		}
//		
//		return true;
//	}	
	
	public static String jdomDocumentToString(org.jdom2.Document doc) {
		Format fmt = Format.getPrettyFormat();
		fmt.setEncoding("UTF-8");
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(fmt);
		return outputter.outputString(doc);
	}
	
	public static Document stringToJdomDocument(String xmlString){
		try{
			StringReader reader = new StringReader(xmlString);
			InputSource is = new InputSource(reader);
			Document xmlDoc = (new SAXBuilder()).build(is);
			return xmlDoc;
		}catch (JDOMException e) {
			log.error("stringToJdomDocument error! xmlString is:"+xmlString, e);
			return null;
		}catch (IOException e) {
			log.error("stringToJdomDocument error! xmlString is:"+xmlString, e);
			return null;
		}catch (Exception e) {
			log.error("stringToJdomDocument error! xmlString is:"+xmlString, e);
			return null;
		}
	}

	/**
	 * 列表转化为字符串
	 * @param list
	 * @param split
	 * @return
	 */
	public static String convertListToString(Collection<Integer> list, String split)
	{
		if(list == null || list.size() == 0)
			return null;
		int nCount = 0;
		StringBuffer ids = new StringBuffer();
		for (Integer id : list)
		{
			ids.append(id);
			nCount++;
			if (nCount != list.size())
			{
				ids.append(split);
			}
		}
		return ids.toString();
	}
	
	public static <T> String collectionToString(Collection<T> list, String split) {
		if(list == null || list.size() == 0)
			return null;
		int nCount = 0;
		StringBuffer obj = new StringBuffer();
		for (T t : list)
		{
			obj.append(t);
			nCount++;
			if (nCount != list.size())
			{
				obj.append(split);
			}
		}
		return obj.toString();
	}
	
	/**
	 * 将整数集组成的字符串还原为整数列表
	 * @author shanguoming 2011-11-22 下午05:18:15
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<Integer> splitIntStr(String str){
		return convertStringToList(str, ",");
	}
	
	/**
	 * 将整数集组成的字符串还原为整数列表
	 * @author wangyabei
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<Integer> convertStringToList(String str, String split){
		List<Integer> list = new ArrayList<Integer>();
		if(!StringUtil.isNullOrEmpty(str)){
			String[] array = str.split(split);
			for(String intStr: array){
				intStr = intStr.trim();
				if(!StringUtil.isNullOrEmpty(intStr)){
					list.add(Integer.parseInt(intStr));
				}
			}
		}
		return list;
	}
	
	/**
	 * 数组转化为字符串
	 * @param list
	 * @param split
	 * @return
	 */
	public static String convertArrayToString(int[] list, String split)
	{
		if(list == null || list.length == 0)
			return null;
		int nCount = 0;
		StringBuffer ids = new StringBuffer();
		for (Integer id : list)
		{
			ids.append(id);
			nCount++;
			if (nCount != list.length)
			{
				ids.append(split);
			}
		}
		return ids.toString();
	}


	
	/**
	 * 获取对象的属性值
	 * @author wangyabei 2011-8-22 上午09:09:08
	 * @param object
	 * @param name
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Object getObjectProperty(Object object, String name){
    	if(name==null || name.isEmpty()) return null;
    	Class clazz = object.getClass();
    	String methodName = "get" + name.substring(0, 1).toUpperCase()+name.substring(1);
    
    	try  {
        	Method method = clazz.getMethod(methodName);
        	if(method!=null){
        		return method.invoke(object);
        	}
        } catch (Exception ex){
        }
  
        try{
        	Field field = clazz.getField(name);
        	if(field!=null){
        		return field.get(object);
        	}
        }catch(Exception ex){
        }
        
        return null;
    }
	
	/**
	 * 将对象的属性及值放入map
	 * @author wangyabei 2011-8-22 上午09:09:26
	 * @param object
	 * @param names 以逗号(,)分隔的属性名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getObjectMap(Object object, String names){
		return getObjectMap(object, names.split(","));
	}
	
	/**
	 * 将指定对象的属性及值放入map
	 * @author wangyabei 2011-8-22 上午09:11:17
	 * @param object
	 * @param names 属性名数组
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getObjectMap(Object object, String[] names){
		return getObjectMap(object, Arrays.asList(names));
	}
	
	/**
	 * 将指定对象的属性及值放入map
	 * @author wangyabei 2011-8-22 上午09:11:17
	 * @param object
	 * @param names 属性名列表
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Map getObjectMap(Object object, List<String> names){
		Map map = new HashMap();
		for(String name: names){
			Object value = getObjectProperty(object, name);
			if(value!=null)
				map.put(name, value);
		}
		return map;
	}
	
	/**
	 * 将列表转换为JSONArray
	 * @author wangyabei 2011-9-21 下午07:27:37
	 * @param list
	 * @param names
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JSONArray convertList2JSONArray(Collection list, String names){
		JSONArray array = new JSONArray();
		for(Object obj:list){
			Map map = getObjectMap(obj, names);
			JSONObject json = new JSONObject();
			for(Object object : map.entrySet()) {
				Entry entry = (Entry)object;
				json.put((String) entry.getKey(), entry.getValue());
			}
			array.add(json);
		}
		return array;
	}

    public static Calendar strToCalendar(String date) {
        return strToCalendar(date, false);
    }


    /**
     * 将字符串格式时间转换为Calendar
     * @param date
     * @param isEnd 如果字符串格式为 yyyy-MM-dd HH:mm:ss，此参数无用。如果格式为yyyy-MM-dd，isEnd:true=补上“23:59:59”， isEnd:false=补上“00:00:00”
     * @return
     */
    public static Calendar strToCalendar(String date, boolean isEnd) {
        Calendar happenTime = null;
        if (!StringUtil.isNullOrEmpty(date)) {
            date = date.replaceAll("T", " ");
            happenTime = Calendar.getInstance();
            happenTime.setTime(strToDate(date, isEnd));
        }
        return happenTime;
    }
    /**
     * 获取本机IP
     */
    public static String getLocalHostIP(){
        //List<String> res=new ArrayList<String>();
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> nii=ni.getInetAddresses();
                while(nii.hasMoreElements()){
                    ip = (InetAddress) nii.nextElement();
                    if (ip.getHostAddress().indexOf(":") == -1) {
                        if(!"127.0.0.1".equals(ip.getHostAddress())){
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 比较IP地址
     * @author xuanyunfei 2011-6-1 下午05:01:57
     * @param String startIp, String endIp
     * @return endIp > startIp true
     */
    public static boolean compareIpAddr(String startIp, String endIp) {
        boolean flag = false;
        if(!StringUtil.isNullOrEmpty(startIp)&&!StringUtil.isNullOrEmpty(endIp)){
            String startips[] = startIp.split("\\.");
            String endIps[] = endIp.split("\\.");
            for (int i = 0; i < startips.length; i++) {
                if (Integer.parseInt(endIps[i]) > Integer.parseInt(startips[i])) {
                    flag = true;
                    break;
                } else {
                    if (Integer.parseInt(endIps[i]) == Integer
                            .parseInt(startips[i])) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }

        return flag;
    }
	
	/**
	 * oracle最大表达式数为1000的处理
	 * @author zhangyang 2012-2-15 下午12:10:33
	 * @param list
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String oracleInStatement(List list, String name){
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(" in (");
		
		for(int i=0; i< list.size(); i++){
			if(i != (list.size() -1)){
				if(i!=0 && ((i+1)%1000 == 0)){ //buffer中有1000个值
					sb.append(list.get(i)).append(") or ").append(name).append(" in(");
				}else {
					sb.append(list.get(i)).append(",");
				}
			}else {
				sb.append(list.get(i));
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 连接列表为字符串
	 * @author wangyabei 2011-10-8 下午04:40:47
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String joinList(Collection list){
		StringBuilder sb = new StringBuilder();
		for(Object obj: list){
			sb.append(obj.toString()).append(",");
		}
		if(sb.length()>0) sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/**
	 * 解析整形，如果失败返回null
	 * 
	 * @author wangyabei 2012-5-16 上午10:02:15
	 * @param obj
	 * @return
	 */
	public static Integer parseInt(Object obj){
		try{
			return Integer.parseInt(obj.toString());
		}catch(Exception ex){
//			log.error(null, ex);
			return null;
		}
	}
	
	/**
	 * 获取excel中单元格的内容，转换成String格式
	 * 因为可能存在纯数字内容被excel自动当成number来处理
	 * @author wangyuxing 2012-6-11 下午08:41:35
	 * @param row
	 * @param cellNum
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFromCell(Row row, int cellNum, String defaultValue) {
		String result = defaultValue;
		Cell cell = row.getCell(cellNum);
		if(null != cell){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				DecimalFormat df = new DecimalFormat("0");
				result = df.format(cell.getNumericCellValue());
				break;
			}
		}
		if(null != result){
			result = result.trim();
		}
		return  result;
	}
	
	/**
	 * 获取excel中单元格的内容，转换成Integer格式
	 * 因为可能存在纯数字内容被excel自动当成number来处理
	 * @author wangyuxing 2012-6-11 下午09:04:44
	 * @param row
	 * @param cellNum
	 * @param defaultValue
	 * @return
	 */
	public static Integer getIntegerFromCell(Row row, int cellNum, Integer defaultValue) {
		Integer result = defaultValue;
		Cell cell = row.getCell(cellNum);
		if (null != cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				result = Integer.valueOf(cell.getRichStringCellValue().getString());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				result = Integer.valueOf((int)cell.getNumericCellValue());
				break;
		}
		}
		return  result;
	}
	
	public static boolean checkIntegerIsNull(Integer a) {
		if (a == null || a == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 加密
	 * @param value
	 * @return
	 *//*
	public static String encrypt(String value){
		try{
			if(StringUtil.isNullOrEmpty(value)){
				return "";
			}
			else if(PropertyUtil.canExecSecurity(value)){
				return HexConvertUtil.getInstance()._$$1(value);
			}
		}catch(Exception ex){
			
		}
		return value;
		
	}*/
	
	/**
	 * 解密
	 * @param value
	 * @return
	 *//*
	public static String decrypt(String value){
		try{
			if(StringUtil.isNullOrEmpty(value)){
				return "";
			}
			else if(!PropertyUtil.canExecSecurity(value)){
				return HexConvertUtil.getInstance()._$$2(value);
			}
		}catch(Exception ex){
			
		}
		return value;
	}*/


}

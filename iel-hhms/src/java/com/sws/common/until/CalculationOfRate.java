package com.sws.common.until;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sws.model.ManualRecord;

/**
 * 人工录入模块的一些用到的方法
 * @author wwg
 *
 */
public class CalculationOfRate {
	
	/**
	 * 分组，根据人员类别将数据分到不同的组中
	 * @param manualRecordList
	 * @return
	 */
	public static Map<Long, List<ManualRecord>> group(List<ManualRecord> manualRecordList) {
		Long roleId = 0L;
		Long key = 0L;
		ManualRecord maRecord = new ManualRecord();
		Map<Long, List<ManualRecord>> manualMap = new HashMap<Long, List<ManualRecord>>();
		//得到数据中的所有的人员类型
		Set<Long> keys = new HashSet<Long>();
		for (int i = 0; i < manualRecordList.size(); i++) {
			key = manualRecordList.get(i).getRoleId();
			keys.add(key);
		}
		//遍历所有人员类型
		for (Long k : keys) {
			//map的val，将筛选出的数据加入到val中
			List<ManualRecord> val = new ArrayList<ManualRecord>();
			//遍历所有人工录入的数据，将每条记录的人员类型Id（roleId）与k比较，相同的加入到同一个List中，不同的则创建新的List
			for (int i = 0; i < manualRecordList.size(); i++) {
				maRecord = manualRecordList.get(i);
				roleId = maRecord.getRoleId();
				if(roleId == k){
					val.add(maRecord);
				}
			}
			manualMap.put(k, val);
		}
		return manualMap;
	}
	
	/**
	 * 计算依从率
	 * 根据传进来的手卫生记录计算该数据的依从率
	 * @param manualRecords
	 * @return
	 */
	public static String countRate(List<ManualRecord> manualRecords){
		double right = 0;
		double total = manualRecords.size();
		for (int i = 0; i < manualRecords.size(); i++) {
			if(manualRecords.get(i).getIsRight() == 1){
				right++;
			}
		}
		//计算依从率，保留两位小数（非四舍五入）
		String rate = new java.text.DecimalFormat("###0.00").format(right/total*100);
		return rate;
	}
	
	/**
	 * 计算正确率
	 * 根据传进来的手卫生记录计算正确率
	 * @param manualRecords
	 * @return
	 */
	public static String countCorrect(List<ManualRecord> manualRecords){
		double washNumber = 0;
		double rightNumber = 0;
		for (int i = 0; i < manualRecords.size(); i++) {
			if(manualRecords.get(i).getIsWashHand() == 1){
				washNumber++;
			}
		}
		for (int i = 0; i < manualRecords.size(); i++){
			if(manualRecords.get(i).getIsRight() == 1){
				rightNumber++;
			}
		}
		String rate = new java.text.DecimalFormat("###0.00").format(rightNumber/washNumber*100);
		return rate;
	}
	
	/**
	 * 分组，根据洗手时机将手卫生记录分到对应洗手时机的组中
	 * @param manualRecords
	 * @return
	 */
	public static Map<Short, List<ManualRecord>> groupByOccassion(List<ManualRecord> manualRecords){
		Map<Short, List<ManualRecord>> manualRecordMap = new HashMap<Short, List<ManualRecord>>();
		Short key = 0;
		Set<Short> keys = new HashSet<Short>();
		//得到所有的洗手时机，并去重
		for (int i = 0; i < manualRecords.size(); i++) {
			key = manualRecords.get(i).getOccassion();
			keys.add(key);
		}
		for(Short k : keys){
			List<ManualRecord> val = new ArrayList<ManualRecord>();
			for (int i = 0; i < manualRecords.size(); i++) {
				ManualRecord mr = manualRecords.get(i);
				if(k == mr.getOccassion()){
					val.add(mr);
				}
			}
			manualRecordMap.put(k, val);
		}
		return manualRecordMap;
	}
	
	/**
	 * 计算执行率（洗手的/(洗手的+未洗手的)）
	 * 传入数据，根据是否进行手卫生来计算出执行率
	 * @param manualRecords
	 * @return
	 */
	public static String rateOfExecute(List<ManualRecord> manualRecords){
		double washHandNumber = 0;
		double total = manualRecords.size();
		for (int i = 0; i < manualRecords.size(); i++) {
			if(manualRecords.get(i).getIsWashHand() == 1){
				washHandNumber++;
			}
		}
		String rate = new java.text.DecimalFormat("###0.00").format(washHandNumber/total*100);
		return rate;
	}

}

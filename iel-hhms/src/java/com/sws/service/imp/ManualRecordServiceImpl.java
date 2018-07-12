package com.sws.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sws.dao.ManualRecordDao;
import com.sws.model.ManualRecord;
import com.sws.service.ManualRecordService;

@Service("manualRecordService")
public class ManualRecordServiceImpl implements ManualRecordService {

	@Autowired
	private ManualRecordDao manualRecordDao;
	
	@Override
	public void add(ManualRecord manualRecord) {
		manualRecordDao.add(manualRecord);
	}

	@Override
	public List<ManualRecord> findAll() {
		List<ManualRecord> manualRecords = manualRecordDao.findAll();
		return manualRecords;
	}

	@Override
	public List<ManualRecord> findByDepartIdAndTimeAndWay(Long departId,
			Date timeStart, Date timeEnd, Short way) {
		List<ManualRecord> manualRecords = manualRecordDao.findByDepartIdAndTimeAndWay(departId, timeStart, timeEnd, way);
		return manualRecords;
	}

	@Override
	public List<ManualRecord> findByTimeAndWay(Date timeStart,
			Date timeEnd, Short way) {
		List<ManualRecord> manualRecords = manualRecordDao.findByTimeAndWay(timeStart, timeEnd, way);
		return manualRecords;
	}

	@Override
	public List<ManualRecord> findByDepartIdAndTime(Long departId,
			Date timeStart, Date timeEnd) {
		List<ManualRecord> manualRecords = manualRecordDao.findByDepartIdAndTime(departId, timeStart, timeEnd);
		return manualRecords;
	}

	@Override
	public List<ManualRecord> findByTime(Date timeStart, Date timeEnd) {
		List<ManualRecord> manualRecords = manualRecordDao.findByTime(timeStart, timeEnd);
		return manualRecords;
	}

}

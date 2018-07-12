package com.sws.service;

import java.util.List;

import com.gk.extend.hibernate.entity.QueryEntity;
import com.sws.model.PreRateStatistics;



public interface PreRateStatisticsService {
	public PreRateStatistics getById(Long id);
	public void save(PreRateStatistics info);
	public void update(PreRateStatistics info);
	public void deleteAll(String ids);
	public void saveAll(List<PreRateStatistics> list);
	public List<PreRateStatistics> findBy(String[] propertyName, Object[] value);
	public List<PreRateStatistics> queryList(QueryEntity queryEntity);

}

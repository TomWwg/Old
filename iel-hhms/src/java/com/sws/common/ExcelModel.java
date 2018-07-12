package com.sws.common;

import java.util.List;

public class ExcelModel {
	// 生成的excel路径
	private String		filePath;
	// excel标题
	private String[]	title;
	// 行数据
	private List		data;
	private String[]	dataKey;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public String[] getDataKey() {
		return dataKey;
	}

	public void setDataKey(String[] dataKey) {
		this.dataKey = dataKey;
	}

}

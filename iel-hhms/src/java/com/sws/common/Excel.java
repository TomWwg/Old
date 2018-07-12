package com.sws.common;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sws.common.entity.DepartRateEntity;



public class Excel {
	HSSFRow				row		= null;
	HSSFCell			cell	= null;
	private ExcelModel	em;

	public Excel(ExcelModel em) {
		this.em = em;
	}

	/**
	 * 创建新的excel和新的sheet
	 * 
	 * @return
	 */
	public HSSFSheet getSheet() {
		// 创建新的Excel工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();// 创建默认的sheet
		return sheet;
	}

	/**
	 * 创建表头
	 * 
	 * @param tableHeader
	 */
	public void createTableHeader(HSSFSheet sheet, String[] tableHeader) {
		row = sheet.createRow(0);
		for (int i = 0; i < tableHeader.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(tableHeader[i]);
		}
	}

	/**
	 * 创建行数据
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param list
	 *            List<Object> 数据
	 */
	@SuppressWarnings("unchecked")
	public void createTableRow(HSSFSheet sheet, List<Object> list) {
		for (int i = 0; i < list.size(); i++) {// 创建行
			row = sheet.createRow(i + 1);
			Map<String, String> rowData = MapAndBean.convertBean(list.get(i),
					true);
			int j = 0;
			for (String key : em.getDataKey()) {
				// 创建列
				cell = row.createCell(j);
				// 定义单元格为字符串类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(getValue(rowData.get(key)));
				// 应用格式
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				j++;
			}
		}
	}

	private String getValue(Object obj) {
		if (obj instanceof String) {
			return obj.toString();
		} else if (obj instanceof Boolean) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 导出到excel表中
	 * 
	 * @param outputFile
	 * @param workbook
	 */
	public void exportExcel(String outputFile, String[] tableHeader,
			List<Object> list) {
		// 创建新的Excel工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();// 创建默认的sheet
		this.createTableHeader(sheet, tableHeader);
		this.createTableRow(sheet, list);
		System.out.println("文件正在生成......");
		try {
			// 新建输出文件流
			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel工作薄存盘
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();

		} catch (Exception e) {
			System.out.println("已运行CreateXLS();" + e);
		}
		System.out.println("文件已生成......");
	}

	@SuppressWarnings("unchecked")
	public void exportExcel() {
		exportExcel(em.getFilePath(), em.getTitle(), em.getData());
	}

	public HSSFWorkbook exportExcelForStrem(String outputFile,
			String[] tableHeader, List<Object> list) {
		// 创建新的Excel工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();// 创建默认的sheet
		this.createTableHeader(sheet, tableHeader);
		this.createTableRow(sheet, list);
		return workbook;
	}

	@SuppressWarnings("unchecked")
	public HSSFWorkbook exportExcelForStrem() {
		return exportExcelForStrem(em.getFilePath(), em.getTitle(), em
				.getData());
	}

	//动态生成报表
	public HSSFWorkbook exportExcelForStremRate(List<DepartRateEntity> departRateList) {
		// 创建新的Excel工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();// 创建默认的sheet
		this.createTableHeader(sheet, em.getTitle());
		for (int i = 0; i < departRateList.size(); i++) {// 创建行
			row = sheet.createRow(i + 1);
			int j = 0;
			cell = row.createCell(j);//创建列
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(departRateList.get(i).getTime());
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			j++;
			for(String rate:departRateList.get(i).getRateList()){
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(rate);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				j++;
			}
		}
		return workbook;
	}
	public static void main(String[] args) {

	}
}

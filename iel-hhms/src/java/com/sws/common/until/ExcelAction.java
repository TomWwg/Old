package com.sws.common.until;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;

import com.sws.common.baseAction.BaseAction;



/**
 * @Description : excel导出的时候可以使用
 **/
public abstract class ExcelAction<T> extends BaseAction<Object> {

	private static final long serialVersionUID = -3689244657017999809L;

	/**
	 * cellStyle1:固定样式
	 * 
	 */
	private HSSFCellStyle cellStyle = null;

	/**
	 * newRow:记录某一行
	 * 
	 */
	private HSSFRow row = null;

	private HSSFWorkbook wb = null;

	private HSSFSheet sheet = null;
	
	//违章信息打印时暂时存储的excel位置

	/**
	 * @Description : 设置记录的行
	 **/
	protected void setRow(HSSFRow row) {
		this.row = row;
	}

	/**
	 * @Description : 设置记录的行
	 **/
	protected void setRow(HSSFSheet sheet, Integer i) {
		if (sheet != null)
			this.row = sheet.createRow((short) i.intValue());
	}

	/**
	 * @Description : 设置记录的行
	 **/
	protected void setRow(Integer i) {
		if (this.sheet != null){
			this.row = this.sheet.createRow(i.intValue());
		}
	}
	/**
	 * @Description : 输出excel文件
	 **/
	protected void outputFile(String sentFileName) throws Exception {
		if (this.wb == null || sentFileName == null || "".equalsIgnoreCase(sentFileName)){
			return;
		}
		ServletActionContext.getResponse().reset();
		ServletActionContext.getResponse().setContentType("utf-8");
		ServletActionContext.getResponse().setContentType("application/msexcel");
		ServletActionContext.getResponse().setHeader("Content-Disposition",
		"attachment; filename=" + java.net.URLEncoder.encode(sentFileName + ".xls", "UTF-8"));
		//"attachment; filename=" + new String((sentFileName + ".xls").getBytes("gb2312"),"iso8859-1"));
		this.wb.write(ServletActionContext.getResponse().getOutputStream());
		ServletActionContext.getResponse().getOutputStream().flush();
		ServletActionContext.getResponse().getOutputStream().close();
	}
	

	/**
	 * @throws IOException 
	 * @Description : 加载excel文件,并加载sheet
	 **/
	protected void loadFileAndSheet(final String fileName, final Integer i) throws IOException{
		if (this.wb == null) { //如果没有加载excel文件则加载			
			this.loadFile(fileName);
		}
		this.sheet = this.wb.getSheetAt(i);	//获取每一个分页的对象文档中默认有三个sheet，默认第一个下角标为0。
	}
	
	/**
	 * @Description : 加载excel文件
	 **/
	protected void loadFile(final String fileName) throws IOException {
		POIFSFileSystem fs = null;
		String excelPath = "download" + File.separator + "excel" + File.separator + fileName + ".xls";
		//获取资源路径  
		InputStream inputStream = ExcelAction.class.getClassLoader().getResourceAsStream(excelPath);
		fs = new POIFSFileSystem(inputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		this.wb = wb;
		this.fetchStyle();
	}

	/**
	 * excel表的一般格式内容样式的设置
	 * @Description : excel导出时使用
	 **/
	private void fetchStyle() {
		if (wb == null){
			return;
		}
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 11);
		this.cellStyle = wb.createCellStyle();
		HSSFDataFormat format = wb.createDataFormat();
		this.cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderBottom((short) 1);// 下边框
		cellStyle.setBorderLeft((short) 1);// 左边框
		cellStyle.setBorderRight((short) 1);// 右边框
		cellStyle.setBorderTop((short) 1);//
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
	}
	
	/**
	 * 设置单元格的格式，字符串
	 * @Description : excel导出时使用，设置excel中的值
	 **/
	protected void setCellValue(final Integer i, String value) {
		try {
			if (value == null){
				value = "";
			}
			HSSFCell cell0 = this.row.createCell(i.intValue());
			// 在新的poi jar 包中此属性自动会设置 
			// cell0.setEncoding((short) 1);
            cell0.setCellStyle(this.cellStyle);
            cell0.setCellValue(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @Description: 格式化 超链接
	 */
	protected void setFormulaCell(final Integer i, String value, String formula) {
		try {
			if (value == null)
				value = "";
			HSSFCell cell0 = this.row.createCell(i.intValue());
			cell0.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell0.setCellFormula("HYPERLINK(\"" + formula+ "\",\"" + value+ "\")");
			cell0.setCellValue(value);
			//现在超链接单元格看起来和一般的单元格没有分别， 除非你把鼠标放上去才会变成手行光标。 为了和一般的习惯相符，还需要把字符颜色变成蓝色和加上下划线。 这就要用到 style了、
			HSSFCellStyle linkStyle = wb.createCellStyle();
			HSSFFont cellFont= wb.createFont();
			cellFont.setUnderline((byte) 1);
			cellFont.setColor(HSSFColor.BLUE.index);
			cellFont.setFontHeightInPoints((short) 11);
			linkStyle.setFont(cellFont);
			//最后把style应用到cell上去就大功告成了。
			cell0.setCellStyle(linkStyle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @Description: 格式化 超链接，传cellStyle
	* @param
	*/
	protected void setFormulaCell2(final Integer i, String value, String formula, HSSFCellStyle cellStyle) {
		try {
			if (value == null)
				value = "";
			HSSFCell cell0 = this.row.createCell(i.intValue());
			cell0.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell0.setCellFormula("HYPERLINK(\"" + formula+ "\",\"" + value+ "\")");
			cell0.setCellValue(value);
			cell0.setCellStyle(cellStyle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @Description: 创建某一单元格样式
	*/
	protected HSSFCellStyle createCellStyle(){
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFFont cellFont= wb.createFont();
		cellFont.setUnderline((byte) 1);
		cellFont.setColor(HSSFColor.BLUE.index);
		cellFont.setFontHeightInPoints((short) 11);
		cellStyle.setFont(cellFont);
		return cellStyle;
	}
	
	/**
	 * 设备某行单元格的内容，数字
	 * @param i
	 * @param value
	 */
	protected void setCellValue(final Integer i, final Integer value) {
		if (value == null){
			setCellValue(i, "");
		} else {
			setCellValue(i, value + "");
		}
	}

	/**
	 * 设置单元格的内容为时间字符串，日期"yyyy-MM-dd'T'HH:mm:ss"
	 * @param i
	 * @param value
	 * @param format
	 */
	protected void setCellValue(final Integer i, final Date date) {
		this.setCellValue(i, DateUtils.date2StrByYMDHMS(date));
	}

	/**
	 * 放入从map中取到的值
	 * @param i
	 * @param value
	 * @param map
	 */
	protected void setCellValue(final Integer i, final Object value, final HashMap<Object, String> map) {
		this.setCellValue(i, map.get(value));
	}
	
    /**
	 * @Description : 输出excel文件Add图片（.jpeg）文件
	 * @param mapTitle { 
	 * ("fileName",String), 文件名：类型+时间戳
	 * {"location",String}}   jfreeChart生成图至本地的位置
	 * @param dataset
	 **/
    private static BufferedImage bufferImg = null; 
	protected void outputFileAddPic(Map<String,String>mapTitle) throws Exception {
		if (this.wb == null || mapTitle.get("fileName") == null || "".equalsIgnoreCase(mapTitle.get("fileName"))){
			return;
		}
		String webpath=getRequest().getContextPath();
		ServletActionContext.getResponse().reset();
		ServletActionContext.getResponse().setContentType("utf-8");
		ServletActionContext.getResponse().setContentType("application/msexcel");
		ServletActionContext.getResponse().setHeader("Content-Disposition",
		"attachment; filename=" + java.net.URLEncoder.encode(mapTitle.get("fileName") + ".xls", "UTF-8"));
		//"attachment; filename=" + new String((sentFileName + ".xls").getBytes("gb2312"),"iso8859-1"));
		if(mapTitle.get("location")!=null&&!"".equals(mapTitle.get("location"))){
		//向excel中插入图片,在这里可以将上面的生成的图传递过来，插入到excel中
		//start
	        HSSFPatriarch patriarch=sheet.createDrawingPatriarch();
	        HSSFClientAnchor anchor=new HSSFClientAnchor(0, 0, 512, 255, (short)5, 5, (short)10, 30);
	        anchor.setAnchorType(3);
	        ByteArrayOutputStream handlePicture  = new ByteArrayOutputStream();
	        handlePicture  = handlePicture(mapTitle.get("location")); 
			patriarch.createPicture(anchor, wb.addPicture(handlePicture .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

		//end
		}
		this.wb.write(ServletActionContext.getResponse().getOutputStream());
		ServletActionContext.getResponse().getOutputStream().flush();
		ServletActionContext.getResponse().getOutputStream().close();
	}
	
	 private static ByteArrayOutputStream handlePicture(String pathOfPicture) {  
	        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();  
	        try {  
	            bufferImg = ImageIO.read(new File(pathOfPicture));  
	            ImageIO.write(bufferImg, "jpeg", byteArrayOut);  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return byteArrayOut;  
	    }  
	 
	
	
}

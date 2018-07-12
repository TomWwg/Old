package com.sws.common.until;

/**author:tangfeng
 * create date:2010/08/20
 * version :1.0
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUpload {
	private static final Log log = LogFactory.getLog(FileUpload.class);
	//文件标题请求参数
	private String title;
	//上传文件域
	private File upload;
	//上传文件类型
	private String uploadContentType;
	//保存路径
	private String savePath;
	//上传文件的名称
	private String uploadFileName;
	//文件大小
	private int fileSize = 0;
	
	private String renameFileName = "/hikvision_license_file";
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public boolean renameFile(){
		if(this.savePath == null || this.savePath.trim().equals("")){
			log.info("file save path is null.");
			return false;
		}
		if(this.uploadFileName == null || this.uploadFileName.trim().equals("")){
			log.info("file name is null.");
			return false;
		}
		File file = new File(savePath+uploadFileName);
		File renamefile = new File(savePath+renameFileName);
		if(renamefile.exists()){
			renamefile.delete();
		}
		if(file.exists() 
				&& file.isFile() 
				&& file.canRead()){
			if(!file.renameTo(renamefile)){
				log.info("rename '"+renamefile+"' failed.");
			}
		}
		return true;
	}
	public boolean renameFile2OldName(){
		if(this.savePath == null || this.savePath.trim().equals("")){
			log.info("file save path is null.");
			return false;
		}
		if(this.uploadFileName == null || this.uploadFileName.trim().equals("")){
			log.info("file name is null.");
			return false;
		}
		File renamefile = new File(savePath+uploadFileName);
		if(renamefile.exists()){
			renamefile.delete();
		}
		File file = new File(savePath+renameFileName);
		if(file.exists() 
				&& file.isFile() 
				&& file.canRead()){
			if(!file.renameTo(renamefile)){
				log.info("rename '"+renamefile+"' failed.");
			}
		}
		return true;
	}
	public boolean removeFile(){
		if(this.savePath == null || this.savePath.trim().equals("")){
			log.info("file save path is null.");
			return false;
		}
		
		File file = new File(savePath+renameFileName);
		if(file.exists() 
				&& file.isFile() 
				&& file.canRead()){
			if(!file.delete()){
				log.info("delete file failed.");
			}
		}
		return true;
	}

	/**
	 * 执行上传动作
	 * @return boolean
	 * @throws IOException
	 */
	
	public boolean executeUpload() throws IOException{
		if(this.savePath == null || this.savePath.trim().equals("")){
			log.info("file save path is null.");
			return false;
		}
		if(this.uploadFileName == null || this.uploadFileName.trim().equals("")){
			log.info("file name is null.");
			return false;
		}

		if(this.upload == null ){
			log.info("file is null.");
			return false;
		}
		if(this.fileSize <= 0 || this.fileSize < 1024){
			log.info("file size is "+this.fileSize+" .");
			return false;
		}
		//输出文件
		FileOutputStream fos = new FileOutputStream(savePath+uploadFileName);
		//输入文件
		FileInputStream fis = new FileInputStream(upload);
		byte[] buffer = new byte[fileSize];
		int len = 0;
		//开始执行文件读写
		while((len = fis.read(buffer)) >0){
			fos.write(buffer,0,len);
		}
		fis.close();
		fos.close();
		return true;
	}
}

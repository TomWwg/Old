package com.sws.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.gk.extend.hibernate.dao.LicParams;
import com.sws.common.baseAction.BaseAction;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.CommUtil;
import com.sws.common.until.DateUtils;
import com.sws.common.until.FileUpload;
import com.sws.service.LicenseService;

public class LicenseAction extends BaseAction<LicenseAction> {

	private static final long serialVersionUID = 6174600173664561315L;
	
	private Logger log = LoggerFactory.getLogger(LicenseAction.class);
	
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private LicParams licParams;
	
	private File licenseFile;	//上传的license文件
	
	private String savePath;	//license文件保存路径
	private String licenseFileFileName;	//license文件名
	private String licenseFileContentType;	//license文件保类型
	/**
	 * 获取过期时间，以判断LICENSE是否能使用
	 */
	public void getVersion() {
		// 如果存在证书，设置过期时间
		if(!CommUtil.isNullOrEmpty(licParams.getLicNo())){
			String expire = licParams.getExpireDate();	//2055-05-05
			JSONObject json = new JSONObject();
			Map<String, Object> infoMap = new HashMap<String, Object>();
			long expireDateDay = 0;
			expireDateDay = DateUtils.daysOfTwoDate(DateUtils.str2DateByYMD(expire), new Date());
			infoMap.put("expireDateDay", expireDateDay);
			infoMap.put("versionInfo", licParams);
			json.put("success", success);
			json.put("message", msg);
			json.put("result", infoMap);
			writeResponse(json.toString());
		} else {
			success = false;
			msg = "证书错误，请上传授信证书";
			writeResponse();
		}
	}

	/**
	 * 上传证书
	 */
	public void uploadLicense() {
 		FileUpload fileUpload = new FileUpload();
		fileUpload.setSavePath(getSavePath());
		fileUpload.setFileSize(SysStatics.LICNESE_FILE_SIZE);
		fileUpload.setUpload(licenseFile);
		fileUpload.setUploadFileName(SysStatics.LICENSE_FILE);
		try {
			if (licenseFile != null && licenseFile.length() <= SysStatics.LICNESE_FILE_SIZE) {
				// 上传证书
				if(fileUpload.executeUpload()) {
					// 解析证书
					if(!licenseService.loadLicence(licParams)) {
						success = false;
						msg = "证书内容解析错误";
					} else {
						success = true;
						msg = "证书上传成功";
					}
				} else {
					success = false;
					msg = "证书上传失败";
				}
			} else {
				success = false;
				msg = "证书文件长度错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = e.getMessage();
		}
		getVersion();
		log.info(msg);
	}

	
	public File getLicenseFile() {
		return licenseFile;
	}
	public void setLicenseFile(File licenseFile) {
		this.licenseFile = licenseFile;
	}

	public String getSavePath() {
		return ServletActionContext.getServletContext().getRealPath(savePath);
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getLicenseFileFileName() {
		return licenseFileFileName;
	}
	public void setLicenseFileFileName(String licenseFileFileName) {
		this.licenseFileFileName = licenseFileFileName;
	}

	public String getLicenseFileContentType() {
		return licenseFileContentType;
	}
	public void setLicenseFileContentType(String licenseFileContentType) {
		this.licenseFileContentType = licenseFileContentType;
	}
	
	
}

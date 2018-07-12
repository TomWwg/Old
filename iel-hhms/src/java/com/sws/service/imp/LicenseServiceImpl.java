package com.sws.service.imp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import com.gk.extend.hibernate.dao.LicParams;
import com.sws.common.entity.LicConstant;
import com.sws.common.statics.SysStatics;
import com.sws.common.until.CryptFile;
import com.sws.service.LicenseService;
import com.sys.core.util.StringUtils;



@Service
public class LicenseServiceImpl implements LicenseService{

	private static final Log log = LogFactory.getLog(LicenseServiceImpl.class);

	/**
	 * 加载licence文件，并进行解析licence
	 */
	public boolean loadLicence(LicParams licParams) {
		try {
			Document licenceDoc = getLicenceDoc();
			paraseDoc(licParams, licenceDoc);
		}
		catch (Exception e) {
			log.error("解析证书失败:" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 使用公钥RSAPublicKey解密license证书，返回证书内容
	 * @return
	 * @throws Exception
	 */
	private Document getLicenceDoc() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		String licReaderClassPath = LicenseServiceImpl.class.getResource("").getPath();
		licReaderClassPath = java.net.URLDecoder.decode(licReaderClassPath, "utf-8");
		String licencePath = licReaderClassPath.substring(0, licReaderClassPath.indexOf("/com/sws/")) + SysStatics.LICENSE_FILE;
		String pubKeyPath = licReaderClassPath.substring(0, licReaderClassPath.indexOf("/com/sws/")) + SysStatics.RSA_PUB_KEY_FILE;
		InputSource is = null;
		is = new InputSource(new StringReader(CryptFile.decryptFile(licencePath, pubKeyPath)));
		doc = builder.build(is);
//		doc2String(doc);
		return doc;
	}

	/**
	 * 解密license后，为全局bean：LicParams设值
	 * @param licParams
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	private LicParams paraseDoc(LicParams licParams, Document doc) throws Exception {
		Element rootElement = doc.getRootElement();
		Element controllerElement = getSubElement(rootElement, LicConstant.CONTROLLER);
		// 证书编号
		licParams.setLicNo(getSubContent(rootElement, LicConstant.LIC_NO));
		// 证书类型
		licParams.setLicType(getSubContent(rootElement, LicConstant.LIC_TYPE));
		// 客户名称
		licParams.setCustomName(getSubContent(rootElement, LicConstant.CUSTOM_NAME));
		// 发布时间
		licParams.setReleaseDate(getSubContent(rootElement, LicConstant.RELEASE_DATE));
		// 过期时间
		licParams.setExpireDate(getSubContent(rootElement, LicConstant.EXPIRE_DATE));
		// 设备数
		licParams.setDeviceNumLimit(getIntSubController(controllerElement, LicConstant.CONTROLLER_DEVICE, LicConstant.DEVICE_NUM_LIMIT));
		return licParams;

	}

	private String getSubContent(Element rootElement, String key) throws IOException {
		Element releaseDateElement = getSubElement(rootElement, key);
		if (releaseDateElement == null) {
			return null;
		}
		return releaseDateElement.getText();
	}

	private int getIntSubController(Element controllerElement, String controllerName, String key) throws IOException {
		Element controller = getSubElement(controllerElement, controllerName);
		if(controller == null) {
			return 0;
		}
		Element element = getSubElement(controller, key);
		String deviceNumLimit = element.getText();
		if (StringUtils.isNotBlank(deviceNumLimit)) {
			return Integer.parseInt(deviceNumLimit);
		}
		else {
			return 0;
		}
	}

	private Element getSubElement(Element parentElement, String key) throws IOException {
		List<Element> licenseElements = parentElement.getChildren();
		Element result = null;
		if (licenseElements != null) {
			for (Element element : licenseElements) {
				if (element.getName().equals(key)) {
					result = element;
					break;
				}
			}
		}
		return result;
	}

	public static void doc2String(Document doc) throws Exception {
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(format);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		xmlout.output(doc, bo);
		log.info(bo.toString());
	}
}

package com.sws.common.until;

/**
 * @Tel:
 * @address: http://www.gk.com
 * @date: 2013-3-27 上午09:41:47
 */
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sys.core.util.ArrayUtils;
import com.sys.core.util.StringUtils;
import com.sys.core.util.coder.Base64Utils;
import com.sys.core.util.exception.AESErrorException;

/**
 * 封装V6.x平台中AES加密和解密的算法.具体的加密过程这里不叙述。
 * <p>
 * </p>
 * @author wangyabei 2013-3-27 上午09:41:47
 * @version V1.0
 */
public class AESUtils {
	
	private static byte END_START = 59;
	
	private static Cipher cipher = null;
	
	private static Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException{
//		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher = Cipher.getInstance("AES/CFB/NoPadding");
		return cipher;
	}
	
	/**
	 * 使用aes加密原文
	 * <p>
	 * @author wangyabei 2013-4-17 上午11:31:19
	 * @param sSrc  原文
	 * @param sKey  密钥
	 * @param ivParameter cbc加密的起始向量长度必须是16
	 * @return
	 * @throws AESErrorException
	 */
	public static String encryptWithBase64(String sSrc, String sKey,String ivParameter) throws AESErrorException {
		ensureSrcNotNull(sSrc);
		ensureKeyIsRight(sKey);
		try{
			byte[] raw = sKey.getBytes("UTF-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = getCipher();
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes(Charset.forName("UTF-8")));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
			return Base64Utils.encodeBase64String(encrypted,false);
		}catch(Exception e){
			throw new AESErrorException(e);
		}
	}
	
	/**
	 * 使用aes算法解密
	 * 算法是方法encryptWithBase64的反向
	 * @see AESUtils#encryptWithBase64(java.lang.String ,java.lang.String);
	 * @author wangyabei 2013-4-17 上午11:34:07
	 * @param sSrc 密文
	 * @param sKey 密钥
	 * @param ivParameter cbc解密的起始向量 长度必须是16
	 * @return
	 * @throws AESErrorException
	 */
	 public static String decryptWithBase64(String sSrc, String sKey,String ivParameter) throws AESErrorException {
		 ensureSrcNotNull(sSrc);
		 ensureKeyIsRight(sKey);
		 try {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = getCipher();
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes(Charset.forName("UTF-8")));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted = Base64Utils.decodeBase64(sSrc);
			byte[] original = cipher.doFinal(encrypted);
//			original = removeEndFlag(original);
			return new String(original,"utf-8");
		} catch (Exception ex) {
			throw new AESErrorException(ex);
		}
	 }
	
	@SuppressWarnings("unused")
    private static byte[] removeEndFlag(byte[] original) {
		int index = ArrayUtils.lastIndexOf(original, END_START);
		int length = original.length;
		if(index >= length -1){
			return original;
		}
		if(original[index + 1] == 0){
			return ArrayUtils.subarray(original, 0, index);
		}
	    return original;
    }


	@SuppressWarnings("unused")
    private static byte[] buildFullSrc(String sSrc) throws UnsupportedEncodingException {
		byte[] src = sSrc.getBytes("utf-8");
		int len = src.length;
		if(len % 16 == 0){
			return src;
		}
		int append = 16 - (len % 16);
		byte[] appendArr = new byte[append];
		appendArr[0] = END_START;
		for(int i = 1; i < append; i++){
			appendArr[i] = 0;
		}
		return ArrayUtils.addAll(src, appendArr);
    }

	private static void ensureSrcNotNull(String sSrc) {
		if(StringUtils.isEmpty(sSrc)){
			throw new IllegalArgumentException("src is empty.");
		}
    }
	
	private static void ensureKeyIsRight(String key){
		// 判断Key是否正确
		if (key == null) {
			throw new IllegalArgumentException("key is empty.");
		}
		// 判断Key是否为16位
		if (key.length() != 16) {
			throw new IllegalArgumentException("Key长度不是16位");
		}
	}
	
	/**
	 * 解密字符串，待解密内容的长度必须是16的倍数，如果不足，这需要补足
	 * @author wangyabei 2013-3-27 上午10:23:28
	 * @param sSrc 需要解密的字符串，该字符串是将加密后的字节数组进行转化过的
	 * @param sKey 解密key，字符串的长度必须是16
	 * @return
	 * @throws AESErrorException
	 */
	@Deprecated
	public static String decrypt(String sSrc, String sKey) throws AESErrorException {
		ensureKeyIsRight(sKey);
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = getCipher();
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original,Charset.forName("utf-8"));
			return originalString;
		} catch (Exception ex) {
			throw new AESErrorException(ex);
		}
	}
	
	/**
	 * 根据key加密字符串，返回值是经过补全的字符串。16进制形式的字符串 “55b1d69cbb4c857e0c7”
	 * 
	 * @author wangyabei 2013-3-27 上午10:26:51
	 * @param sSrc 
	 * @param sKey  解密key，字符串的长度必须是16
	 * @return 
	 * @throws AESErrorException
	 */
	@Deprecated
	public static String encrypt(String sSrc, String sKey) throws AESErrorException {
		ensureKeyIsRight(sKey);
		try{
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = getCipher();
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes(Charset.forName("utf-8")));
			return byte2hex(encrypted).toLowerCase();
		}catch(Exception e){
			throw new AESErrorException(e);
		}
	}
	
	private static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l < 0 || l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte)Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}
	
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString().toUpperCase();
	}
	
	public static void main(String[] args) throws AESErrorException {
		String kk = "中文abc1234dd";
		String key = "1234567890123456";
		String test =encryptWithBase64(kk,key,"0000000000000000");
		System.out.println(test);
		test = "T8k93DYl6YGkpdTDXWmFDi7lhOCJ6jxml3UWFeCjDiDK2V+pwP7MmL2bTqmDTE6W/9MQ8Ueq1DSwTtdy/sF04w==";
		System.out.println(decryptWithBase64(test, "7f381e086d082b47","8807599888075998"));
		
    }
}

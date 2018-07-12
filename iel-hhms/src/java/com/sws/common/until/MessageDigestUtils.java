package com.sws.common.until;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCERSAPublicKey;
import org.bouncycastle.openssl.PEMReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class MessageDigestUtils {
	static {
		Security.addProvider(new BouncyCastleProvider()); 
	}
	
	public static String getMD5(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes("UTF-8"));
			byte[] b = md.digest();
			return bufferToHex(b, 0, b.length);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static String getSHA256(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(src.getBytes("UTF-8"));
			byte[] b = md.digest();
			return bufferToHex(b, 0, b.length);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
//	public static String createVerifyCode(String codeId){
//		String code=UUID.randomUUID().toString();
//		CacheUtils.setVerifyCode(codeId, code);
//		return code;
//	}
	
	public static String parseDES(String value,String key){
		
		// 从原始密钥数据创建DESKeySpec对象
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			keyFactory.generateSecret(dks);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**      
     * 根据键值进行加密,des加密算法
	 *@author wangyabei 2015-1-21上午09:40:17
	 * @return
	 */ 
	public static String encrypt(String data, String key) throws Exception {         
		byte[] bt = encrypt(data.getBytes(), key.getBytes());         
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	} 
    /**      
     * 根据键值进行加密,des加密算法
     * @author wangyabei 2015-1-21上午09:40:17
     * @return      
     **/    
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();           
		DESKeySpec dks = new DESKeySpec(key);           
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");         
		SecretKey securekey = keyFactory.generateSecret(dks);           
		Cipher cipher = Cipher.getInstance("DES");           
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);           
		return cipher.doFinal(data);     
	}
	/**
     * @param source
     *            加密后的byte数组。可用加密方法encrypt（“String”）生成即可
     * @return 解密后的字符串。
     * @Description 解密算法。
     */
    @SuppressWarnings("unused")
	private byte[] decrypt(byte[] source,Key key) {
        byte[] dissect = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);// 使用私钥解密
            dissect = cipher.doFinal(source);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return dissect;
    }

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
												// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	public static String des(String key,String msg){
		
		try {
			
			BASE64Decoder dec = new BASE64Decoder();
	        byte[] messageBytes = dec.decodeBuffer(msg);
			
			// 从原始密钥数据创建DESKeySpec对象
	        DESKeySpec dks = new DESKeySpec(key.getBytes("utf-8"));
	 
	        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey securekey = keyFactory.generateSecret(dks);

			Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, securekey);
			
			byte[] b = c.doFinal(messageBytes);
			
			return new String(b, Charset.forName("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static final String publicKey="-----BEGIN RSA PUBLIC KEY-----\n"+
"MIGHAoGBAOEiyqtuQJ3V9tH8adh+n/XtFPg6jpJn2B9C4DfKbz0B18QaHzk64wnv\n"+
"nyaxkC+Ke9ndi16pGhKzRX/hgbRZUNs9kIxF8JzjO/bx+1IDfl+tIHYsptCpgqCB\n"+
"0sxN/wjaNtuqAbbgOBCLtS4UfZjVjhR6j1hZ6Jx9opaW5srgJ/WLAgED\n"+
"-----END RSA PUBLIC KEY-----";
	private static PEMReader reader;
	public static String decodeByRSAPrivateKey(String base64Str)throws Exception{
		PrivateKey privateKey =getPrivateKey();
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE,privateKey);
		BASE64Decoder dec = new BASE64Decoder();
		return new String(cipher.doFinal(dec.decodeBuffer(base64Str)),"UTF-8");
	}
	public static String encodeByRSAPublicKeyAndBase64(String str)throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE,getPublicKey()); 
		byte[] temp=cipher.doFinal(str.getBytes("UTF-8"));
		String msg=new BASE64Encoder().encode(temp);
		return msg;
	}
	private static PrivateKey getPrivateKey()throws Exception{
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:sec.pem");
//		FileInputStream in=new FileInputStream("c:/sec.pem");
		BASE64Decoder dec = new BASE64Decoder();
		byte[] buff=dec.decodeBuffer(resource.getInputStream());
//		byte[] buff=dec.decodeBuffer(in);
		KeyFactory kf = KeyFactory.getInstance("RSA"); 
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buff); 
		PrivateKey privateKey = kf.generatePrivate(keySpec);  
		return privateKey;
	}
	private static PublicKey getPublicKey()throws Exception{
		Security.addProvider(new BouncyCastleProvider());  
		reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(publicKey.getBytes("UTF-8")))); 
		JCERSAPublicKey publicKey = (JCERSAPublicKey) reader.readObject(); 
		return publicKey;
	}
	public static void main(String[] args) throws Exception{
		String msg="中文";
		String t=encodeByRSAPublicKeyAndBase64(msg);
		System.out.println(t);
		System.out.println(t.length());
		System.out.println(decodeByRSAPrivateKey(t));
		System.out.println(decodeByRSAPrivateKey("3gdytNYUhQs5MMTm51bkgNOB83iYCY1OZPe82C3TKl7gBWaVRN48keFibKaUpgIt2WGZTHG+00dD3RTfrN4zVxj+iBKfXAmTTj3Hp9uvyTUohC2YjTlWUPEUNDYCUHadPnIye/LBORF0B3WPVQ3lR/7A7XoXsYN4kUOgA/bs2vk="));
		System.out.println(decodeByRSAPrivateKey("uO/Dhtb37mSahlRkkcvNXva/oZzZn+fFmLamrBLmLCqgU/455uDviL57ajuXPMkv/iGy8HLRYZ2XbcWPPeE+GDpMGU1hbDVrUMx/unjvtBN3nKRO9q+NZQPNHF3n78o9dhRwwzC0RgyHZQszqFGDcPin+ioKQXgKXo7iUS8cSaY="));

		//System.out.println(getSHA256("密码Abc123"));
		
	}
	

}

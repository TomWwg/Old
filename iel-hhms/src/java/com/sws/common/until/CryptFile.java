package com.sws.common.until;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptFile {
	
	private static Logger log = LoggerFactory.getLogger(CryptFile.class);
	
	/**
	 * 
	 * Generate a pair of keys and save them in file RSAKey.xml also save the public key in RSAPublicKey.xml and private key in RSAPrivateKey.xml.
	 */
	
	// 参数path为储存key的文件夹地址
	public static void saveRSAKey(String path) {
		FileOutputStream fos = null;
		FileOutputStream fosPu = null;
		FileOutputStream fosPr = null;
		ObjectOutputStream oos = null;
		ObjectOutputStream oosPu = null;
		ObjectOutputStream oosPr = null;
		try {
			SecureRandom sr = new SecureRandom();
			KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
			kg.initialize(1024, sr);
			fos = new FileOutputStream(path + "RSAKey.xml");
			fosPu = new FileOutputStream(path + "RSAPublicKey.xml");
			fosPr = new FileOutputStream(path + "RSAPrivateKey.xml");
			oos = new ObjectOutputStream(fos);
			oosPu = new ObjectOutputStream(fosPu);
			oosPr = new ObjectOutputStream(fosPr);
			// System.out.println("Generating the key pair;");
			KeyPair k = kg.generateKeyPair();
			oos.writeObject(k);
			oosPu.writeObject(k.getPublic());
			oosPr.writeObject(k.getPrivate());
			// System.out.println("Key pair is output;");
			oos.close();
		} catch (NoSuchAlgorithmException e) {
			log.error("saveRSAKey::Cannot save KEY.");
		} catch (FileNotFoundException e) {
			log.error("saveRSAKey::Cannot save KEY.");
		} catch (IOException e) {
			log.error("saveRSAKey::Cannot save KEY.");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					fos = null;
				}
			}
			if (fosPu != null) {
				try {
					fosPu.close();
				} catch (IOException e) {
					fosPu = null;
				}
			}
			if (fosPr != null) {
				try {
					fosPr.close();
				} catch (IOException e) {
					fosPr = null;
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					oos = null;
				}
			}
			if (oosPu != null) {
				try {
					oosPu.close();
				} catch (IOException e) {
					oosPu = null;
				}
			}
			if (oosPr != null) {
				try {
					oosPr.close();
				} catch (IOException e) {
					oosPr = null;
				}
			}
		}
	}
	
	/**
	 * 
	 * Get the pair of keys from the file RSAKey.xml
	 * 
	 * @return KeyPair
	 * 
	 */
	
	public static KeyPair getKeyPair(String path) {
		KeyPair kp = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			KeyPair keypair = (KeyPair)in.readObject();
			kp = keypair;
			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("getKeyPair::Cannot get the key pair.");
		}
		return kp;
	}
	
	/**
	 * 
	 * Get the public key from the file RSAPublicKey.xml
	 * 
	 * @return KeyPair
	 * 
	 */
	
	public static PublicKey getPublicKey(String path) {
		PublicKey pk = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			PublicKey publickey = (PublicKey)in.readObject();
			pk = publickey;
			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("getPublicKey::Cannot get the public key.");
		}
		return pk;
	}
	
	/**
	 * 
	 * Get the private key from the file RSAPrivateKey.xml
	 * 
	 * @return KeyPair
	 * 
	 */
	
	public static PrivateKey getPrivateKey(String path) {
		PrivateKey pk = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			PrivateKey privatekey = (PrivateKey)in.readObject();
			pk = privatekey;
			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("getPrivateKey::Cannot get the private key.");
		}
		return pk;
	}
	
	/**
	 * encrypt srcFile and save the result in destFile
	 * 
	 * @param srcFileName The file which contains the plaintext
	 * @param destFileName The file which will contain the ciphertext.
	 */
	
	public synchronized static void encryptFile(String srcFileName, String destFileName, String privateKeyPath)
			throws Exception {
		Base64OutputStream outputWriter = null; // 将结果用 Base64算法加密
		InputStream inputReader = null;
		try {
			byte[] buf = new byte[116];
			int len = 0;
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(privateKeyPath));
			outputWriter = new Base64OutputStream(new FileOutputStream(destFileName));
			inputReader = new FileInputStream(srcFileName);
			while ((len = inputReader.read(buf)) != -1) {
				byte[] encText = null;
				byte[] newArr = null;
				if (buf.length == len) {
					newArr = buf;
				} else {
					newArr = new byte[len];
					for (int i = 0; i < len; i++) {
						newArr[i] = buf[i];
					}
				}
				cipher.update(newArr);
				encText = cipher.doFinal();
				outputWriter.write(encText);
			}
			outputWriter.flush();
			log.info("Encrypt File :: SUCCESS.");
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			log.error("Encrypt File:: no such algorithm.");
		} catch (NoSuchPaddingException e) {
			// e.printStackTrace();
			log.error("Encrypt File:: no such padding.");
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Encrypt File Error");
		} finally {
			try {
				if (outputWriter != null) {
					outputWriter.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				log.error("Encrypt File:: Empty file scanned during encryption");
				// e.printStackTrace();
			}
		}
	}
	
	/**
	 * Decrypt the srcFile and save the result in destFile
	 * 
	 * @param srcFileName The file which contains the ciphertext.
	 * @param destFileName The file whici will contian the plaintext.
	 */
	
	public synchronized static String decryptFile(String srcFileName, String publicKeyPath) throws Exception {
		StringBuffer result = new StringBuffer();
		Base64InputStream inputReader = null; // 将输入用Base64算法解码
		try {
			byte[] buf = new byte[128];
			int len = 0;
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, getPublicKey(publicKeyPath));
			inputReader = new Base64InputStream(new FileInputStream(srcFileName));
			while ((len = inputReader.read(buf)) != -1) {
				byte[] decText = null;
				byte[] newArr = null;
				if (buf.length == len) {
					newArr = buf;
				} else {
					newArr = new byte[len];
					for (int i = 0; i < len; i++) {
						newArr[i] = buf[i];
					}
				}
				cipher.update(newArr);
				decText = cipher.doFinal();
				result.append(new String(decText));
			}
			log.info("Decrypt File :: SUCCESS.");
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			log.error("Decrypt File:: no such algorithm.");
		} catch (NoSuchPaddingException e) {
			// e.printStackTrace();
			log.error("Decrypt File:: no such padding.");
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Decrypt File Error");
		} finally {
			try {
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				log.error("Decrypt File:: Empty file scanned during decryption");
				// e.printStackTrace();
			}
		}
		return result.toString();
	}

//	/**
//	 *
//	 * @param srcFileName
//	 * @return
//	 * @throws Exception
//	 */
//	public static String decryptFile(String srcFileName) throws Exception {
//		// System.out.println("Starting 2 decrypt "+srcFileName+" with public key ");
//
//		Cipher cipher;
//		StringBuffer result = new StringBuffer();
//		BufferedInputStream inputReader = null;
//
//		try {
//			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//			byte[] buf = new byte[128];
//			int bufl;
//
//			inputReader = new BufferedInputStream(new FileInputStream(srcFileName));
//			byte[] b = new byte[8];
//			inputReader.read(b);
//			Integer a = Integer.valueOf(new String(b));
//			b = new byte[a];
//			inputReader.read(b);
//			BigInteger pub = new BigInteger(new String(b));
//			b = new byte[8];
//			inputReader.read(b);
//			a = Integer.valueOf(new String(b));
//			b = new byte[a];
//			inputReader.read(b);
//			BigInteger moudle = new BigInteger(new String(b));
//			RSAPublicKey publickKey = new RSAPublicKeyImpl(moudle, pub);
//			cipher.init(Cipher.DECRYPT_MODE, publickKey);
//
//			// System.out.println(new String(b));
//			while ((bufl = inputReader.read(buf)) != -1) {
//				byte[] encText = null;
//				byte[] newArr = null;
//				if (buf.length == bufl) {
//					newArr = buf;
//				} else {
//					newArr = new byte[bufl];
//					for (int i = 0; i < bufl; i++) {
//						newArr[i] = buf[i];
//					}
//				}
//
//				encText = cipher.doFinal(newArr);
//				result.append(new String(encText, "UTF-8"));
//			}
//			log.info("Decrypt File :: SUCCESS.");
//			// FileOutputStream fos = new FileOutputStream("C://hh.txt");
//			// fos.write(result.getBytes());
//			// fos.flush();
//			// fos.close();
//			// System.out.println(result);
//		} catch (NoSuchAlgorithmException e) {
//			// e.printStackTrace();
//			log.error("Decrypt File:: no such algorithm.");
//		} catch (NoSuchPaddingException e) {
//			// e.printStackTrace();
//			log.error("Decrypt File:: no such padding.");
//		} catch (Exception e) {
//			// e.printStackTrace();
//			log.error("Decrypt File Error");
//		} finally {
//			try {
//				if (inputReader != null) {
//					inputReader.close();
//				}
//				// System.out.println("Finish decryption.");
//			} catch (Exception e) {
//				log.error("Decrypt File::Exception when exec decryptFile");
//				// Logger.servErr("Exception when exec decryptFile with "+srcFileName+",\n"+publicKeyPath);
//				// e.printStackTrace();
//			}
//		}
//		return result.toString();
//	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("This is File Cryption.");
		if (args.length != 1) {
			System.err.println("Usage: only file.{xml,cyt}");
			System.err.println("    Converter requires exactly one filename argument");
			System.exit(1);
		}
		
		String baseName = null;
		String filename = args[0];
		baseName = filename.substring(0, filename.length() - 4);
		
		if (filename.endsWith(".xml")) {
			// Encrypt xml file and output a cyt file
			String sourceName = filename;
			filename = baseName + ".cyt";
			System.out.println("Opening " + filename + " for encryption output");
			System.out.println("Encrypt xml file and Writing the cyt file");
			CryptFile.saveRSAKey("c:\\");
			try {
				CryptFile.encryptFile(sourceName, filename, "c:\\RSAPrivateKey.xml");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			if (filename.endsWith(".cyt")) {
				// Decrypt cyt file and output a xml file
				String sourceName = filename;
				filename = baseName + "1.xml";
				System.out.println("Opening " + filename + " for decryption output");
				// Decrypt xml file
				System.out.println("Decrypt cyt file and Writing the xml file");
				try {
					System.out.println(CryptFile.decryptFile(sourceName, "c:\\RSAPublicKey.xml"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Finished writing the xml file");
				// output.close();
			} else {
				System.err.println("Usage: only file.{xml,cyt}");
				System.exit(1);
			}
	}
}

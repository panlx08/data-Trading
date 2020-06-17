package hfut.hu.BlockValueShare.util;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import com.google.common.base.Strings;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.crypto.symmetric.DES;

import hfut.hu.BlockValueShare.dh.StreamUtil;


public class DeEnCoderHutoolUtil {
	//构建RSA对象
	private static RSA rsa = new RSA();
	//获得私钥
	private static PrivateKey privateKey = rsa.getPrivateKey();
	//获得公钥
	private static PublicKey publicKey = rsa.getPublicKey();
	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static void setPrivateKey(PrivateKey privateKey) {
		DeEnCoderHutoolUtil.privateKey = privateKey;
	}

	public static PublicKey getPublicKey() {
		return publicKey;
	}

	public static void setPublicKey(PublicKey publicKey) {
		DeEnCoderHutoolUtil.publicKey = publicKey;
	}

	/**
	 * function RSA加密通用方法：对称加密解密
	 * @param originalContene : 明文
	 * @return 密文
	 */
	public static String rsaEncrypt(String originalContent) {
		if (Strings.isNullOrEmpty(originalContent)) {
			//明文或加密密钥为空时
			return null;
		}
		//公钥加密，之后私钥解密
		return rsa.encryptStr(originalContent, KeyType.PublicKey);
	}
	
	/**
	 * function RSA解密通用方法：对称加密解密
	 * @param originalContene : 密文
	 * @return 明文
	 */
	public static String rsaDecrypt(String ciphertext) {
		if (Strings.isNullOrEmpty(ciphertext)) {
			return null;
		}
		return rsa.decryptStr(ciphertext, KeyType.PrivateKey);
	}
	
	public static void main(String[] args) throws IOException {
		String original = StreamUtil.readFromFile("B/BPublicKey");
		System.out.println(original);

		@SuppressWarnings("unused")
		KeyPair pair = SecureUtil.generateKeyPair("RSA");
		String a = DeEnCoderHutoolUtil.rsaEncrypt(original);
		System.out.println(a);
		
		String ciphertext = DeEnCoderHutoolUtil.rsaEncrypt(original);
		String b = DeEnCoderHutoolUtil.rsaDecrypt(ciphertext);
		System.out.println(b);
		
	}
	
	/**
	 * function DES加密通用方法：对称加密解密
	 * @param originalContene : 明文
	 * @param key 加密密钥
	 * @return 密文
	 */
	public static String desEncrypt(String originalContent,String key) {
		if (Strings.isNullOrEmpty(originalContent)  ||  
				Strings.isNullOrEmpty(key)) {
			return null;
		}
		//可随机生成密钥
		//byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).
		//	getEncoded();	
		//构建
		DES des = SecureUtil.des(key.getBytes());
		//加密
		return des.encryptHex(originalContent);
	}
	/**
	 * function DES解密通用方法：对称加密解密
	 * @param originalContene : 密文
	 * @param key DES解密密钥（同加密密钥）
	 * @return 明文
	 */
	public static String desDecrypt(String ciphertext,String key) {
		if (Strings.isNullOrEmpty(ciphertext) ||
				Strings.isNullOrEmpty(key)) {
			return null;
		}
		
		//可随机生成密钥
				//byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).
				//	getEncoded();
		//构建
		DES des = SecureUtil.des(key.getBytes());
		
		//加密
		return des.decryptStr(ciphertext);
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
}

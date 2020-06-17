package hfut.hu.BlockValueShare.util;

import java.io.File;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.PublicKeySign;
import com.google.crypto.tink.PublicKeyVerify;

import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import com.google.crypto.tink.integration.gcpkms.GcpKmsClient;
import com.google.crypto.tink.proto.KeyTemplate;
import com.google.crypto.tink.signature.PublicKeySignFactory;
import com.google.crypto.tink.signature.PublicKeyVerifyFactory;
import com.google.crypto.tink.signature.SignatureKeyTemplates;
/*
 * 
 * *AEAD工具嘞
 */
public class createKeySet {
	
	public void register(){
		//仅使用AEAD原语实现
		try {
			AeadConfig.register();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void aead(byte[] plaintext, byte[] aad) {
		try {
			//生成密钥集
			KeysetHandle keysetHandle =KeysetHandle.generateNew(
					AeadKeyTemplates.AES128_GCM);
			//使用key获取所选原语的实例
			Aead aead = AeadFactory.getPrimitive(keysetHandle);
			
			//使用原语完成加密任务
			byte[] ciphertext = aead.encrypt(plaintext, aad);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createKeySet() {
		try {
			KeyTemplate keyTemplate = AeadKeyTemplates.AES128_GCM;
			KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save2File() {
		try {
			//创建AES对应的keysettHandle
			KeysetHandle keysetHandle = KeysetHandle.generateNew(
					AeadKeyTemplates.AES128_GCM);
					
			//写入json文件
			String keysetFilename ="my_keyset.json";
			CleartextKeysetHandle.write(keysetHandle,
					JsonKeysetWriter.withFile(new File(keysetFilename)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save2FileBaseKMS() {
		try {
			//创建AES对应的keysettHandle
			KeysetHandle keysetHandle = KeysetHandle.generateNew(
					AeadKeyTemplates.AES128_GCM);
					
			//写入json文件
			String keysetFilename ="my_keyset.json";
			//使用gcp-kms对密钥加密
			String masterKeyUri = "gcp-kms://project/tink-examples/locations/global/keyRings/foo/cryptoKeys/ba";
			keysetHandle.write(JsonKeysetWriter.withFile(new File(keysetFilename)),
					new GcpKmsClient().getAead(masterKeyUri));
			CleartextKeysetHandle.write(keysetHandle,
					JsonKeysetWriter.withFile(new File(keysetFilename)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadKeySet() {
		try {

			String keysetFilename ="my_keyset.json";
			//使用aws-kms对密钥加密
			String masterKeyUri = "aws-kms://arn:aws:kms:us-east-1:007084425826:key/84a65985-f868-fbfc-83c2-366618acf147";

			KeysetHandle keysetHandle = KeysetHandle.read(JsonKeysetReader.withFile(new File(keysetFilename)),
					 new AwsKmsClient().getAead(masterKeyUri));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadCleartextKetSet() {
		try {
			String keysetFilename ="my_keyset.json";
			KeysetHandle keysetHandle =CleartextKeysetHandle.read(
					JsonKeysetReader.withFile(new File(keysetFilename)));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void aeadAES(byte[] plaintext, byte[] aad) {
		//获取和使用AEAD11
		try {
			//创建AES对应的keysetHandle
			KeysetHandle keysetHandle =KeysetHandle.generateNew(
					AeadKeyTemplates.AES128_GCM);
			
			//使用私钥
			Aead aead = AeadFactory.getPrimitive(keysetHandle);
			
			//获取私钥加密明文
			byte[] ciphertext = aead.encrypt(plaintext, aad);
					
			//解密密文
			byte[] decrypted = aead.decrypt(ciphertext, aad);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 数字签名
	 */
	public void  signatures(byte[] data) {
		try {
			//签名
			//创建ESCSA对应的keysetHandle
			KeysetHandle privateKeysetHandle =KeysetHandle.generateNew(
					SignatureKeyTemplates.ECDSA_P256);
			
			//使用私钥
			PublicKeySign signer = PublicKeySignFactory.getPrimitive(
					privateKeysetHandle);
			
			//获取私钥签名
			byte[] signature= signer.sign(data);
					
			//签名验证
			//获取公钥对应keysetHandle
			KeysetHandle publicKeysetHandle =
					privateKeysetHandle.getPublicKeysetHandle();
			
			//获取私钥
			PublicKeyVerify verifier = PublicKeyVerifyFactory.getPrimitive(
					publicKeysetHandle);
			
			//使用私钥校验签名
			verifier.verify(signature, data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

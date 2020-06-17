package hfut.hu.BlockValueShare.dh;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/*
 * *密钥对
 * *节点对称密钥DES加密数据，存入区块链，
 * *用户发送公钥给节点，节点将自己的对称密钥用公钥加密后，发送给用户
 * *用户引用自己的私钥解密，获得数据加密密钥
 * 
 */
public class KeySuitVO {
	private String symmetricKey; //DES对称密钥
	private String privateKey;//RSA私钥
	private String publicKey;//RSA公钥
	private String secret;//对称密钥
	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}
	private Map<String, Object> keyPair;//生成密钥对

	public KeySuitVO() throws Exception {
		this.keyPair = RSAProvider.generateKeyPair();
		this.symmetricKey = DESProvider.generateKey();
		this.privateKey = RSAProvider.getPrivateKeyBytes(keyPair);
		this.publicKey = RSAProvider.getPublicKeyBytes(keyPair);
		
		StreamUtil.printToFile("B/SymmetricKey", symmetricKey);
    	StreamUtil.printToFile("B/BPublicKey", publicKey);
		StreamUtil.printToFile("B/BPrivateKey", privateKey);
	}
	
	
	public Map<String, Object> getKeyPair() throws Exception {
		Map<String, Object> keyPair = RSAProvider.generateKeyPair();
		return keyPair;
	}
	
	public void setKeyPair(Map<String, Object> keyPair) {
		this.keyPair = keyPair;
	}
	
	public String getSymmetricKey() throws Exception {
		return symmetricKey;
	}
	public void setSymmetricKey(String symmetricKey) {
		this.symmetricKey = symmetricKey;
	}
	
	public String getPrivateKey() throws Exception {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public String getPublicKey() throws Exception {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
}

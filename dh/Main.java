package hfut.hu.BlockValueShare.dh;

import java.util.Map;

public class Main {
	public static void main(String[] args) throws Exception {
		//对称密钥
		String symmetricKey = DESProvider.generateKey();
		StreamUtil.printToFile("A/SymmetricKey", symmetricKey);
		
		//公钥
    	Map<String, Object> keyPair = RSAProvider.generateKeyPair();//生成密钥对
    	String pubkey = RSAProvider.getPublicKeyBytes(keyPair);
		StreamUtil.printToFile("A/BPublicKey", pubkey);
    	
		//私钥
    	String prikey = RSAProvider.getPrivateKeyBytes(keyPair);
		StreamUtil.printToFile("B/BPrivateKey", prikey);
		
		//用公钥a/b对对称密钥secretkey加密
		String secretKey = StreamUtil.readFromFile("A/SymmetricKey");
    	byte[] ctext = RSAProvider.encryptPublicKey(secretKey,pubkey);
    	String text = StreamUtil.byteToBase64(ctext);
    	StreamUtil.printToFile("SecretKey", text);
		
		//用私钥b对secretkey解密
    	String text2 = StreamUtil.readFromFile("SecretKey");
    	byte[] ctext2 = StreamUtil.base64ToByte(text2);
    	String ptext = RSAProvider.decryptPrivateKey(ctext2, prikey);
    	
    	//验证֤
    	//System.out.println("公钥"+pubkey);
    	//System.out.println("私钥"+prikey);
    	System.out.println("原文"+symmetricKey);
    	System.out.println("解密"+text);
    	
	}
}

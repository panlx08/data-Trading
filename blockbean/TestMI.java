package hfut.hu.BlockValueShare.blockbean;

import hfut.hu.BlockValueShare.dh.DESProvider;
import hfut.hu.BlockValueShare.dh.KeySuitVO;
import hfut.hu.BlockValueShare.dh.RSAProvider;
import hfut.hu.BlockValueShare.dh.StreamUtil;

public class TestMI {

	public static void main(String[] args) throws Exception {
		KeySuitVO keySuitVO =new KeySuitVO();

		//用公钥a/b对对称密钥secretkey加密
		String secretKey = keySuitVO.getSymmetricKey();
		byte[] ctext = RSAProvider.encryptPublicKey(secretKey,keySuitVO.getPublicKey());
		String text = StreamUtil.byteToBase64(ctext);    	
		StreamUtil.printToFile("SecretKey", text);
				
		//用私钥b对secretkey解密
		String text2 = StreamUtil.readFromFile("SecretKey");
		byte[] ctext2 = StreamUtil.base64ToByte(text2);
		String ptext = RSAProvider.decryptPrivateKey(ctext2, keySuitVO.getPrivateKey());
		    	
		String originalString = "helloWorld";
		byte[] sec = DESProvider.encrypt(originalString, secretKey);
		String get = DESProvider.decrypt(sec, ptext);
//		    	//验证֤
//
	    	System.out.println("原文"+originalString);
		  	System.out.println("解密"+get);
	}

}

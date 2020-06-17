package hfut.hu.BlockValueShare.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FileRsa {
	public void encryptByRSA(String fileName, String saveFileName, String keyFileName) throws Exception {
		long start = System.currentTimeMillis();
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom();
			keygen.init(random);
			SecretKey key = keygen.generateKey();
			String[] result = readKeyUtil(new File(keyFileName));
			RSAPublicKey key2 = getPublicKey(result[0],result[1]);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.WRAP_MODE, key2);
			byte[] wrappedKey = cipher.wrap(key);
			DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFileName));
			out.writeInt(wrappedKey.length);
			out.write(wrappedKey);
			InputStream in = new FileInputStream(fileName);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			crypt(in, out, cipher);
			in.close();
			out.close();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("�ܹ���ʱ��"+(end-start));
	}
	public void decryptByRSA(String fileName, String saveFileName, String keyFileName) throws Exception {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(fileName));
			int length = in.readInt();
			byte[] wrappedKey = new byte[length];
			in.read(wrappedKey, 0, length);
			String[] result = readKeyUtil(new File(keyFileName));
			RSAPrivateKey key2 = getPrivateKey(result[0],result[1]);
		
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.UNWRAP_MODE, key2);
			Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
			
			OutputStream out = new FileOutputStream(saveFileName);
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			crypt(in, out, cipher);
			in.close();
			out.close();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//�����ݿ����
	public void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException {
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(blockSize);
		byte[] inBytes = new byte[blockSize];
		byte[] outBytes = new byte[outputSize];
		
		int inLength = 0;
		boolean next = true;
		while (next) {
			inLength = in.read(inBytes);
			System.out.println(inLength);
			if (inLength == blockSize) {
				int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
				out.write(outBytes, 0, outLength);
			} else {
				next = false;
			}
		}
		if (inLength > 0) {
			outBytes = cipher.doFinal(inBytes, 0, inLength);
		} else {
			outBytes = cipher.doFinal();
		}
		out.write(outBytes);
	}
	
	//����RSA��Կ��
	public void generateRSAKey(String savePath) throws Exception {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			keygen.initialize(RSA_KEYSIZE, random);
			KeyPair keyPair = keygen.generateKeyPair();
			RSAPublicKey puk=(RSAPublicKey) keyPair.getPublic();
			createXmlFile(puk.getModulus().toString(), puk.getPublicExponent().toString(),savePath + "\\public.xml");
			RSAPrivateKey prk=(RSAPrivateKey) keyPair.getPrivate();
			createXmlFile(prk.getModulus().toString(), prk.getPrivateExponent().toString(),savePath + "\\private.xml");
			/*OutputStreamWriter osw  = new OutputStreamWriter(new FileOutputStream(savePath + "\\public.xml"));
			// �õ���Կ�ַ��� 
			System.out.println(puk.getModulus());
			System.out.println(puk.getPublicExponent());
			System.out.println(prk.getModulus());
			System.out.println(prk.getPrivateExponent());
	        // �õ�˽Կ�ַ��� 
			osw.write(createXmlFile(puk.getModulus().toString(), puk.getPublicExponent().toString()));
			osw.close();
			osw = new OutputStreamWriter(new FileOutputStream(savePath + "\\private.xml"));
			osw.write(createXmlFile(prk.getModulus().toString(), prk.getPrivateExponent().toString()));
			osw.close();*/
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	   /**
	  * ʹ��ģ��ָ������RSA��Կ
	  * ע�⣺���˴�������Ĭ�ϲ�λ��ʽ��ΪRSA/None/PKCS1Padding����ͬJDKĬ�ϵĲ�λ��ʽ���ܲ�ͬ����AndroidĬ����RSA
	  * /None/NoPadding��
	  * 
	  * @param modulus
	  *            ģ
	  * @param exponent
	  *            ָ��
	  * @return
	  */
	 public static RSAPublicKey getPublicKey(String modulus, String exponent) {
	     try {
	         BigInteger b1 = new BigInteger(modulus);
	         BigInteger b2 = new BigInteger(exponent);
	         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	         RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
	         return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	     }
	 }
	
	 /**
	  * ʹ��ģ��ָ������RSA˽Կ
	  * ע�⣺���˴�������Ĭ�ϲ�λ��ʽ��ΪRSA/None/PKCS1Padding����ͬJDKĬ�ϵĲ�λ��ʽ���ܲ�ͬ����AndroidĬ����RSA
	  * /None/NoPadding��
	  * 
	  * @param modulus
	  *            ģ
	  * @param exponent
	  *            ָ��
	  * @return
	  */
	 public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
	     try {
	         BigInteger b1 = new BigInteger(modulus);
	         BigInteger b2 = new BigInteger(exponent);
	         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	         RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
	         return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	     }
	 }
	
	/**
	 * ����xml�ַ���
	 * @return
	 * @throws Exception 
	 */
	public static void createXmlFile(String Modulus,String Exponent,String filepath) throws Exception{
		 Document document = DocumentHelper.createDocument();
	     Element root = document.addElement( "root" );
	     Element supercarElement= root.addElement("Modulus");
	
	     supercarElement.addText(Modulus);
	     Element supercarElement2= root.addElement("Exponent");
	
	     supercarElement2.addText(Exponent);
	     // д�뵽һ���µ��ļ��� 
	     writer(document,filepath);
	}
	/** 
	 * ��document����д���µ��ļ� 
	 *  
	 * @param document 
	 * @throws Exception 
	 */  
	public static void writer(Document document,String path) throws Exception {  
	    // ���յĸ�ʽ  
	    // �Ű������ĸ�ʽ  
	    OutputFormat format = OutputFormat.createPrettyPrint();  
	    // ���ñ���  
	    format.setEncoding("UTF-8");  
	    // ����XMLWriter����,ָ����д���ļ��������ʽ  
	    XMLWriter writer = new XMLWriter(new OutputStreamWriter(  
	            new FileOutputStream(new File(path)), "UTF-8"), format);  
	    // д��  
	    writer.write(document);  
	    // ����д��  
	    writer.flush();  
	    // �رղ���  
	    writer.close();  
	}
	
	public String[] readKeyUtil(File file) throws DocumentException{
		 // ����saxReader����  
	    SAXReader reader = new SAXReader();  
	    // ͨ��read������ȡһ���ļ� ת����Document����  
	    Document document = reader.read(file);  
	    //��ȡ���ڵ�Ԫ�ض���  
	    Element node = document.getRootElement();  
	    Element ele = node.element("Modulus");
	    String Modulus = ele.getTextTrim();
	    Element ele1 = node.element("Exponent");
	    String Exponent = ele1.getTextTrim();
		return new String[]{Modulus,Exponent};
	}
	
	private static final int RSA_KEYSIZE = 1024;
	public static void main(String[] args) throws Exception {
		//����xml��ʽ�Ĺ�Կ��˽Կ
		new FileRsa().generateRSAKey("G://ZJY//1//");
		//���ļ����м���
		String path = "G://ZJY//get//";
		File file = new File("G://ZJY//12345.txt");
		String filename= file.getName();
		String getfile = path +filename;
		new FileRsa().encryptByRSA("G://ZJY//12345.txt", getfile, "G:/ZJY/1/public.xml");
		//���ļ����н���
		new FileRsa().decryptByRSA("G://ZJY//get//12345.txt", "G://ZJY//1234567.txt", "G:/ZJY/1/private.xml");
		
	}
}
package hfut.hu.BlockValueShare.dh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * XStream工具类，用于xml与对象之间的转换
 * <br>Object fromXML(String xml)  将xml转换成对象
 * <br>String toXML(Object obj) 将对象转换成xml字段串
 * 
 */
public class StreamUtil {

	private static XStream xstream = new XStream(new DomDriver());//X��
	private static PrintStream ps;
	private static BufferedReader in;
	
	/**
	 * 字符串转base64
	 * @param s 原始字段
	 * @return String base64
	 */
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		try {
			return (new BASE64Encoder()).encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * base64还原成字符串
	 * @param s base64串
	 * @return String 原始串
	 */
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * �ֽ�����תbase64
	 * @param bs �ֽ�����
	 * @return String base64��
	 */
	public static String byteToBase64(byte[] bs) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bs);
	}

	/**
	 * base64 转字节数组
	 * @param base64 base64串
	 * @return byte 原始字节数组
	 */
	public static byte[] base64ToByte(String base64) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(base64);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *将XML准换成对象
	 * @param String xml
	 * @return Object
	 */
	public static Object fromXML(String xml) {
		return xstream.fromXML(xml);
	}
	
	/**
	 * 将对象转换成xml字段串
	 * @param Object obj
	 * @return
	 */
	public static String toXML(Object obj) {
		String xml = xstream.toXML(obj);
		String a = xml.replaceAll("\n", "");//去掉换行
		String s = a.replaceAll("\r", "");
		return s;
	}
	
	/**
	 * ָ指定文件名写数据
	 * @param String file
	 * @param String data
	 * @throws IOException 
	 */
	public static void printToFile(String file,String data) throws IOException {
		//不存在则创建
		File temp = new File(file);
		File tempParent = temp.getParentFile();
		if(tempParent!=null) {
			if(!tempParent.exists()) {
			    tempParent.mkdirs();
				temp.createNewFile();
			}
		}
		FileOutputStream fos = new FileOutputStream(file);
		ps = new PrintStream(fos);
		ps.print(data);
		ps.close();
	}
	
	/**
	 *指定文件名读数据
	 * @param String file
	 * @throws IOException 
	 */
	public static String readFromFile(String file) throws IOException {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String s = "";
		String temp = null;
		while((temp = in.readLine())!=null) {
			s += temp;
		}
		in.close();
		return s;
	}
}

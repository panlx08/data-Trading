package hfut.hu.BlockValueShare;

import java.io.FileWriter;
import java.net.InetAddress;


public class Register {
	/*
	 **获取本地IP，并写入文件
	 */
	public static void getIpAddr()  throws Exception{
		InetAddress addr = InetAddress.getLocalHost();
		String ipAdr = addr.getHostAddress();
		String fileName ="peers.list";
		FileWriter fileWritter =new FileWriter(fileName,true);
		fileWritter.write(ipAdr+ ":8015"+"\r\n");
		fileWritter.close();
//		System.out.print("Done");
	}
	public static void main(String[] args) throws Exception {
		getIpAddr();
	}
}

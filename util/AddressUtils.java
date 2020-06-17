package hfut.hu.BlockValueShare.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ip地址工具类
 * @author ACGkaka
 *
 */
public class AddressUtils {

/**
* 获取本机的内网ip地址
* @return
* @throws SocketException
*/
public String getInnetIp() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		Enumeration<NetworkInterface> netInterfaces;
		netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress()
				&& !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
				finded = true;
					break;
			} else if (ip.isSiteLocalAddress()
		&& !ip.isLoopbackAddress()
				&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
			localip = ip.getHostAddress();
		}
			}
		}
		
	
			return localip;

	}


/**
* 获取本机的外网ip地址
* @return
*/
public String getV4IP(){
String ip = "";
String chinaz = "http://ip.chinaz.com";

StringBuilder inputLine = new StringBuilder();
String read = "";
URL url = null;
HttpURLConnection urlConnection = null;
BufferedReader in = null;
try {
url = new URL(chinaz);
urlConnection = (HttpURLConnection) url.openConnection();
in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
while((read=in.readLine())!=null){
inputLine.append(read+"\r\n");
}
//System.out.println(inputLine.toString());
} catch (MalformedURLException e) {
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}finally{
if(in!=null){
try {
in.close();
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
}
Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
Matcher m = p.matcher(inputLine.toString());
if(m.find()){
String ipstr = m.group(1);
ip = ipstr;
//System.out.println(ipstr);
}
return ip;
}


/**
* 解析ip地址
* 
* 设置访问地址为http://ip.taobao.com/service/getIpInfo.php
* 设置请求参数为ip=[已经获得的ip地址]
* 设置解码方式为UTF-8
* 
* @param content  请求的参数 格式为：ip=192.168.1.101
* @param encoding 服务器端请求编码。如GBK,UTF-8等
* @return
* @throws UnsupportedEncodingException
*/
public String getAddresses(String content, String encoding) throws UnsupportedEncodingException {
//设置访问地址
String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
// 从http://whois.pconline.com.cn取得IP所在的省市区信息
String returnStr = this.getResult(urlStr, content, encoding);
if (returnStr != null) {
// 处理返回的省市区信息
// System.out.println(returnStr);
String[] temp = returnStr.split(",");
if (temp.length < 3) {
return "0";// 无效IP，局域网测试
}

String country = ""; //国家
String area = ""; //地区
String region = ""; //省份
String city = ""; //市区
String county = ""; //地区
String isp = ""; //ISP公司
for (int i = 0; i < temp.length; i++) {
switch (i) {
case 2:
country = (temp[i].split(":"))[1].replaceAll("\"", "");
country = URLDecoder.decode(country, encoding);// 国家
break;
case 3:
area = (temp[i].split(":"))[1].replaceAll("\"", "");
area = URLDecoder.decode(area, encoding);// 地区
break;
case 4:
region = (temp[i].split(":"))[1].replaceAll("\"", "");
region = URLDecoder.decode(region, encoding);// 省份
break;
case 5:
city = (temp[i].split(":"))[1].replaceAll("\"", "");
city = URLDecoder.decode(city, encoding);// 市区
if("内网IP".equals(city)) {
return "地址为：内网IP";
}
break;
case 6:
county = (temp[i].split(":"))[1].replaceAll("\"", "");
county = URLDecoder.decode(county, encoding);// 地区
break;
case 7:
isp = (temp[i].split(":"))[1].replaceAll("\"", "");
isp = URLDecoder.decode(isp, encoding); // ISP公司
break;
}
}


return new StringBuffer("地址为："+country+",").append(region+"省,").append(city+"市,").append(county+",").append("ISP公司："+isp)
.toString();
}
return null;
}


/**
* 访问目标地址并获取返回值
* @param urlStr 请求的地址
* @param content 请求的参数 格式为：ip=192.168.1.101
* @param encoding 服务器端请求编码。如GBK,UTF-8等
* @return
*/
private String getResult(String urlStr, String content, String encoding) {
URL url = null;
HttpURLConnection connection = null;
try {
url = new URL(urlStr);
connection = (HttpURLConnection) url.openConnection();// 新建连接实例
connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
connection.setReadTimeout(33000);// 设置读取数据超时时间，单位毫秒
connection.setDoOutput(true);// 是否打开输出流 true|false
connection.setDoInput(true);// 是否打开输入流true|false
connection.setRequestMethod("POST");// 提交方法POST|GET
connection.setUseCaches(false);// 是否缓存true|false
connection.connect();// 打开连接端口
DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
out.flush();// 刷新
out.close();// 关闭输出流
BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
// ,以BufferedReader流来读取
StringBuffer buffer = new StringBuffer();
String line = "";
while ((line = reader.readLine()) != null) {
buffer.append(line);
}
reader.close();
return buffer.toString();
} catch (IOException e) {
e.printStackTrace();
} finally {
if (connection != null) {
connection.disconnect();// 关闭连接
}
}
return null;
}


/**
* 测试方法
* 获取本机的内网ip，外网ip和指定ip的地址
* @param args
*/
public static void main(String[] args) {
AddressUtils addressUtils = new AddressUtils();
//step1.获得内网ip和外网ip，并输出到控制台
String ip1="";
try {
ip1 = addressUtils.getInnetIp(); //局域网的ip地址，比如：192.168.1.101
} catch (SocketException e1) {
e1.printStackTrace();
}
System.out.println("内网ip:"+ip1);
String ip2 = addressUtils.getV4IP(); //用于实际判断地址的ip
System.out.println("外网ip:"+ip2);
//step2.根据外网ip地址，得到市级地理位置
String address = "";
try {
address = addressUtils.getAddresses("ip=" + ip2, "utf-8");
} catch (UnsupportedEncodingException e) {
e.printStackTrace();
}
// 输出地址，比如：中国，山东省，济南市，联通
System.out.println("您的"+address);
System.out.println("******************************");
System.out.println("请输入想要查询的ip地址(输入exit退出)：");
Scanner scan=new Scanner(System.in);
String ip="";
while(!"exit".equals(ip=scan.next())) {
try {
address = addressUtils.getAddresses("ip=" + ip, "utf-8");
} catch (UnsupportedEncodingException e) {
e.printStackTrace();
}
// 输出地址，比如：中国，山东省，济南市，联通
System.out.println(ip+"的"+address);
System.out.println("******************************");
System.out.println("请输入想要查询的ip地址(输入exit退出)：");
}
scan.close();
System.out.println("再见");
}

}
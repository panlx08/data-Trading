package hfut.hu.BlockValueShare.util;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
/*
 * IP获取
 */
public class IpUtil {
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 ||
				"unknown".equalsIgnoreCase(ipAddress)){
					ipAddress = request.getHeader("Proxy-Client-IP");				
			}
			if (ipAddress == null || ipAddress.length() == 0 ||
					"unknown".equalsIgnoreCase(ipAddress)){
						ipAddress = request.getHeader("WL-Proxy-Client-IP");	
				}
			if (ipAddress == null || ipAddress.length() == 0 ||
					"unknown".equalsIgnoreCase(ipAddress)){
						ipAddress = request.getRemoteAddr();
						if (ipAddress.equals("127.0.0.1")) {
							//根据网卡选区本机配置的IP
							InetAddress inet = null;
							try {
								inet = InetAddress.getLocalHost();
							} catch (Exception e) {
								e.printStackTrace();
							}
							ipAddress = inet.getHostAddress();
						}
				}
			
			if (ipAddress != null && ipAddress.length() > 15){
				// 多个代理取第一个
				if (ipAddress.indexOf(",") > 0){
							ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
					}
				}
		} catch (Exception e) {
			ipAddress = "";
		}
		return ipAddress;
	}
}

package com.fcc.commons.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.fcc.commons.http.client.methods.Response;
import com.fcc.commons.http.client.methods.impl.HttpGet;
import com.fcc.commons.socket.SocketUtil;

/**
 * IP 工具类
 * @author 傅泉明
 *
 * 2010-11-22
 */
public class IpUtil {
	
	/** 查询本地外网IP、所在地区，请求的IP地址 */ 
	private static Pattern ipAddrPattern = Pattern.compile("(.*)<iframe src=\"(.*)\"(.*)", Pattern.CASE_INSENSITIVE);
	/** 查询本地外网IP、所在地区 */
	private static Pattern ipPattern = Pattern.compile("(.*)您的IP是：\\[(.*)\\] 来自：(.*)</center.*", Pattern.CASE_INSENSITIVE);
	
	/** 查询域名IP、所在地区 */
	private static Pattern[] domainNameIpPattern = new Pattern[]{
		Pattern.compile("(.*)<font color=\"blue\">.*>> (.*)</font>(.*)", Pattern.CASE_INSENSITIVE),
		Pattern.compile("(.*)本站.*：(.*)</li><li>参考(.*)", Pattern.CASE_INSENSITIVE)
	};
	/**
	 * 获取本机外网IP、地区
	 * @return
	 */
	public static String[] getExtranetIpAndArea() {
		// 获取ip138，请求的ip地址
		HttpGet ipAddrRequest = new HttpGet("http://ip138.com");
		ipAddrRequest.setResponseCharset("gb2312");
		String ipAddr = null;
		BufferedReader br = null;
		try {
			Response response = ipAddrRequest.execute();
			br = new BufferedReader(new InputStreamReader(response.getInputStream(), ipAddrRequest.getResponseCharset()));
			String str = null;
			while ((str = br.readLine()) != null) {
				// 获取指定的内容
				Matcher m = ipAddrPattern.matcher(str);
				if (m.find()) {
					ipAddr = m.group(2);
					ipAddr = ipAddr.substring(0, ipAddr.indexOf("\""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		ipAddrRequest.close();
		
		if (ipAddr == null) return null;
		
		// 获取ip地址
		HttpGet request = new HttpGet(ipAddr);
		request.setResponseCharset("gb2312");
		try {
			Response response = request.execute();
//			if (response.getStatusLine().contains(String.valueOf(HttpURLConnection.HTTP_OK))) {
				br = new BufferedReader(new InputStreamReader(response.getInputStream(), request.getResponseCharset()));
				String str = null;
				while ((str = br.readLine()) != null) {
					// 获取指定的内容
					Matcher m = ipPattern.matcher(str);
					if (m.find()) {
						String[] ips = new String[2];
						ips[0] = m.group(2);
						ips[1] = m.group(3);
						return ips;
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		request.close();
		return null;
	}
	/**
	 * 获取本机局域网IP
	 * @return
	 */
	public static String getLocalHostIp() {
		String ip = null;
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
	/**
	 * 取得域名的IP
	 * @param domainName	域名：www.g.cn
	 * @return
	 */
	public static String[] getDomainNameIp(String domainName) {
		InetAddress[] addresses = SocketUtil.getInetAddressByDomainName(domainName);
		int length = addresses.length;
		String[] ips = new String[length];
		for (int i = 0; i < length; i++) {
			ips[i] = addresses[i].getHostAddress();
		}
		return ips;
	}
	/**
	 * 取得域名的IP、地区		通过网站搜索
	 * @param domainName	域名：www.g.cn
	 * @return string[]	[0]:IP，[1]:地区
	 */
	public static String[] getDomainNameIpAndArea(String domainName) {
		HttpGet request = new HttpGet("http://www.ip138.com/ips1388.asp");
		request.getRequestBody().getHttpParams().addParam("action", "2");
		request.getRequestBody().getHttpParams().addParam("ip", domainName);
		request.setRequestCharset("GBK");
		request.setResponseCharset("gb2312");
		BufferedReader br = null;
		try {
			Response response = request.execute();
			String[] ips = new String[2];
			if (response.getStatusLine().contains(String.valueOf(HttpURLConnection.HTTP_OK))) {
				br = new BufferedReader(new InputStreamReader(response.getInputStream(), request.getResponseCharset()));
				String str = null;
				int size = 0;
				while ((str = br.readLine()) != null) {
					// 获取指定的内容
					Pattern p = domainNameIpPattern[size];
					Matcher m = p.matcher(str);
					if (m.find()) {
						if (size == 0) {
							ips[0] = m.group(2);
						} else {
							ips[1] = m.group(2);
							return ips;
						}
						size = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
		request.close();
		return null;
	}
	
	public static void main(String[] args) {
		String[] params = IpUtil.getExtranetIpAndArea();
		System.out.println(java.util.Arrays.toString(params));
		
		System.out.println(java.util.Arrays.toString(IpUtil.getDomainNameIpAndArea("http://www.163.com")));
	}
}

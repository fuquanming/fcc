/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.socket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <p>Title:SocketUtil.java</p>
 * <p>Package:com.fcc.commons.socket</p>
 * <p>Description:Socket工具类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
//@SuppressWarnings("unchecked")
public class SocketUtil {
	
//	private static Logger logger = Logger.getLogger(SocketUtil.class);
	/** URL 的协议名称 */
	public static final String URL_PROTOCOL = "url_protocol";
	/** URL 的主机名 */
	public static final String URL_HOST = "url_host";
	/** URL 的主机名(IP) */
	public static final String URL_IP = "url_ip";
	/** URL 的主机名(域名) */
	public static final String URL_DOMAIN_NAME = "url_domain_name";
	/** URL 的端口号 */
	public static final String URL_PORT = "url_port";
	/** URL 的路径部分（访问的资源） */
	public static final String URL_PATH = "url_path";
	/** URL 的查询部分 */
	public static final String URL_QUERY = "url_query";
	
//	public static Pattern ipPattern = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
	
	public static Pattern ipPattern = Pattern.compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");
	
	/******************* 以下是建立socket方法 ***********************/
	/**
	 * 通过域名取得 互联网协议 (IP) 地址
	 * @param domainName
	 * @return
	 */
	public static InetAddress[] getInetAddressByDomainName(String domainName) {
		InetAddress[] address = null;
		if (domainName != null && !"".equals(domainName)) {
			try {
				address = InetAddress.getAllByName(domainName);
				return address;
			} catch (UnknownHostException e) {
//				logger.error(e);
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 通过IP取得 互联网协议 (IP) 地址
	 * @param ip
	 * @return
	 */
	public static InetAddress getInetAddressByIp(String ip) {
		InetAddress address = null;
		if (ip != null && !"".equals(ip)) {
			String[] ips = ip.split("\\.");
			int length = ips.length;
			byte[] bs = new byte[length];
			for (int i = 0; i < length; i++) {
				bs[i] = (byte)Integer.parseInt(ips[i]);
			}
			try {
				address = InetAddress.getByAddress(bs);
			} catch (UnknownHostException e) {
//				logger.error(e);
				e.printStackTrace();
			}
		}
		return address;
	}
	/**
	 * 通过IP取得 互联网协议 (IP) 地址
	 * @param ip	
	 * @return
	 */
	public static InetAddress getInetAddressByIp(int[] ip) {
		InetAddress address = null;
		if (ip != null) {
			int length = ip.length;
			byte[] bs = new byte[length];
			for (int i = 0; i < length; i++) {
				bs[i] = (byte)ip[i];
			}
			try {
				address = InetAddress.getByAddress(bs);
			} catch (UnknownHostException e) {
//				logger.error(e);
				e.printStackTrace();
			}
		}
		return address;
	}
	/**
	 * 通过互联网协议 (IP) 地址		取得 IP
	 * @param address
	 * @return
	 */
	public static String getIpByInetAddress(InetAddress address) {
		return address.getHostAddress();
	}
	
	/**
	 * 通过URL地址取得 协议、主机名、IP、域名、端口号、请求页面、请求参数
	 * @param urlStr
	 * @return	URL_PROTOCOL、URL_HOST、URL_IP、URL_DOMAIN_NAME、URL_PORT、URL_PATH、URL_QUERY
	 */
	public static Map<String, String> getParams(String urlStr) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			URL url = new URL(urlStr);
			// 协议
			String protocol = url.getProtocol();
			// 主机名
			String host = url.getHost();
			// 端口号
			int port = url.getPort();
			// 路径部分
			String path = url.getPath();
			// 查询部分
			String query = url.getQuery();
			
			if (port == -1) port = 80;
			// 域名
			String domainName = null;
			// IP
			String ip = null;
			
			if (path == null || "".equals(path)) path = "/";
			
			if (query == null) query = "";
			
			// 解析是否是IP地址
			Matcher m = ipPattern.matcher(host);
			
			if (m.find()) {
				ip = host;
				map.put(URL_IP, ip);
			} else {
				domainName = host;
				map.put(URL_DOMAIN_NAME, domainName);
			}
			map.put(URL_PROTOCOL, protocol);
			map.put(URL_HOST, host);
			map.put(URL_PORT, String.valueOf(port));
			map.put(URL_PATH, path);
			map.put(URL_QUERY, query);
		} catch (MalformedURLException e) {
//			logger.error(e);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 取得socket 链接
	 * @param address			互联网协议 (IP) 地址
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByInetAddress(InetAddress address, int port)
			throws Exception {
		return getSocketByInetAddress(address, port, 0, 0);
	}
	
	/**
	 * 取得socket 链接
	 * @param address			互联网协议 (IP) 地址
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByInetAddress(InetAddress address, int port, int requestTimeout)
			throws Exception {
		return getSocketByInetAddress(address, port, requestTimeout, 0);
	}
	
	/**
	 * 取得socket 链接
	 * @param address			互联网协议 (IP) 地址
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByInetAddress(InetAddress address, int port, int requestTimeout, int readTimeout)
			throws Exception {
		Socket socket = null;
		try {
			socket = new Socket();
			InetSocketAddress isa = new InetSocketAddress(address, port);
			if (readTimeout > 0) socket.setSoTimeout(readTimeout);
			if (requestTimeout > 0) {
				socket.connect(isa, requestTimeout);
			} else {
				socket.connect(isa);
			}
			return socket;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 取得socket 链接
	 * @param domainName		域名
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByDomainName(String domainName, int port)
			throws Exception {
		return getSocketByDomainName(domainName, port, 0, 0);
	}
	
	/**
	 * 取得socket 链接
	 * @param domainName		域名
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByDomainName(String domainName, int port, int requestTimeout)
			throws Exception {
		return getSocketByDomainName(domainName, port, requestTimeout, 0);
	}
	
	/**
	 * 取得socket 链接
	 * @param domainName		域名
	 * @param port				端口号
	 * @return
	 */
	@SuppressWarnings("unused")
    public static Socket getSocketByDomainName(String domainName, int port, int requestTimeout, int readTimeout)
			throws Exception {
		try {
			InetAddress[] address = getInetAddressByDomainName(domainName);
			int size = address.length;
			for (int i = 0; i < size; i++) {
				return getSocketByInetAddress(address[i], port, requestTimeout, readTimeout);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param ip				ip
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByIp(String ip, int port) 
			throws Exception {
		try {
			InetAddress address = getInetAddressByIp(ip);
			if (address != null) {
				return getSocketByInetAddress(address, port);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param ip				ip
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByIp(String ip, int port, int requestTimeout) 
			throws Exception {
		try {
			InetAddress address = getInetAddressByIp(ip);
			if (address != null) {
				return getSocketByInetAddress(address, port, requestTimeout);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param ip				ip
	 * @param port				端口号
	 * @return
	 */
	public static Socket getSocketByIp(String ip, int port, int requestTimeout, int readTimeout) 
			throws Exception {
		try {
			InetAddress address = getInetAddressByIp(ip);
			if (address != null) {
				return getSocketByInetAddress(address, port, requestTimeout, readTimeout);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param ip 
	 * @param port					端口号
	 * @return
	 */
	public static Socket getSocketByIp(int[] ip, int port) 
			throws Exception {
		try {
			InetAddress address = getInetAddressByIp(ip);
			if (address != null) {
				return getSocketByInetAddress(address, port);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param ip 
	 * @param port					端口号
	 * @return
	 */
	public static Socket getSocketByIp(int[] ip, int port, int timeout) 
			throws Exception {
		try {
			InetAddress address = getInetAddressByIp(ip);
			if (address != null) {
				return getSocketByInetAddress(address, port, timeout);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param urlStr	链接地址
	 * @return
	 */
	public static Socket getSocketByUrl(String urlStr) throws Exception {
		try {
			Map<String, String> map = getParams(urlStr);
			if (map != null) {
				String ip = map.get(URL_IP);
				String domainName = map.get(URL_DOMAIN_NAME);
				if (ip != null) {
					return getSocketByIp(ip, Integer.parseInt(map.get(URL_PORT)));
				}
				if (domainName != null) {
					return getSocketByDomainName(domainName, Integer.parseInt(map.get(URL_PORT)));
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 取得socket 链接
	 * @param urlStr	链接地址
	 * @return
	 */
	public static Socket getSocketByUrl(String urlStr, int requestTimeout) throws Exception {
		try {
			Map<String, String> map = getParams(urlStr);
			if (map != null) {
				String ip = map.get(URL_IP);
				String domainName = map.get(URL_DOMAIN_NAME);
				if (ip != null) {
					return getSocketByIp(ip, Integer.parseInt(map.get(URL_PORT)), requestTimeout);
				}
				if (domainName != null) {
					return getSocketByDomainName(domainName, Integer.parseInt(map.get(URL_PORT)), requestTimeout);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
}

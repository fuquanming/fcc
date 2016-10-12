/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

/**
 * <p>Title:SocketFactory.java</p>
 * <p>Package:com.fcc.commons.socket</p>
 * <p>Description:socket工厂</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class SocketFactory {

	private static Logger logger = Logger.getLogger(SocketFactory.class.getName());
	
	/** 缓存socket */
	private static Map<String, Map<String, SocketCache>> socketCacheMap = new HashMap<String, Map<String, SocketCache>>();
	/** 缓存 每个url 的key */
	private static Map<String, String> urlCacheMap = new HashMap<String, String>();
	
	private static SocketDestroyThread socketThread = null;
	
	/**
	 * 取得 缓存中的socket
	 * @param urlStr		请求的地址
	 * @param period 	空闲时间 单位 ：秒
	 * @return
	 * @throws Exception
	 */
	public static synchronized Socket getSocket(String urlStr) throws Exception {
		if (urlStr == null || "".equals(urlStr)) return null;
		
		if (socketThread == null) {
			// 初始化 启动定时清理 空闲的 socket 线程 轮询周期 5分钟
			socketThread = new SocketFactory().new SocketDestroyThread(300);
			Thread thread = new Thread(socketThread);
			thread.setDaemon(true);
			thread.start();
		}
		
		Map<String, String> map = SocketUtil.getParams(urlStr);
		// 取得 URL 中的IP
		String ip = map.get(SocketUtil.URL_IP);
		// 取得 URL 中的域名
		String domainName = map.get(SocketUtil.URL_DOMAIN_NAME);
		// 取得 互联网协议 (IP) 地址
		InetAddress address = null;
		if (ip != null) {
			address = SocketUtil.getInetAddressByIp(ip);
		}
		if (domainName != null) {
			address = SocketUtil.getInetAddressByDomainName(domainName)[0];
		}
		// TODO 取得实际socket 链接的IP地址
		// TODO urlKey 改变后 结构为 IP + : + 端口号 作为key
		String urlKey = SocketUtil.getIpByInetAddress(address) + ":" + map.get(SocketUtil.URL_PORT);
		
		Socket socket = null;
		// TODO 设置某个URL 中的socket 队列
		Map<String, SocketCache> socketMap = socketCacheMap.get(urlKey);
		if (socketMap == null) {
			// TODO 为初始化过
			socketMap = new HashMap<String, SocketCache>();
			socketCacheMap.put(urlKey, socketMap);
		}
		
		// TODO 初始化过
		String key = null;
		SocketCache socketCache = null;
		synchronized (socketMap) {
			// TODO 销毁 urlStr 对应的 所有socket 使用到
			String temp_key = urlCacheMap.get(urlStr);
			if (temp_key == null) {
				urlCacheMap.put(urlStr, urlKey);
			}
			
			Iterator<String> it = socketMap.keySet().iterator();
			while (it.hasNext()) {
				key = it.next();
				// TODO 未使用
				socketCache = socketMap.get(key);
				// 没有被使用且没有标识被销毁
				if (socketCache.isRunning() == false && socketCache.isDestroyed() == false) {
					socketCache.setTimeSeconds(System.currentTimeMillis());
					socket = socketMap.get(key).getSocket();
					break;
				}
			}
			
			if (key != null && socket != null && socketCache != null) {
				// TODO 找到标识该socket 在使用中
				socketCache.setRunning(true);
			} else {
				// TODO 没有找到 取得新的socket
				socket = SocketUtil.getSocketByInetAddress(address, Integer.valueOf(map.get(SocketUtil.URL_PORT)));
				socketCache = new SocketCache();
				socketCache.setSocket(socket);
				socketCache.setRunning(true);
				// TODO 初始化 销毁 空闲 10 分钟socket
				socketCache.setIdleSeconds(600000);
				socketMap.put(socket.toString(), socketCache);
			}
		}
		return socket;
    }
	/**
	 * 返回socket 
	 * @param urlStr	IP + : + 端口号
	 * @param socket
	 */
	public static void closeSocket(Socket socket) throws Exception {
		if (socket == null) return;
		// TODO urlStr 改变后 结构为 IP + : + 端口号 作为key
		String urlKey = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		// TODO 设置某个URL 中的socket 队列
		Map<String, SocketCache> socketMap = socketCacheMap.get(urlKey);
		if (socketMap != null) {
			String key = socket.toString();
			SocketCache socketCache = socketMap.get(key);
			if (socketCache != null) socketCache.setRunning(false);
		}
    }
	
	/**
	 * 销毁 缓存中的socket
	 * @param urlStr	IP + : + 端口号
	 * @param socket
	 */				   	 
	public static void destroyedSocket(Socket socket) {
		if (socket == null) return;
		// TODO urlStr 改变后 结构为 IP + : + 端口号 作为key
		String urlKey = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		String key = socket.toString();
		IOUtils.closeQuietly(socket);
		// TODO 设置某个URL 中的socket 队列
		Map<String, SocketCache> socketMap = socketCacheMap.get(urlKey);
		synchronized (socketMap) {
			socketMap.remove(key);
		}
	}
	
	/**
	 * 销毁 缓存中的所有socket
	 * @param urlStr	请求的地址
	 */
	public static void destroyedSocket(String urlStr) {
		if (urlStr == null || "".equals(urlStr)) return;
		
		// TODO urlKey 结构为 IP + : + 端口号 作为key
		String urlKey = urlCacheMap.get(urlStr);
		if (urlKey == null) return;
		
		// TODO 设置某个URL 中的socket 队列
		Map<String, SocketCache> socketMap = socketCacheMap.get(urlKey);
		if (socketMap == null) return;
		synchronized (socketMap) {
			Iterator<SocketCache> it = socketMap.values().iterator();
			while (it.hasNext()) {
				IOUtils.closeQuietly(it.next().getSocket());
			}
			socketMap.clear();
			urlCacheMap.remove(urlStr);
		}
		
	}
	
	/**
	 * 销毁空闲超时的socket
	 * @author 傅泉明
	 *
	 * 2010-6-25
	 */
	class SocketDestroyThread implements Runnable {
		
		// 临时变量
		List<Socket> list = new ArrayList<Socket>();
		
		private long period;
		
		private boolean stopping;
		
		private boolean destory;
		
		public SocketDestroyThread(long period) {
			this.period = period * 1000;
		}
		
		public void run() {
			while (stopping == false) {
				if (socketCacheMap != null) {
					synchronized (socketCacheMap) {
						list.clear();
						Iterator<Map<String, SocketCache>> it = socketCacheMap.values().iterator();
						while (it.hasNext()) {
							Map<String, SocketCache> map = it.next();
							Iterator<SocketCache> tempIt = map.values().iterator();
							while (tempIt.hasNext()) {
								SocketCache socketCache = tempIt.next();
								if (socketCache.isRunning() == false) {
									long nowTime = System.currentTimeMillis();
//									System.out.println("SocketDestroyThread.run() " + 
//											" nowTime = " + nowTime + 
//											" TimeSeconds = " + socketCache.getTimeSeconds() +
//											" IdleSeconds = " + socketCache.getIdleSeconds() + 
//											(nowTime - socketCache.getTimeSeconds()) + "\t" + 
//											socketCache.getSocket());
									if ((nowTime - socketCache.getTimeSeconds()) >= socketCache.getIdleSeconds()) {
										socketCache.setDestroyed(true);
										list.add(socketCache.getSocket());
									}
								}
							}
						}
					}
					int size = list.size();
					for (int i = 0; i < size; i++) {
						Socket socket = list.get(i);
						logger.info("销毁：" + socket.toString());
						destroyedSocket(socket);
					}
				}
				
				
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (stopping == true) {
				destory = true;
			}
		}

		public boolean isDestory() {
			return destory;
		}

		public void setDestory(boolean destory) {
			this.destory = destory;
		}
	}
	public static boolean stopSocketThread() {
		boolean flag = true;
		if (socketThread != null) {
			socketThread.stopping = true;
			logger.info("SocketDestroyThread destory");
		}
		return flag;
	}
	
}


/**
 * 标识该socket 使用情况
 * @author 傅泉明
 *
 * 2010-6-25
 */
class SocketCache {
	/** 计时时间(用来比较是否超过空闲时间) 单位：毫秒 */
	private long timeSeconds = System.currentTimeMillis();
	/** 空闲时间(当空闲时间超过该值释放该资源) 单位：毫秒 默认10分钟 */
	private long idleSeconds = 600000;
	/** 表示是否使用中 */
	private boolean isRunning;
	/** 表示是否销毁 */
	private boolean isDestroyed;
	/** 缓存的数据 */
	private Socket socket;
	
	public long getTimeSeconds() {
		return timeSeconds;
	}
	public void setTimeSeconds(long timeSeconds) {
		this.timeSeconds = timeSeconds;
	}
	public long getIdleSeconds() {
		return idleSeconds;
	}
	public void setIdleSeconds(long idleSeconds) {
		this.idleSeconds = idleSeconds;
	}
	public boolean isDestroyed() {
		return isDestroyed;
	}
	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}



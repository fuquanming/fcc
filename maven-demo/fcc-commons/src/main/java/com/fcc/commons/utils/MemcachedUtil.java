package com.fcc.commons.utils;

import java.util.Date;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

/**
 * <p>Description:Memcached 客户端，采用 alisoft-xplatform-asf-cache-2.5.1-jdk1.5.jar</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class MemcachedUtil {
    
	private static ICacheManager<IMemcachedCache> manager;
	
	private static IMemcachedCache memcachedCache;
	
	public static IMemcachedCache getMemcachedCache() {
        return memcachedCache;
    }

    public static void setMemcachedCache(IMemcachedCache memcachedCache) {
        MemcachedUtil.memcachedCache = memcachedCache;
    }

    public static synchronized void init() {
        manager = CacheUtil.getCacheManager(IMemcachedCache.class,
                MemcachedCacheManager.class.getName());
        manager.setConfigFile("config/memcached.xml");
        manager.start();
        memcachedCache = manager.getCache("mclient1");
    }
	
	public static synchronized void destroy() {
		if (manager != null) {
			manager.stop();
			manager = null;
			memcachedCache = null;
		}
	}
	
	/**
	 * 添加一个值,如果存在则失败
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 *            过期时间(毫秒)
	 * @return
	 */
	public static boolean add(String key, Object value, long timeoutSecond) {
		return memcachedCache.add(key, value, new Date(timeoutSecond * 1000));
	}
	
}

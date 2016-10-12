/*
 * @(#)MemcachedLock.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月12日
 * 修改历史 : 
 *     1. [2016年10月12日]创建文件 by fqm
 */
package com.fcc.commons.locks;

import java.util.Date;

import com.alisoft.xplatform.asf.cache.IMemcachedCache;

/**
 * 提供分布式锁：Memcached
 * @version v1.0
 * @author 傅泉明
 */
public class MemcachedLock implements Lock {

    /** 默认缓存时间 */
    private static final int DEFAULT_CACHE_SECONDS = 60 * 60 * 1;// 单位秒 设置成一个小时
    
    private IMemcachedCache memcachedCache;
    
    /** key前缀 */
    private String lockPre = "memcached:lock:";
    
    public IMemcachedCache getMemcachedCache() {
        return memcachedCache;
    }

    public void setMemcachedCache(IMemcachedCache memcachedCache) {
        this.memcachedCache = memcachedCache;
    }
    
    /**
     * 未实现
     * @see com.fcc.commons.locks.Lock#lock(java.lang.String)
     **/
    @Override
    public void lock(String lockKey) {
        int i = 1 % 0;
        i = i + 1;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.locks.Lock#unLock(java.lang.String)
     **/
    @Override
    public void unLock(String lockKey) {
        memcachedCache.remove(lockPre + lockKey);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String)
     **/
    @Override
    public boolean tryLock(String lockKey) {
        String lockName = lockPre + lockKey;
        String identifier = lockName + System.currentTimeMillis();
        return memcachedCache.add(lockName, identifier, new Date(DEFAULT_CACHE_SECONDS * 1000));
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String, long)
     **/
    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds) throws InterruptedException {
        return tryLock(lockKey, tryTimeoutMilliSeconds, DEFAULT_CACHE_SECONDS);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String, long, int)
     **/
    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds, int lockTimeoutSeconds) throws InterruptedException {
        boolean flag = false;
        try {
            String lockName = lockPre + lockKey;
            String identifier = lockName + System.currentTimeMillis();
            
            long end = System.currentTimeMillis() + tryTimeoutMilliSeconds;
            while (System.currentTimeMillis() < end) {
                boolean lockFlag = memcachedCache.add(lockName, identifier, new Date(lockTimeoutSeconds * 1000));
                if (lockFlag) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return flag;
    }

}

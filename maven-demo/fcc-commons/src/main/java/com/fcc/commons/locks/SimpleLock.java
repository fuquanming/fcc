/*
 * @(#)SimpleLock.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月10日
 * 修改历史 : 
 *     1. [2016年10月10日]创建文件 by fqm
 */
package com.fcc.commons.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 提供单机锁
 *
 * @version v1.0
 * @author 傅泉明
 */
public class SimpleLock implements Lock {
	
	private Map<String, ReentrantLock> lockMap = new HashMap<String, ReentrantLock>();
	
	private ReentrantLock lock = new ReentrantLock();
	
	private ReentrantLock getLock(String lockKey) {
		ReentrantLock cacheLock = null;
		lock.lock();
		try {
			cacheLock = lockMap.get(lockKey);
			if (cacheLock == null) {
				cacheLock = new ReentrantLock();
				lockMap.put(lockKey, cacheLock);
			}
		} finally {
			lock.unlock();
		}
		return cacheLock;
	}
	
	@Override
	public void lock(String lockKey) {
		ReentrantLock lock = getLock(lockKey);
		if (lock != null) {
		    lock.lock();
		}
	}

	@Override
	public void unLock(String lockKey) {
		ReentrantLock lock = getLock(lockKey);
		if (lock != null) lock.unlock();
	}

	@Override
	public boolean tryLock(String lockKey) {
		ReentrantLock lock = getLock(lockKey);
		if (lock != null) {
			return lock.tryLock();
		}
		return false;
	}

	@Override
	public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds) throws InterruptedException {
	    ReentrantLock lock = getLock(lockKey);
        if (lock != null) {
            return lock.tryLock(tryTimeoutMilliSeconds, TimeUnit.MILLISECONDS);
        }
        return false;
	}

	/**
	 * 未实现锁超时
	 * @see com.fcc.commons.locks.Lock#tryLock(java.lang.String, long, long)
	 *
	 */
	public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds, int lockTimeoutSeconds) throws InterruptedException {
	    ReentrantLock lock = getLock(lockKey);
	    if (lock != null) {
            return lock.tryLock(tryTimeoutMilliSeconds, TimeUnit.MILLISECONDS);
        }
        return false;
	}

}

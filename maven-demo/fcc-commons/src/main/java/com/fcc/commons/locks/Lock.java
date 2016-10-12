/*
 * @(#)Lock.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月10日
 * 修改历史 : 
 *     1. [2016年10月10日]创建文件 by fqm
 */
package com.fcc.commons.locks;

/**
 * 锁
 * @version v1.0
 * @author 傅泉明
 */
public interface Lock {
	/**
	 * 获取锁定
	 * 如果锁定不可用，出于线程调度目的，将禁用当前线程，并且在获得锁定之前，该线程将一直处于休眠状态。
	 * @param lockKey
	 */
	void lock(String lockKey);
	
	/**
	 * 释放锁定
	 * @param lockKey
	 */
	void unLock(String lockKey);
	
	/**
	 * 仅在调用时锁定为空闲状态才获取该锁定。 
	 * 如果锁定可用，则获取锁定，并立即返回值 true。如果锁定不可用，则此方法将立即返回值 false
	 * @param lockKey
	 * @return
	 */
	boolean tryLock(String lockKey);
	
	/**
	 * 如果锁定在给定的等待时间内空闲，并且当前线程未被中断，则获取锁定。
	 * 如果获得了锁定，则返回值 true。 
	 * @param lockKey
	 * @param tryTimeoutMilliSeconds	等待锁定的最长时间
	 * @return
	 */
	boolean tryLock(String lockKey, long tryTimeoutMilliSeconds) throws InterruptedException;
	
	/**
	 * 如果锁定在给定的等待时间内空闲，并且当前线程未被中断，则获取锁定。
	 * 如果获得了锁定，则返回值 true。 
	 * @param lockKey
	 * @param tryTimeoutMilliSeconds	等待锁定的最长时间
	 * @param lockTimeoutMilliSeconds	锁的超时时间
	 * @return
	 */
	boolean tryLock(String lockKey, long tryTimeoutMilliSeconds, int lockTimeoutSeconds) throws InterruptedException;
	
}

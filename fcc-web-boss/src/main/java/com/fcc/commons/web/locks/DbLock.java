package com.fcc.commons.web.locks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.locks.Lock;
import com.fcc.web.sys.model.SysLock;
import com.fcc.web.sys.service.SysLockService;
/**
 * <p>Description:使用数据库，提供锁，对获取锁效率不高的情况使用</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class DbLock implements Lock {
	@Resource
	private SysLockService sysLockService;
	@Resource
	private BaseService baseService;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Logger logger = Logger.getLogger(DbLock.class);
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SysLock getSysLock(String lockKey) {
		SysLock sysLock = null;
		try {
			sysLock = (SysLock) baseService.get(SysLock.class, lockKey);
		} catch (Exception e) {
		}
		if (sysLock == null) {
			lock.lock();
			try {
				sysLock = new SysLock();
				sysLock.setCreateTime(new Date());
				sysLock.setLockKey(lockKey);
				sysLock.setLockStatus(SysLock.STATUS_UNLOCK);
				baseService.create(sysLock);
			} catch (Exception e) {
				sysLock = null;
			} finally {
				lock.unlock();
			}
		}
		return sysLock;
	}
	
	public boolean tryLock(String lockKey) {
		SysLock sysLock = getSysLock(lockKey);
		if (sysLock != null) {
			String lockStatus = sysLock.getLockStatus();
			boolean unlockFlag = false;
			if (SysLock.STATUS_LOCK.equals(lockStatus)) {// 锁定 比较时间
				Date createTime = sysLock.getCreateTime();
				if (createTime != null) {
					Calendar now = Calendar.getInstance();
					Calendar createTimeCal = Calendar.getInstance();
					createTimeCal.setTime(createTime);
					// 是否超时30分钟
					createTimeCal.add(Calendar.MINUTE, 30);
					if (createTimeCal.before(now)) {// 锁定超过30分钟 释放锁
						unlockFlag = true;
					} else {
						return false;
					}
				}
			}
			// 更新
			try {
				int count = sysLockService.update(sysLock, SysLock.STATUS_LOCK);
				if (count == 1) {
					if (unlockFlag == true) {
						logger.info("释放锁：" + lockKey + "，oldCreateTime：" +format.format(sysLock.getCreateTime()));
					}
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	public void unLock(String lockKey) {
		SysLock sysLock = getSysLock(lockKey);
		if (sysLock != null) {
			String lockStatus = sysLock.getLockStatus();
			if (SysLock.STATUS_LOCK.equals(lockStatus)) {// 锁定
				// 更新
				try {
					int count = sysLockService.update(sysLock, SysLock.STATUS_UNLOCK);
					if (count == 1) {
						return;
					}
				} catch (Exception e) {
				}
			}
		}
	}

    @Override
    public void lock(String lockKey) {
        
    }

    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds) throws InterruptedException {
        return false;
    }

    @Override
    public boolean tryLock(String lockKey, long tryTimeoutMilliSeconds, int lockTimeoutSeconds) throws InterruptedException {
        return false;
    }

}

package com.fcc.web.sys.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.dao.SysLockDao;
import com.fcc.web.sys.model.SysLock;
import com.fcc.web.sys.service.SysLockService;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Service
public class SysLockServiceImpl implements SysLockService {
	@Resource
	private BaseService baseService;
	@Resource
	private SysLockDao sysLockDao;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer update(SysLock oldSysLock, String newLockStatus) {
		return sysLockDao.update(oldSysLock, newLockStatus);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize,
			Map<String, Object> param, boolean isSQL) {
	    return sysLockDao.queryPage(pageNo, pageSize, param, isSQL);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public List<SysLock> query(int pageNo, int pageSize,
			Map<String, Object> param, boolean isSQL) {
	    return sysLockDao.query(pageNo, pageSize, param, isSQL);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
	    return sysLockDao.report(pageNo, pageSize, param, isSQL);
	}
}

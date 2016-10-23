/*
 * @(#)SysUserOnlineServiceImpl.java
 * 
 * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月21日
 * 修改历史 : 
 *     1. [2016年10月21日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.web.sys.model.SysUserOnline;
import com.fcc.web.sys.service.SysUserOnlineService;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class SysUserOnlineServiceImpl implements SysUserOnlineService {

    @Resource
    private BaseDao baseDao;
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#online(com.fcc.web.sys.model.SysUserOnline)
     **/
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void online(SysUserOnline sysUserOnline) {
        baseDao.save(sysUserOnline);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#offline(java.lang.String)
     **/
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void offline(String sid) {
        baseDao.deleteById(SysUserOnline.class, new Object[]{sid}, "id");
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserOnlineService#batchOffline(java.util.List)
     **/
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void batchOffline(List<String> needOfflineIdList) {
        baseDao.deleteById(SysUserOnline.class, needOfflineIdList.toArray(), "id");
    }
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)//只查事务申明
    @Override
    public List<SysUserOnline> findExpiredUserOnlineList(Date expiredDate, int pageSize) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("lastAccessTime", expiredDate);
        return baseDao.queryPage(1, pageSize, "from UserOnline o where o.lastAccessTime <:lastAccessTime order by o.lastAccessTime asc", param, false);
    }
}

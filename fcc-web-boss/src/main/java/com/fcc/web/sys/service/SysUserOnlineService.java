/*
 * @(#)SysUserOnlineService.java
 * 
 * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月21日
 * 修改历史 : 
 *     1. [2016年10月21日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service;

import java.util.Date;
import java.util.List;

import com.fcc.web.sys.model.SysUserOnline;

/**
 * 用户在线
 * @version 
 * @author 傅泉明
 */
public interface SysUserOnlineService {
    
    /**
     * 上线
     * @param sysUserOnline
     */
    public void online(SysUserOnline sysUserOnline);
    /**
     * 下线
     * @param sid
     */
    public void offline(String sid);
    /**
     * 批量下线
     * @param needOfflineIdList
     */
    public void batchOffline(List<String> needOfflineIdList);
    /**
     * 无效的UserOnline
     * @param expiredDate
     * @param pageSize
     * @return
     */
    public List<SysUserOnline> findExpiredUserOnlineList(Date expiredDate, int pageSize);
}

/*
 * @(#)SysLogDao.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月22日
 * 修改历史 : 
 *     1. [2016年11月22日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.model.SysLog;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface SysLogDao {
    
    /**
     * 分页查询
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 分页查询
     * @return
     */
    List<SysLog> query(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
    /**
     * 报表
     * @return
     */
    ListPage report(int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}

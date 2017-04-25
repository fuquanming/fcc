/*
 * @(#)OperateDao.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao;

import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.web.sys.view.OperateValueCount;

/**
 * 系统操作
 * @version v1.0
 * @author 傅泉明
 */
public interface OperateDao {
    /**
     * 删除操作
     * @param ids   操作ID
     * @return
     */
    public Integer delete(String[] ids);
    /**
     * 查询操作最大值
     * @return
     */
    public OperateValueCount getMaxOperateValueAndCount();
    /**
     * 分页查询操作
     * @param pageNo        页码
     * @param pageSize      页记录数
     * @param param         查询参数
     * @return
     */
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
    
}

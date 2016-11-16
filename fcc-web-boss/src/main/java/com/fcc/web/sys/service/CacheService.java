/*
 * @(#)SystemService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月14日
 * 修改历史 : 
 *     1. [2016年11月14日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service;

import java.util.Set;

import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;

/**
 * 系统服务
 * @version 
 * @author 傅泉明
 */
public interface CacheService {
    /**
     * 初始化 
     */
    void init();
    /**
     * 获取模块 
     * @param moduleId
     * @return
     */
    Module getModuleById(String moduleId);
    /**
     * 获取操作
     * @param operateId
     * @return
     */
    Operate getOperateById(String operateId);
    /**
     * 获取模块的操作 
     * @param moduleId
     * @return
     */
    Set<Operate> getModuleOperate(String moduleId);
}

/*
 * @(#)ModuleDao.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao;

import java.util.List;

import com.fcc.web.sys.model.Module;

/**
 * 系统模块
 * @version v1.0
 * @author 傅泉明
 */
public interface ModuleDao {
    
    /**
     * 创建模块操作
     * @param moduleId
     * @param operateIds
     */
    public void createModuleOperate(String moduleId, String[] operateIds);
    /**
     * 删除模块
     * @param moduleId
     * @param isAll     true:及子模块
     * @return
     */
    public Integer delete(String moduleId, boolean isAll);
    
    /**
     * 删除模块操作
     * @param moduleId
     * @param isAll     true:及子模块操作
     * @return
     */
    public Integer deleteModuleOperate(String moduleId, boolean isAll);
    
    /**
     * 获取模块及操作
     * @return
     */
    public List<Module> getModuleWithOperate();
    
    public List<Module> getModuleByUrl();
    
    /**
     * 返回当前模块的子模块
     * @param moduleId
     * @return
     */
    public List<Module> findChildModulesWithOperation(String moduleId);
    
    public List<Module> findModules(List<String> moduleIdList);
    
    public List<Module> findChildModules(String parentModuleId, boolean allChildren);
    
}

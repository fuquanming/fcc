/*
 * @(#)SystemServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月14日
 * 修改历史 : 
 *     1. [2016年11月14日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.RoleModuleRight;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class CacheService {

    @Resource
    private BaseService baseService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private RoleModuleRightService roleModuleRightService;
    
    private ThreadPoolExecutor pool;
    /** 缓存模块URL地址  moduleUrl, moduleId */
    private Map<String, String> moduleUrlMap = new ConcurrentHashMap<String, String>();
    /** 导出数据总量 */
    private int exportDataTotalSize = 500000;
    /** 应用名称 */
    private String appName;
    /** 线程池大小 */
    private int threadPoolSize = 10;

    public void init() {
        System.out.println("CacheService.init");
        pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    public void destroy() {
        pool.shutdown();
    }
    
    public ThreadPoolExecutor getThreadPool() {
        return pool;
    }
    
    public Map<String, String> getModuleUrlMap() {
        return moduleUrlMap;
    }
    
    /**
     * 缓存模块
     * @return ModuleId, Module
     */
    @Cacheable(value = {"moduleCache"}, key = "'moduleMap'")
    public Map<String, Module> getModuleMap() {
        System.out.println("-----getModuleMap-----");
        moduleUrlMap.clear();
        List<Module> moduleList = moduleService.getModuleWithOperate();
        Map<String, Module> moduleMap = new HashMap<String, Module>(moduleList.size());
        for (Module module : moduleList) {
            String moduleId = module.getModuleId();
            String moduleUrl = module.getModuleDesc();
            moduleMap.put(moduleId, module);
            if (moduleUrl != null && !"".equals(moduleUrl)) moduleUrlMap.put(moduleUrl, moduleId);
        }
        return moduleMap;
    }
    
    /**
     * 缓存角色模块权限
     * @return RoleId, ModuleId, RoleModuleRight
     */
    @Cacheable(value = {"roleModuleRightCache"}, key = "'roleModuleRightMap'")
    public Map<String, Map<String, RoleModuleRight>> getRoleModuleRightMap() {
        List<RoleModuleRight> dataList = roleModuleRightService.getRoleModuleRights();
        Map<String, Map<String, RoleModuleRight>> dataMap = new HashMap<String, Map<String, RoleModuleRight>>();
        for (RoleModuleRight data : dataList) {
            String roleId = data.getRoleId();
            Map<String, RoleModuleRight> moduleMap = dataMap.get(roleId);
            if (moduleMap == null) {
                moduleMap = new HashMap<String, RoleModuleRight>();
                dataMap.put(roleId, moduleMap);
            }
            moduleMap.put(data.getModuleId(), data);
        }
        return dataMap;
    }
    
    /**
     * 清除模块缓存 
     */
    @CacheEvict(value = {"moduleCache"}, allEntries = true)
    public void cleanModuleMap() {
        
    }
    
    /**
     * 清除角色模块权限缓存
     */
    @CacheEvict(value = {"roleModuleRightCache"}, allEntries = true)
    public void cleanRoleModuleRightMap() {
        
    }
    
    public int getExportDataTotalSize() {
        return exportDataTotalSize;
    }

    public void setExportDataTotalSize(int exportDataTotalSize) {
        this.exportDataTotalSize = exportDataTotalSize;
    }
    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

}

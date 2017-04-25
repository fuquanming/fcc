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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class CacheService {

    private Logger logger = Logger.getLogger(CacheService.class);
    @Resource
    private BaseService baseService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private RoleModuleRightService roleModuleRightService;
    
    private ReentrantLock lock = new ReentrantLock();
    
    /**
     * 缓存模块URL
     * @return
     */
    @Cacheable(value = {"fcc:moduleUrlMapCache:0"}, key = "'fcc:moduleUrlMapCache'")
    public Map<String, String> getModuleUrlMap() {
        logger.info("-----getModuleUrlMap-----");
        Map<String, Module> moduleMap = getModuleMap();
        Map<String, String> moduleUrlMap = new HashMap<String, String>(moduleMap.size());
        for (Module module : moduleMap.values()) {
            String moduleId = module.getModuleId();
            String moduleUrl = module.getModuleDesc();
            moduleMap.put(moduleId, module);
            if (moduleUrl != null && !"".equals(moduleUrl)) moduleUrlMap.put(moduleUrl, moduleId);
        }
        return moduleUrlMap;
    }
    
    public SysUser getSysUser(HttpServletRequest request) {
        return (SysUser) request.getSession().getAttribute(Constants.SysUserSession.loginUser);
    }
    
    /**
     * 缓存模块
     * @return ModuleId, Module
     */
    @Cacheable(value = {"fcc:moduleMapCache:0"}, key = "'fcc:moduleMapCache'")
    public Map<String, Module> getModuleMap() {
        lock.lock();
        Map<String, Module> moduleMap = null;
        try {
            logger.info("-----getModuleMap-----");
//            moduleUrlMap.clear();
            List<Module> moduleList = moduleService.getModuleWithOperate();
            moduleMap = new HashMap<String, Module>(moduleList.size());
            for (Module module : moduleList) {
                String moduleId = module.getModuleId();
//                String moduleUrl = module.getModuleDesc();
                moduleMap.put(moduleId, module);
//                if (moduleUrl != null && !"".equals(moduleUrl)) moduleUrlMap.put(moduleUrl, moduleId);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return moduleMap;
    }
    
    /**
     * 缓存角色模块权限
     * @return RoleId, ModuleId, RoleModuleRight
     */
    @Cacheable(value = {"fcc:roleModuleRightMapCache:0"}, key = "'fcc:roleModuleRightMapCache'")
    public Map<String, Map<String, RoleModuleRight>> getRoleModuleRightMap() {
        logger.info("-----getRoleModuleRightMap-----");
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
    
    @Cacheable(value = {"fcc:operateMapCache:0"}, key = "'fcc:operateMapCache'")
    public Map<String, Operate> getOperateMap() {
        logger.info("-----getOperateMap-----");
        @SuppressWarnings("unchecked")
        List<Operate> dataList = baseService.getAll(Operate.class);
        Map<String, Operate> dataMap = new HashMap<String, Operate>(dataList.size());
        for (Operate data : dataList) {
            dataMap.put(data.getOperateId(), data);
        }
        return dataMap;
    }
    
    @Cacheable(value = {"fcc:loginCountCache:86400"}, key = "#userId")
    public String getLoginCount(String userId) {
        return "";
    }
    
    @CachePut(value = {"fcc:loginCountCache:86400"}, key = "#userId")
    public String updateLoginCount(String userId, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"))
        .append(":").append(count);
        return sb.toString();
    }
    
    /**
     * 更新模块缓存 
     */
    @CachePut(value = {"fcc:moduleMapCache:0"}, key = "'fcc:moduleMapCache'")
//    @CacheEvict(value = {"fcc:moduleMapCache:0"}, allEntries = true)
    public Map<String, Module> updateModuleMap() {
        return getModuleMap();
    }
    
    /**
     * 更新模块URL缓存 
     */
    @CachePut(value = {"fcc:moduleUrlMapCache:0"}, key = "'fcc:moduleUrlMapCache'")
//    @CacheEvict(value = {"fcc:moduleUrlMapCache:0"}, allEntries = true)
    public Map<String, String> updateModuleUrlMap() {
        return getModuleUrlMap();
    }
    
    /**
     * 更新角色模块权限缓存
     */
    @CachePut(value = {"fcc:roleModuleRightMapCache:0"}, key = "'fcc:roleModuleRightMapCache'")
//    @CacheEvict(value = {"fcc:roleModuleRightMapCache:0"}, allEntries = true)
    public Map<String, Map<String, RoleModuleRight>> updateRoleModuleRightMap() {
        return getRoleModuleRightMap();
    }
    
    /**
     * 更新操作缓存
     */
    @CachePut(value = {"fcc:operateMapCache:0"}, key = "'fcc:operateMapCache'")
//    @CacheEvict(value = {"fcc:operateMapCache:60"}, allEntries = true)
    public Map<String, Operate> updateOperateMap() {
        return getOperateMap();
    }

}

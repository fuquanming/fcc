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
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.cache.annotation.CacheEvict;
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

    @Resource
    private BaseService baseService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private RoleModuleRightService roleModuleRightService;
    
    private ReentrantLock lock = new ReentrantLock();
    /** 缓存模块URL地址  moduleUrl, moduleId */
    private Map<String, String> moduleUrlMap = new ConcurrentHashMap<String, String>();

    public Map<String, String> getModuleUrlMap() {
        System.out.println("getModuleUrlMap=" + moduleUrlMap.size());
        return moduleUrlMap;
    }
    
    public SysUser getSysUser(HttpServletRequest request) {
        return (SysUser) request.getSession().getAttribute(Constants.SysUserSession.loginUser);
    }
    
    /**
     * 缓存模块
     * @return ModuleId, Module
     */
    @Cacheable(value = {"fcc:moduleMapCache"}, key = "'fcc:moduleMapCache'")
    public Map<String, Module> getModuleMap() {
        lock.lock();
        Map<String, Module> moduleMap = null;
        try {
            System.out.println("-----getModuleMap-----");
            moduleUrlMap.clear();
            List<Module> moduleList = moduleService.getModuleWithOperate();
            moduleMap = new HashMap<String, Module>(moduleList.size());
            for (Module module : moduleList) {
                String moduleId = module.getModuleId();
                String moduleUrl = module.getModuleDesc();
                moduleMap.put(moduleId, module);
                if (moduleUrl != null && !"".equals(moduleUrl)) moduleUrlMap.put(moduleUrl, moduleId);
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
    @Cacheable(value = {"fcc:roleModuleRightMapCache"}, key = "'fcc:roleModuleRightMapCache'")
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
    
    @Cacheable(value = {"fcc:operateMapCache"}, key = "'fcc:operateMapCache'")
    public Map<String, Operate> getOperateMap() {
        @SuppressWarnings("unchecked")
        List<Operate> dataList = baseService.getAll(Operate.class);
        Map<String, Operate> dataMap = new HashMap<String, Operate>(dataList.size());
        for (Operate data : dataList) {
            dataMap.put(data.getOperateId(), data);
        }
        return dataMap;
    }
    
    /**
     * 清除模块缓存 
     */
    @CacheEvict(value = {"fcc:moduleMapCache"}, allEntries = true)
    public void cleanModuleMap() {
    }
    
    /**
     * 清除角色模块权限缓存
     */
    @CacheEvict(value = {"fcc:roleModuleRightMapCache"}, allEntries = true)
    public void cleanRoleModuleRightMap() {
    }
    
    /**
     * 清除操作缓存
     */
    @CacheEvict(value = {"fcc:operateMapCache"}, allEntries = true)
    public void cleanOperateMap() {
    }

}
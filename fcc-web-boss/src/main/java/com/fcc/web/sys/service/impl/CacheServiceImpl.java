/*
 * @(#)SystemServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月14日
 * 修改历史 : 
 *     1. [2016年11月14日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.annotations.Cache;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.ModuleService;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private BaseService baseService;
    @Resource
    private ModuleService moduleService;
    
    private Map<String, Module> moduleMap = new HashMap<String, Module>();
    private Map<String, Operate> operateMap = new HashMap<String, Operate>();
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        List<Module> moduleList = baseService.getAll(Module.class);
    }

    @Override
    public Module getModuleById(String moduleId) {
        return moduleMap.get(moduleId);
    }

    @Override
    public Operate getOperateById(String operateId) {
        return operateMap.get(operateId);
    }

    @Override
    public Set<Operate> getModuleOperate(String moduleId) {
        return null;
    }

}

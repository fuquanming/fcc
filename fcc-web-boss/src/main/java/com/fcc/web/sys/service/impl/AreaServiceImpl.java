/*
 * @(#)AreaServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年1月17日
 * 修改历史 : 
 *     1. [2017年1月17日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.web.service.TreeableService;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Area;
import com.fcc.web.sys.service.AreaService;

/**
 * 地区
 * @version 
 * @author 傅泉明
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Resource
    private TreeableService treeableService;

    @Override
    @Transactional(rollbackFor = Exception.class) //事务申明
    public void add(Area data, Area parent) {
        treeableService.add(data, parent);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class) //事务申明
    public void edit(Area data, Area parent) {
        treeableService.edit(data, parent);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class) //事务申明
    public Integer editStatus(String[] ids, boolean nodeStatus) {
        return treeableService.editStatus(Area.class, ids, nodeStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) //事务申明
    public void delete(String nodeId) {
        treeableService.delete(Area.class, nodeId);
    }

    @Override
    @Transactional(readOnly = true) //只查事务申明
    public Area getAreaById(String nodeId) {
        return (Area) treeableService.getTreeableById(Area.class, nodeId);
    }

    @Override
    @Transactional(readOnly = true) //只查事务申明
    public List<EasyuiTreeNode> getTreeGrid(Map<String, Object> params) {
        return treeableService.getTreeGrid(Area.class, params);
    }
    
    @Override
    @Transactional(readOnly = true) //只查事务申明
    public List<EasyuiTreeNode> getTree(String nodeId, boolean allChildren, boolean parent) {
        return treeableService.getTree(Area.class, nodeId, allChildren, parent);
    }

}

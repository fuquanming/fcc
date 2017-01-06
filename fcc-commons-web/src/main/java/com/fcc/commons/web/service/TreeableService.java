/*
 * @(#)TreeableService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2017年1月4日
 * 修改历史 : 
 *     1. [2017年1月4日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.model.Treeable;
import com.fcc.commons.web.view.EasyuiTreeNode;

/**
 * 树形结构
 * @version 
 * @author 傅泉明
 */
public interface TreeableService {
    /**
     * 添加
     * @param data
     */
    void add(Treeable data, Treeable parent);
    /**
     * 修改
     * @param data
     */
    void edit(Treeable data, Treeable parent);
    /**
     * 更新是否显示
     * @param clazz
     * @param ids
     * @param moduleStatus
     */
    Integer editStatus(Class<?> clazz, String[] ids, boolean nodeStatus);
    /**
     * 删除
     * @param clazz
     * @param nodeId
     */
    void delete(Class<?> clazz, String nodeId);
    /**
     * 查询下级
     * @param clazz
     * @param parentNodeId    
     * @param allChildren      是否所有下级
     * @return
     */
    List<Treeable> findChilds(Class<?> clazz, String parentNodeId, boolean allChildren);
    /**
     * 取得Treeable通过ID
     * @param clazz
     * @param nodeId
     * @return
     */
    Treeable getTreeableById(Class<?> clazz, String nodeId);
    /**
     * 
     * @return treeGrid
     */
    List<EasyuiTreeNode> getTreeGrid(Class<?> clazz, Map<String, Object> params);
    /**
     * 
     * @return tree
     */
    List<EasyuiTreeNode> getTree(Class<?> clazz, String nodeId, boolean allChildren);
    
    /**
     * 分页查询
     * @return
     */
    ListPage queryPage(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}

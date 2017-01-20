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
     * 检查是否存在code
     * @param clazz
     * @param nodeCode
     * @param nodeId
     * @return          true：存在，false：不存在
     */
    boolean checkNodeCode(Class<?> clazz, String nodeCode, String nodeId);
    /**
     * 查询下级
     * @param clazz
     * @param parentNodeId    
     * @param allChildren       是否所有下级
     * @param parent            是否包含parentNodeId
     * @return
     */
    List<Treeable> getChilds(Class<?> clazz, String parentNodeId, boolean allChildren, boolean parent);
    /**
     * 取得Treeable通过ID
     * @param clazz
     * @param nodeId
     * @return
     */
    Treeable getTreeableById(Class<?> clazz, String nodeId);
    /**
     * 取得Treeable通过Name
     * @param clazz
     * @param nodeCode
     * @return
     */
    Treeable getTreeableByName(Class<?> clazz, String nodeName);
    /**
     * 查询
     * @param clazz
     * @param params        
     * @return treeGrid
     */
    List<EasyuiTreeNode> getTreeGrid(Class<?> clazz, Map<String, Object> params);
    /**
     * 查询树形
     * @return tree
     */
    List<EasyuiTreeNode> getTree(Class<?> clazz, String nodeId, boolean allChildren, boolean parent);
}

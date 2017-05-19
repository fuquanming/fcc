/*
 * @(#)TreeableDao.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2017年1月5日
 * 修改历史 : 
 *     1. [2017年1月5日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.dao;

import java.util.List;
import java.util.Map;

import com.fcc.commons.web.model.Treeable;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface TreeableDao {
    /**
     * 删除
     * @param clazz
     * @param nodeId
     */
    Integer delete(Class<?> clazz, String nodeId);
    /**
     * 更新机构是否显示
     * @param treeable
     */
    Integer editNodeStatus(Treeable treeable);
    /**
     * 更新是否显示
     * @param clazz
     * @param ids
     * @param nodeStatus
     */
    Integer editNodeStatus(Class<?> clazz, String[] ids, boolean nodeStatus);
    /**
     * 检查是否存在code
     * @param clazz
     * @param nodeCode
     * @param nodeId
     * @return          true：存在，false：不存在
     */
    boolean checkNodeCode(Class<?> clazz, String nodeCode, String nodeId);
    /**
     * 取得Treeable通过Name
     * @param clazz
     * @param nodeName
     * @return
     */
    Treeable getTreeableByName(Class<?> clazz, String nodeName);
    /**
     * 取得Treeable通过Code
     * @param clazz
     * @param nodeCode
     * @return
     */
    Treeable getTreeableByCode(Class<?> clazz, String nodeCode);
    /**
     * 查询子节点
     * @param clazz
     * @param parentNodeId      父ID
     * @param allChildren       是否全部子节点
     * @param parent            是否包含parentNodeId
     * @return
     */
    public List<Treeable> getChilds(Class<?> clazz, String parentNodeId, boolean allChildren, boolean parent);
    /**
     * 查询
     * @param clazz
     * @param param
     * @return
     */
    public List<Treeable> queryList(Class<?> clazz, Map<String, Object> params);
}

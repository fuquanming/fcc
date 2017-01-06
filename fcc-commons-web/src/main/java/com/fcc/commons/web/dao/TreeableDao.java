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

import com.fcc.commons.data.ListPage;
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
     * 查询子节点
     * @param clazz
     * @param parentNodeId     父ID
     * @param allChildren       是否全部子节点
     * @return
     */
    public List<Treeable> findChilds(Class<?> clazz, String parentNodeId, boolean allChildren);
    /**
     * 查询
     * @param clazz
     * @param param
     * @return
     */
    public List<Treeable> queryList(Class<?> clazz, Map<String, Object> params);
    /**
     * 分页查询
     * @param clazz
     * @param pageNo
     * @param pageSize
     * @param param
     * @param isSQL
     * @return
     */
    ListPage queryPage(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> param, boolean isSQL);
}

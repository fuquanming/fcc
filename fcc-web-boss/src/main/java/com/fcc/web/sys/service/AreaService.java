/*
 * @(#)AreaService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年1月17日
 * 修改历史 : 
 *     1. [2017年1月17日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Area;

/**
 * 地区
 * @version 
 * @author 傅泉明
 */
public interface AreaService {
    /**
     * 添加
     * @param data
     */
    void add(Area data, Area parent);
    /**
     * 修改
     * @param data
     */
    void edit(Area data, Area parent);
    /**
     * 更新地区状态
     * @param ids
     * @param nodeStatus
     * @return
     */
    Integer editStatus(String[] ids, boolean nodeStatus);
    /**
     * 删除
     * @param clazz
     * @param nodeId
     */
    void delete(String nodeId);
    /**
     * 通过ID获取地区
     * @param areaId
     * @return
     */
    Area getAreaById(String nodeId);
    /**
     * 查询地区
     * @param params        查询参数
     * @return treeGrid
     */
    List<EasyuiTreeNode> getTreeGrid(Map<String, Object> params);
    /**
     * 查询树形地区
     * @param areaId        父节点ID
     * @param allChildren   是否显示所有字节点
     * @param parent        是否显示父节点
     * @return
     */
    List<EasyuiTreeNode> getTree(String nodeId, boolean allChildren, boolean parent);
}

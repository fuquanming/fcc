/*
 * @(#)TreeableDaoImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2017年1月5日
 * 修改历史 : 
 *     1. [2017年1月5日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.web.dao.TreeableDao;
import com.fcc.commons.web.model.Treeable;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Repository
@SuppressWarnings("unchecked")
public class TreeableDaoImpl implements TreeableDao {

    @Resource
    private BaseDao baseDao;
    
    @Override
    public Integer delete(Class<?> clazz, String nodeId) {
        // 删除
        Treeable treeable = (Treeable) baseDao.get(clazz, nodeId);
        if (treeable != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from ").append(clazz.getName()).append(" where parentIds like ?");
            return baseDao.executeHql(sb.toString(), treeable.getParentIds() + "%");
        }
        return 0; 
    }

    @Override
    public Integer editNodeStatus(Treeable treeable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(treeable.getClass().getName()).append(" set nodeStatus=? where parentIds like ?");
        return baseDao.executeHql(sb.toString(), treeable.getNodeStatus(), treeable.getParentIds() + "-%");
    }

    @Override
    public Integer editNodeStatus(Class<?> clazz, String[] ids, boolean nodeStatus) {
        String className = clazz.getName();
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("nodeId", ids);
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(className).append(" where nodeId in(:nodeId)");
        List<Treeable> list = baseDao.find(sb.toString(), params);
        for (Treeable data : list) {
            data.setNodeStatus(nodeStatus);
            editNodeStatus(data);
        }
        params.put("nodeStatus", nodeStatus);
        sb.setLength(0);
        sb.append("update ").append(className).append(" set nodeStatus=:nodeStatus where nodeId in(:nodeId)");
        return baseDao.executeHql(sb.toString(), params);
    }

    @Override
    public List<Treeable> getChilds(Class<?> clazz, String parentNodeId, boolean allChildren, boolean parent) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> param = null;
        String className = clazz.getName();
        sb.append("select t, (SELECT count(1) from ").append(className).append(" a where a.parentId=t.nodeId) from ").append(className).append(" t where 1=1 ");
        if (allChildren) {
            if (parentNodeId == null || "".equals(parentNodeId) || Treeable.ROOT.getNodeId().equals(parentNodeId)) {
            } else {
                Treeable data = (Treeable) baseDao.get(clazz, parentNodeId);
                if (data != null) {
                    param = new HashMap<String, Object>(1);
                    sb.append(" and parentIds like:nodeId ");
                    if (parent) {
                        param.put("nodeId", data.getParentIds() + "%");
                    } else {
                        param.put("nodeId", data.getParentIds() + "-%");
                    }
                } else {
                    return Collections.EMPTY_LIST;
                }
            }
        } else {
            param = new HashMap<String, Object>(1);
            sb.append(" and parentId=:nodeId");
            if (parentNodeId == null || "".equals(parentNodeId)) {
                param.put("nodeId", Treeable.ROOT.getNodeId());
            } else {
                if (parent) {
                    sb.append(" or nodeId=:nodeId");
                }
                param.put("nodeId", parentNodeId);
            }
        }
        sb.append(" order by nodeLevel, nodeSort, parentIds");
        List<Object> list = baseDao.find(sb.toString(), param);
        List<Treeable> dataList = getTreeableList(list, true);
        return dataList;
    }
    
    @Override
    public List<Treeable> queryList(Class<?> clazz, Map<String, Object> params) {
        List<Treeable> dataList = null;
        String className = clazz.getName();
        // 做检索查询
        StringBuilder bHql = new StringBuilder();
        bHql.append("select t, (SELECT count(1) from ").append(className).append(" a where a.parentId=t.nodeId) from ").append(className)
                .append(" t where 1=1 ");
        if (params.get("nodeName") != null) {
            bHql.append(" and  t.nodeName like :nodeName ");
        }
        if (params.get("nodeCode") != null) {
            bHql.append(" and  t.nodeCode like :nodeCode ");
        }
        if (params.get("nodeLevel") != null) {
            bHql.append(" and  t.nodeLevel = :nodeLevel ");
        }
        if (params.get("nodeStatus") != null) {
            bHql.append(" and  t.nodeStatus = :nodeStatus ");
        }
        Object parentId = params.get("parentId");
        if (parentId == null || "".equals(parentId) || Treeable.ROOT.getNodeId().equals(parentId)) {
            params.remove("parentId");
        } else {
            Treeable data = (Treeable) baseDao.get(clazz, parentId.toString());
            bHql.append(" and  t.parentIds like :parentId ");
            params.put("parentId", data.getParentIds() + "%");
        }
        bHql.append(" order by t.nodeLevel, t.nodeSort, t.parentIds");
        List<Object> list = baseDao.find(bHql.toString(), params);
        dataList = getTreeableList(list, true);
        return dataList;
    }
    
    private List<Treeable> getTreeableList(List<Object> list, boolean childFlag) {
        List<Treeable> dataList = new ArrayList<Treeable>(list.size());
        for (Object obj : list) {
            Object[] objs = (Object[]) obj;
            Treeable data = (Treeable) objs[0];
            if (childFlag) data.setChildSize(Integer.parseInt(objs[1].toString()));
            dataList.add(data);
        }
        return dataList;
    }
}

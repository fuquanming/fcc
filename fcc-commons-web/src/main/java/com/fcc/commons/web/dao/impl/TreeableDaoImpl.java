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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.data.ListPage;
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
    
    /**
     * @see com.fcc.commons.web.dao.TreeableDao#delete(java.lang.Class, java.lang.String)
     **/
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

    /**
     * @see com.fcc.commons.web.dao.TreeableDao#editNodeStatus(com.fcc.commons.web.model.Treeable)
     **/
    @Override
    public Integer editNodeStatus(Treeable treeable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(treeable.getClass().getName()).append(" set nodeStatus=? where parentIds like ?");
        return baseDao.executeHql(sb.toString(), treeable.getNodeStatus(), treeable.getParentIds() + "-%");
    }

    /**
     * @see com.fcc.commons.web.dao.TreeableDao#editNodeStatus(java.lang.Class, java.lang.String[], boolean)
     **/
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

    /**
     * @see com.fcc.commons.web.dao.TreeableDao#findChilds(java.lang.Class, java.lang.String, boolean)
     **/
    @Override
    public List<Treeable> findChilds(Class<?> clazz, String parentNodeId, boolean allChildren) {
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
                    param.put("nodeId", data.getParentIds() + "-%");
                }
            }
        } else {
//            sb.append(" and organId like:organId ");
//            param.put("organId", parentOrganId + "-____");
            param = new HashMap<String, Object>(1);
            sb.append(" and parentId=:nodeId");
            if (parentNodeId == null || "".equals(parentNodeId)) {
                param.put("nodeId", Treeable.ROOT.getNodeId());
            } else {
                param.put("nodeId", parentNodeId);
            }
        }
        sb.append(" order by nodeLevel, nodeSort, parentIds");
        List<Object> list = baseDao.find(sb.toString(), param);
        List<Treeable> dataList = getTreeableList(list, true);
        return dataList;
    }
    
    public List<Treeable> queryList(Class<?> clazz, Map<String, Object> params) {
        String className = clazz.getName();
        String allName = "all";
        List<Treeable> dataList = null;
        if (params == null || params.size() == 0) {
            dataList = findChilds(clazz, null, false);
        } else {
            Object parentId = params.get("parentId");
            if (parentId != null) {
                Boolean allChildren = (Boolean) params.get(allName);
                dataList = findChilds(clazz, parentId.toString(), allChildren == null ? false : allChildren);
            } else {
                StringBuilder bHql = new StringBuilder();
                bHql.append("select t, (SELECT count(1) from ").append(className).append(" a where a.parentId=t.nodeId) from ").append(className).append(" t where 1=1 ");
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
                params.remove(allName);
                bHql.append(" order by t.nodeLevel, t.nodeSort, t.parentIds");
                List<Object> list = baseDao.find(bHql.toString(), params);
                dataList = getTreeableList(list, false);
            }
        }
        return dataList;
    }
    
    @Override
    public ListPage queryPage(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        String className = clazz.getName();
        StringBuilder cHql = new StringBuilder("select count(t) from ").append(className).append(" t where 1=1 ");
        StringBuilder bHql = new StringBuilder("select t from ").append(className).append(" t where 1=1 ");
        if (param != null) {
            if (param.get("nodeId") != null) {
                bHql.append(" and  t.nodeId = :nodeId ");
                cHql.append(" and  t.nodeId = :nodeId ");
            }
            if (param.get("nodeName") != null) {
                bHql.append(" and  t.nodeName = :nodeName ");
                cHql.append(" and  t.nodeName = :nodeName ");
            }
            if (param.get("nodeCode") != null) {
                bHql.append(" and  t.nodeCode = :nodeCode ");
                cHql.append(" and  t.nodeCode = :nodeCode ");
            }
            if (param.get("nodeLevel") != null) {
                bHql.append(" and  t.nodeLevel = :nodeLevel ");
                cHql.append(" and  t.nodeLevel = :nodeLevel ");
            }
            if (param.get("nodeSort") != null) {
                bHql.append(" and  t.nodeSort = :nodeSort ");
                cHql.append(" and  t.nodeSort = :nodeSort ");
            }
            if (param.get("nodeDesc") != null) {
                bHql.append(" and  t.nodeDesc = :nodeDesc ");
                cHql.append(" and  t.nodeDesc = :nodeDesc ");
            }
            if (param.get("nodeStatus") != null) {
                bHql.append(" and  t.nodeStatus = :nodeStatus ");
                cHql.append(" and  t.nodeStatus = :nodeStatus ");
            }
            if (param.get("parentId") != null) {
                bHql.append(" and  t.parentId = :parentId ");
                cHql.append(" and  t.parentId = :parentId ");
            }
            if (param.get("parentIds") != null) {
                bHql.append(" and  t.parentIds = :parentIds ");
                cHql.append(" and  t.parentIds = :parentIds ");
            }
            if (param.get("sortColumns") != null) {
                bHql.append(" order by t.").append(param.get("sortColumns"));
                param.remove("sortColumns");
                if (param.get("orderType") != null) {
                    bHql.append(" ").append(param.get("orderType"));
                    param.remove("orderType");
                }
                bHql.append(", t.nodeId desc");
            }
        }
        return baseDao.queryPage(pageNo, pageSize, cHql.toString(), bHql.toString(), param, false);
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

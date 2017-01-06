/*
 * @(#)TreeableServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-web
 * 创建日期 : 2017年1月5日
 * 修改历史 : 
 *     1. [2017年1月5日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.dao.TreeableDao;
import com.fcc.commons.web.model.Treeable;
import com.fcc.commons.web.service.TreeableService;
import com.fcc.commons.web.view.EasyuiTreeNode;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class TreeableServiceImpl implements TreeableService {

    @Resource
    private BaseService baseService;
    @Resource
    private TreeableDao treeableDao;
    
    /**
     * @see com.fcc.commons.web.service.TreeableService#add(com.fcc.commons.web.model.Treeable)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void add(Treeable data, Treeable parent) {
        data.setNodeLevel(parent.getNodeLevel() + 1);
        data.setParentIds(data.buildParendIds(parent, data.getNodeId()));
        baseService.add(data);
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#edit(com.fcc.commons.web.model.Treeable)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void edit(Treeable data, Treeable parent) {
        data.setNodeLevel(parent.getNodeLevel() + 1);
        data.setParentIds(data.buildParendIds(parent, data.getNodeId()));
        baseService.edit(data);
        treeableDao.editNodeStatus(data);
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#editStatus(java.lang.Class, java.lang.String[], boolean)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public Integer editStatus(Class<?> clazz, String[] ids, boolean nodeStatus) {
        return treeableDao.editNodeStatus(clazz, ids, nodeStatus);
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#delete(java.lang.Class, java.lang.String)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void delete(Class<?> clazz, String nodeId) {
        treeableDao.delete(clazz, nodeId);
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#findChilds(java.lang.Class, java.lang.String, boolean)
     **/
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<Treeable> findChilds(Class<?> clazz, String parentNodeId, boolean allChildren) {
        return treeableDao.findChilds(clazz, parentNodeId, allChildren);
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#getOrganById(java.lang.Class, java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public Treeable getTreeableById(Class<?> clazz, String nodeId) {
        Treeable treeable = (Treeable) baseService.get(clazz, nodeId);
        return treeable;
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#getTreeGrid(java.lang.Class, java.lang.String, java.lang.String, boolean)
     **/
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<EasyuiTreeNode> getTreeGrid(Class<?> clazz, Map<String, Object> params) {
        Boolean all = null;
        if (params != null) {
            all = (Boolean) params.get("all");
        }
        List<Treeable> dataList = treeableDao.queryList(clazz, params);
        List<EasyuiTreeNode> nodeList = null;
        if (all != null && all == true) {
            nodeList = getEasyuiTreeNodeChild(dataList);
        }
        if (nodeList == null) {
            nodeList = new ArrayList<EasyuiTreeNode>(dataList.size());
            for (Treeable data : dataList) {
                nodeList.add(getEasyuiTreeNode(data));
            }
        }
        return nodeList;
    }

    /**
     * @see com.fcc.commons.web.service.TreeableService#getTree(java.lang.Class, java.lang.String, java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<EasyuiTreeNode> getTree(Class<?> clazz, String nodeId, boolean allChildren) {
        List<Treeable> dataList = treeableDao.findChilds(clazz, nodeId, allChildren);
        List<EasyuiTreeNode> nodeList = null;
        if (allChildren) {
            nodeList = getEasyuiTreeNodeChild(dataList);
        } else {
            nodeList = new ArrayList<EasyuiTreeNode>(dataList.size());
            for (Treeable data : dataList) {
                nodeList.add(getEasyuiTreeNode(data));
            }
        }
        return nodeList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public ListPage queryPage(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> param, boolean isSQL) {
        ListPage listPage = treeableDao.queryPage(clazz, pageNo, pageSize, param, isSQL);
        int size = listPage.getDataSize();
        if (size > 0) {
            List<Treeable> dataList = listPage.getDataList();
            List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>(size);
            for (Treeable data : dataList) {
                nodeList.add(getEasyuiTreeNode(data));
            }
            listPage.setDataList(nodeList);
        }
        return listPage;
    }
    
    private EasyuiTreeNode getEasyuiTreeNode(Treeable data) {
        EasyuiTreeNode node = new EasyuiTreeNode();
        node.setId(data.getNodeId());
        node.setText(data.getNodeName());
        node.setState(data.getChildSize() > 0 ? EasyuiTreeNode.STATE_CLOSED : EasyuiTreeNode.STATE_OPEN);
        Map<String, Object> attributes = new HashMap<String, Object>(4);
        attributes.put("nodeLevel", data.getNodeLevel());
        attributes.put("nodeSort", data.getNodeSort());
        attributes.put("nodeStatus", data.getNodeStatus());
        attributes.put("nodeDesc", data.getNodeDesc());
        attributes.put("parentId", data.getParentId());
        node.setAttributes(attributes);
        return node;
    }
    
    private List<EasyuiTreeNode> getEasyuiTreeNodeChild(List<Treeable> dataList) {
        List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
        if (dataList != null) {
            TreeSet<Treeable> dataSet = new TreeSet<Treeable>();
            dataSet.addAll(dataList);
            
            Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
            
            for (Iterator<Treeable> it = dataSet.iterator(); it.hasNext();) {
                Treeable m = it.next();
                
                EasyuiTreeNode node = getEasyuiTreeNode(m);
                
                nodeMap.put(node.getId(), node);
                
                String parendId = m.getParentId();
                EasyuiTreeNode cacheNode = nodeMap.get(parendId);
                if (cacheNode != null) {
                    List<EasyuiTreeNode> children = cacheNode.getChildren();
                    if (children == null) children = new ArrayList<EasyuiTreeNode>();
                    children.add(node);
                    cacheNode.setChildren(children);
                    cacheNode.setState(EasyuiTreeNode.STATE_OPEN);
                } else {
                    nodeList.add(node);// 移除根目录后，添加节点
                }
            }
        }
        return nodeList;
    }

}

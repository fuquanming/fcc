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
    
    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void add(Treeable data, Treeable parent) {
        if (parent == null) parent = Treeable.ROOT;
        data.setNodeLevel(parent.getNodeLevel() + 1);
        data.setParentIds(data.buildParendIds(parent, data.getNodeId()));
        baseService.add(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void edit(Treeable data, Treeable parent) {
        if (parent == null) parent = Treeable.ROOT;
        data.setNodeLevel(parent.getNodeLevel() + 1);
        data.setParentIds(data.buildParendIds(parent, data.getNodeId()));
        baseService.edit(data);
        treeableDao.editNodeStatus(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public Integer editStatus(Class<?> clazz, String[] ids, boolean nodeStatus) {
        return treeableDao.editNodeStatus(clazz, ids, nodeStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void delete(Class<?> clazz, String nodeId) {
        treeableDao.delete(clazz, nodeId);
    }
    
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public boolean checkNodeCode(Class<?> clazz, String nodeCode, String nodeId) {
        return treeableDao.checkNodeCode(clazz, nodeCode, nodeId);
    }

    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<Treeable> getChilds(Class<?> clazz, String parentNodeId, boolean allChildren, boolean parent) {
        return treeableDao.getChilds(clazz, parentNodeId, allChildren, parent);
    }

    @Override
    @Transactional(readOnly = true)//只查事务申明
    public Treeable getTreeableById(Class<?> clazz, String nodeId) {
        if (nodeId == null || "".equals(nodeId) || Treeable.ROOT.getNodeId().equals(nodeId)) {
            return Treeable.ROOT;
        }
        Treeable treeable = (Treeable) baseService.get(clazz, nodeId);
        return treeable;
    }
    
    @Override
    @Transactional(readOnly = true)//只查事务申明
    public Treeable getTreeableByName(Class<?> clazz, String nodeName) {
        return treeableDao.getTreeableByName(clazz, nodeName);
    }

    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<EasyuiTreeNode> getTreeGrid(Class<?> clazz, Map<String, Object> params) {
        List<Treeable> dataList = null;
        List<EasyuiTreeNode> nodeList = null;
        dataList = treeableDao.queryList(clazz, params);
        nodeList = new ArrayList<EasyuiTreeNode>(dataList.size());
        for (Treeable data : dataList) {
            nodeList.add(getEasyuiTreeNode(data, false));
        }
        return nodeList;
    }

    @Override
    @Transactional(readOnly = true)//只查事务申明
    public List<EasyuiTreeNode> getTree(Class<?> clazz, String nodeId, boolean allChildren, boolean parent) {
        List<Treeable> dataList = treeableDao.getChilds(clazz, nodeId, allChildren, parent);
        List<EasyuiTreeNode> nodeList = null;
        if (allChildren) {
            nodeList = getEasyuiTreeNodeChild(dataList);
        } else {
            nodeList = new ArrayList<EasyuiTreeNode>(dataList.size());
            for (Treeable data : dataList) {
                nodeList.add(getEasyuiTreeNode(data, true));
            }
        }
        return nodeList;
    }
    
    private EasyuiTreeNode getEasyuiTreeNode(Treeable data, boolean childFlag) {
        EasyuiTreeNode node = new EasyuiTreeNode();
        node.setId(data.getNodeId());
        node.setText(data.getNodeName());
        if (childFlag) node.setState(data.getChildSize() > 0 ? EasyuiTreeNode.STATE_CLOSED : EasyuiTreeNode.STATE_OPEN);
        Map<String, Object> attributes = new HashMap<String, Object>(6);
        attributes.put("nodeLevel", data.getNodeLevel());
        attributes.put("nodeSort", data.getNodeSort());
        attributes.put("nodeCode", data.getNodeCode());
        attributes.put("nodeStatus", data.getNodeStatus());
        attributes.put("nodeDesc", data.getNodeDesc());
        attributes.put("parentId", data.getParentId());
        node.setAttributes(attributes);
        return node;
    }
    /**
     * 构建树形结构
     * @param dataList
     * @return
     */
    private List<EasyuiTreeNode> getEasyuiTreeNodeChild(List<Treeable> dataList) {
        List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
        if (dataList != null) {
            TreeSet<Treeable> dataSet = new TreeSet<Treeable>();
            dataSet.addAll(dataList);
            
            Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
            
            for (Iterator<Treeable> it = dataSet.iterator(); it.hasNext();) {
                Treeable m = it.next();
                
                EasyuiTreeNode node = getEasyuiTreeNode(m, true);
                
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

package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.web.view.EasyuiTreeGridOrgan;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.dao.OrganizationDao;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.OrganizationService;

/**
 * <p>Description:组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
	@Resource
	private BaseService baseService;
	@Resource
	private OrganizationDao organizationDao;
	@Resource
	private CacheService cacheService;
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#add(com.fcc.web.sys.model.Organization)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)//事务申明
	public void add(Organization data) {
		baseService.create(data);
	}
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#edit(com.fcc.web.sys.model.Organization)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)//事务申明
	public void edit(Organization data) {
		baseService.update(data);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#delete(java.lang.String)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)//事务申明
    public void delete(String organId) {
	    organizationDao.delete(organId);
    }
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#findOrgans(java.util.Collection)
     **/
	@Override
    @Transactional(readOnly = true)//只查事务申明
    public List<Organization> findOrgans(Collection<String> organIdList) {
		return organizationDao.findOrgans(organIdList);
    }
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#findChildOrgans(java.lang.String, boolean)
     **/
	@Override
    @Transactional(readOnly = true)//只查事务申明
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren) {
		return organizationDao.findChildOrgans(parentOrganId, allChildren);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#getOrganById(java.lang.String)
     **/
	@Override
    @Transactional(readOnly = true)
    public Organization getOrganById(String organId) {
		Organization module = null;
        if(organId != null){
        	if(Organization.ROOT.getOrganId().equals(organId)){
        		module = Organization.ROOT;
        	}else{
        		module = (Organization) baseService.get(Organization.class, organId);
        	}
        }
        return module;
    }
	
	/**
	 * 获取用户组织机构以及下级
	 * @param sysUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
	public List<Organization> getOrgans(SysUser sysUser) {
	    List<Organization> dataList = null;
	    String organId = sysUser.getDept();
        if (sysUser.isAdmin()) {// 管理员无组织机构
            dataList = baseService.getAll(Organization.class);
        } else {
            // 制定ID查询
            dataList = findChildOrgans(organId, true);
            dataList.add((Organization) baseService.get(Organization.class, organId));
        }
        return dataList;
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.OrganizationService#getOrganTreeGrid(com.fcc.web.sys.model.SysUser)
     **/
	@Override
	@Transactional(readOnly = true)
    public List<EasyuiTreeGridOrgan> getOrganTreeGrid(SysUser sysUser) {
	    List<EasyuiTreeGridOrgan> nodeList = new ArrayList<EasyuiTreeGridOrgan>();
	    List<Organization> dataList = getOrgans(sysUser);
        
        if (dataList != null) {
            TreeSet<Organization> dataSet = new TreeSet<Organization>();
            dataSet.addAll(dataList);
            
            Map<String, EasyuiTreeGridOrgan> nodeMap = new HashMap<String, EasyuiTreeGridOrgan>();
            
            for (Iterator<Organization> it = dataSet.iterator(); it.hasNext();) {
                Organization m = it.next();
                
                EasyuiTreeGridOrgan node = new EasyuiTreeGridOrgan();
                node.setId(m.getOrganId());
                node.setText(m.getOrganName());
                node.setOrganDesc(m.getOrganDesc());
                node.setOrganSort(m.getOrganSort());
                node.setOrganLevel(m.getOrganLevel());
                nodeMap.put(node.getId(), node);
                
                String parendId = m.getParentId();
                EasyuiTreeGridOrgan cacheNode = nodeMap.get(parendId);
                if (cacheNode != null) {
                    List<EasyuiTreeNode> children = cacheNode.getChildren();
                    if (children == null) children = new ArrayList<EasyuiTreeNode>();
                    children.add(node);
                    cacheNode.setChildren(children);
                } else {
                    nodeList.add(node);// 移除根目录后，添加节点
                }
            }
        }
	    return nodeList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EasyuiTreeNode> getOrganTree(SysUser sysUser) {
	    List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
	    List<Organization> dataList = getOrgans(sysUser);
	    TreeSet<Organization> menuSet = new TreeSet<Organization>();
        menuSet.addAll(dataList);
        
        Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
        
        for (Iterator<Organization> it = menuSet.iterator(); it.hasNext();) {
            Organization m = it.next();
            
            EasyuiTreeNode node = new EasyuiTreeNode();
            node.setId(m.getOrganId());
            node.setText(m.getOrganName());
            node.setAttributes(new HashMap<String, Object>());
            nodeMap.put(node.getId(), node);
            String parendId = m.getParentId();
            EasyuiTreeNode cacheNode = nodeMap.get(parendId);
            if (cacheNode != null) {
                List<EasyuiTreeNode> children = cacheNode.getChildren();
                if (children == null) children = new ArrayList<EasyuiTreeNode>();
                children.add(node);
                cacheNode.setChildren(children);
            } else {
                nodeList.add(node);// 移除根目录后，添加节点
            }
        }
	    return nodeList;
	}
}

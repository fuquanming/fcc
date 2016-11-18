package com.fcc.web.sys.service;

import java.util.Collection;
import java.util.List;

import com.fcc.commons.web.view.EasyuiTreeGridOrgan;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
/**
 * 组织机构
 * 
 * @version 
 * @author 傅泉明
 */
public interface OrganizationService {
    /**
     * 添加组织机构 
     * @param data
     */
    void add(Organization data);
    /**
     * 修改组织机构
     * @param data
     */
    void edit(Organization data);
    /**
     * 删除组织机构
     * @param organId
     */
    void delete(String organId);

    /**
     * 查找组织机构通过ID
     * @param organIdList
     * @return
     */
    List<Organization> findOrgans(Collection<String> organIdList);

    /**
     * 查询组织机构的下级
     * @param parentOrganId    
     * @param allChildren      是否所有下级
     * @return
     */
    List<Organization> findChildOrgans(String parentOrganId, boolean allChildren);

    /**
     * 取得组织机构通过ID
     * @param organId
     * @return
     */
    Organization getOrganById(String organId);

    /**
     * 取得组织机构通过用户
     * @param sysUser
     * @return treeGrid
     */
    List<EasyuiTreeGridOrgan> getOrganTreeGrid(SysUser sysUser);
    
    /**
     * 取得组织机构通过用户
     * @return tree
     */
    List<EasyuiTreeNode> getOrganTree(SysUser sysUser);

}
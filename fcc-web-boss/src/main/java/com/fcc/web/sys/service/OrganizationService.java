package com.fcc.web.sys.service;

import java.util.Collection;
import java.util.List;

import com.fcc.web.sys.model.Organization;
/**
 * <p>Description:组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface OrganizationService {
	/**
	 * 保存组织机构
	 * @param data
	 */
	public void create(Organization data);
	/**
	 * 更新组织机构
	 * @param data
	 */
	public void update(Organization data);
	/**
	 * 删除组织机构
	 * @param module
	 */
	public void delete(String organId);
	/**
	 * 获取组织机构
	 * @return
	 */
	public List<Organization> getOrgans();

	/**
	 * 返回当前组织机构的子组织机构
	 * @param organId 当前组织的ID
	 * @return
	 */
	public List<Organization> findChildOrgans(String organId);

	/**
	 * 返回当前组织机构的信息
	 * @param organIdList 当前组织机构的ID
	 * @return
	 */
	public List<Organization> findOrgans(Collection<String> organIdList);
	/**
	 * 返回当前组织机构的子组织机构
	 * @param parentOrganId 当前组织机构的ID
	 * @param allChildren 当allChildren = false ,仅返回当前组织机构的直接子组织机构 ; 当allChildren = true ,则返回当前机构的所有子孙组织机构
	 * @return
	 */
	public List<Organization> findChildOrgans(String parentOrganId,
			boolean allChildren);
	
	/**
	 * 根据ID获得模块实体
	 * @param moduleId 模块ID
	 * @return 模块实体
	 */
	public Organization getOrganById(String organId);

	/**
	 * 根据module对象获得父模块实体
	 * @param module 
	 * @return 父模块实体
	 */
	public Organization getParentOrgan(Organization organ);
	
	/**
	 * 根据moduleId获得父模块实体
	 * @param moduleId 
	 * @return 父模块实体
	 */
	public Organization getParentOrgan(String organId);
	
}
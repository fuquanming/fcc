package com.fcc.web.sys.service;

import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Role;
/**
 * <p>Description:系统角色</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface RoleService {
	/**
	 * 保存角色
	 * @param role
	 */
	public void create(Role role);
	/**
	 * 保存角色及权限
	 * @param role
	 * @param moduleRight
	 * @throws RefusedException
	 */
	public void create(Role role, String[] moduleRight) throws RefusedException;
	/**
	 * 更新角色及权限
	 * @param role
	 * @param moduleRight
	 * @throws RefusedException
	 */
	public void update(Role role, String[] moduleRight) throws RefusedException;

	/**
	 * 删除角色及权限
	 * @param roleId		角色ID
	 */
	public void delete(String[] roleIds) throws Exception;
	
	/**
	 * 获取角色对象
	 * @param roleId
	 * @return
	 */
	public Role getRole(String roleId);
	/**
	 * 获取角色及权限
	 * @param roleId
	 * @return
	 */
	public Role getRoleWithModuleRight(String roleId);
	/**
	 * 分页查询角色
	 * @param pageNo
	 * @param pageSize
	 * @param param
	 * @return
	 */
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);
	
}
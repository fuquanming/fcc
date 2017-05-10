package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.Role;

public interface RoleService {

    /**
     * 新增角色
     * @param role
     */
    void add(Role role);

    /**
     * 新增角色
     * @param role
     * @param moduleRight
     * @throws RefusedException
     */
    void add(Role role, String[] moduleRight) throws RefusedException;

    /**
     * 修改角色
     * @param role
     * @param moduleRight
     * @throws RefusedException
     */
    void edit(Role role, String[] moduleRight) throws RefusedException;

    /**
     * 删除角色
     * @param roleIds
     * @throws Exception
     */
    void delete(String[] roleIds) throws Exception;

    /**
     * 获取角色
     * @param roleId
     * @return
     */
    Role getRole(String roleId);

    /**
     * 获取角色
     * @param roleId
     * @return
     */
    Role getRoleWithModuleRight(String roleId);
    
    /**
     * 通过用户类型获取角色
     * @param userType
     * @return
     */
    List<Role> getRoleByUserType(String userType);
    /**
     * 分页查询角色
     * @param pageNo
     * @param pageSize
     * @param param
     * @return
     */
    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

}
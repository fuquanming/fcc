package com.fcc.web.sys.service;

import java.util.Collection;
import java.util.List;

import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.RoleModuleRight;

/**
 * <p>Description:系统模块操作权限</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface RoleModuleRightService {

    public List<RoleModuleRight> getModuleRightByModuleId(String moduleId);

    public List<RoleModuleRight> getModuleRightByModuleIds(String[] moduleIds);

    /**
     * 通过角色ID获取角色模块操作权限
     * @param roleIds
     * @return
     */
    public List<RoleModuleRight> getModuleRightByRoleId(Collection<String> roleIds);

    /**
     * 根据角色ID和模块ID获取相应的操作权值
     * @param role_id CPK
     * @param module_id CPK
     * @return
     */
    public RoleModuleRight getModuleRightByKey(String roleId, String moduleId);

    /**
     * 取得所有RoleModuleRight
     * @return
     */
    public List<RoleModuleRight> getModuleRight();

    /**
     * 清除角色相关的所有操作权值
     * @param role_id
     * @return
     */
    public void deleteModuleRightByRole(String roleId);

    public void updateModuleRight(String roleId, String moduleId, Long right_value);

    public void createRight(String roleId, String[] arModuleRight) throws RefusedException;

    public List<RoleModuleRight> getRoleModuleRight(String roleId);

}
package com.fcc.web.sys.service;

import java.util.List;

import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.RoleModuleRight;
/**
 * 权限值
 * 
 * @version 
 * @author 傅泉明
 */
public interface RoleModuleRightService {
    /**
     * 获取权限通过模块ID
     * @param moduleId
     * @return
     */
    List<RoleModuleRight> getRightByModuleId(String moduleId);
    /**
     * 获取所有权限
     * @return
     */
    List<RoleModuleRight> getRoleModuleRights();
    /**
     * 删除权限通过角色ID
     * @param roleId
     */
    void deleteRightByRole(String roleId);
    /**
     * 修改权限
     * @param roleId        角色ID
     * @param moduleId      模块ID
     * @param rightValue    权限值
     */
    void editRight(String roleId, String moduleId, Long rightValue);
    /**
     * 新增权限
     * @param roleId            角色ID
     * @param moduleRight       模块权限值
     * @throws RefusedException
     */
    void addRight(String roleId, String[] moduleRight) throws RefusedException;
    /**
     * 获取权限值通过角色ID
     * @param roleId
     * @return
     */
    List<RoleModuleRight> getRoleModuleRight(String roleId);
}
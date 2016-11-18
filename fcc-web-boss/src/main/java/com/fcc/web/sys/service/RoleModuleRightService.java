package com.fcc.web.sys.service;

import java.util.Collection;
import java.util.List;

import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.RoleModuleRight;

public interface RoleModuleRightService {

    List<RoleModuleRight> getModuleRightByModuleId(String moduleId);

    List<RoleModuleRight> getModuleRightByModuleIds(String[] moduleIds);

    List<RoleModuleRight> getModuleRightByRoleId(Collection<String> roleIds);

    RoleModuleRight getModuleRightByKey(String roleId, String moduleId);

    List<RoleModuleRight> getRoleModuleRights();

    void deleteModuleRightByRole(String roleId);

    void updateModuleRight(String roleId, String moduleId, Long rightValue);

    void createRight(String roleId, String[] moduleRight) throws RefusedException;

    List<RoleModuleRight> getRoleModuleRight(String roleId);

}
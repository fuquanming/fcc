package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.SysUser;

public interface SysUserService {

    void create(SysUser data, String[] roleIds);

    void createRole(String userId, String[] roleIds);

    void update(SysUser data, String[] roleIds);

    void update(SysUser data);

    void resetPassword(String[] userIds, String userPass);

    void delete(String[] userIds);

    Integer updateStatus(String[] userIds, String userStatus);

    SysUser getLoninUser(String userId, String password) throws RefusedException;

    SysUser getUserWithRole(String userId);

    Integer updatePassword(String userId, String newPassword);

    List<SysUser> getUserByRoleIds(List<String> roleIdList);

    SysUser findByUsername(String userName);

    Set<String> findStringRoles(SysUser user);

    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

    /**
     * 用户菜单 
     * @param sysUser
     * @return
     */
    List<EasyuiTreeNode> getSysUserMenu(SysUser sysUser);

}
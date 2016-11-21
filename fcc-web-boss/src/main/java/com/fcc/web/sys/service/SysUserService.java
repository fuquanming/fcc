package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.SysUser;

public interface SysUserService {

    void add(SysUser data, String[] roleIds);

    void addRole(String userId, String[] roleIds);

    void edit(SysUser data, String[] roleIds);

    void edit(SysUser data);

    void resetPassword(String[] userIds, String userPass);

    void delete(String[] userIds);

    Integer editStatus(String[] userIds, String userStatus);

    SysUser getLoninUser(String userId, String password) throws RefusedException;

    SysUser getUserWithRole(String userId);

    Integer updatePassword(String userId, String newPassword);

    List<SysUser> getUserByRoleIds(List<String> roleIdList);

    SysUser findByUsername(String userName);

    Set<String> findStringRoles(SysUser user);

    ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

    /**
     * 用户模块
     * @param sysUser
     * @return
     */
    TreeSet<Module> getSysUserModule(SysUser sysUser);
    /**
     * 用户菜单 
     * @param sysUser
     * @return
     */
    List<EasyuiTreeNode> getSysUserMenu(SysUser sysUser);

}
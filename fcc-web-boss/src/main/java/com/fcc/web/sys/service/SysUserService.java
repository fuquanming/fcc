package com.fcc.web.sys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.SysUser;

/**
 * <p>Description:系统用户</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface SysUserService {

    /**
     * 保存用户及权限
     * @param data
     * @param operateIds
     */
    public void create(SysUser data, String[] roleIds);

    /**
     * 保存用户权限
     * @param moduleId
     * @param oIds
     */
    public void createRole(String userId, String[] roleIds);

    /**
     * 更新用户及权限
     * @param data
     * @param operateIds
     */
    public void update(SysUser data, String[] roleIds);

    /**
     * 更新用户
     * @param data
     */
    public void update(SysUser data);

    /**
     * 重置密码，使用该创建者
     * @param userId
     */
    public void resetPassword(String[] userIds, String userPass);

    /**
     * 删除用户及用户的角色，使用该创建者
     * @param userId
     */
    public void delete(String[] userIds);

    /**
     * 修改用户状态
     * @param userId
     */
    public Integer updateStatus(String[] userId, String userStatus);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    public Integer updatePassword(String userId, String newPassword);

    /**
     * 用户登入系统
     * @param userId
     * @param password
     * @return
     * @throws RefusedException
     */
    public SysUser getLoninUser(String userId, String password) throws RefusedException;

    /**
     * 取得用户的信息及角色
     * @param userId
     * @return
     */
    public SysUser getUserWithRole(String userId);

    /**
     * 取得用户信息通过角色ID
     * @param roleIds
     * @return
     */
    public List<SysUser> getUserByRoleIds(List<String> roleIdList);

    /**
     * 
     * @param userName
     * @return
     */
    public SysUser findByUsername(String userName);

    /**
     * 查找用户角色ID
     * @param user
     * @return
     */
    public Set<String> findStringRoles(SysUser user);
    /**
     * 分页查询用户
     * @param pageNo
     * @param pageSize
     * @param param
     * @return
     */
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param);

}

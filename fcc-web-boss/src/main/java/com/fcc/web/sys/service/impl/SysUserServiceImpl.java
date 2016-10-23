package com.fcc.web.sys.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.dao.RoleDao;
import com.fcc.web.sys.dao.SysUserDao;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
/**
 * 
 * @author 傅泉明
 *
 * 2010-7-23
 */
@Transactional
@Service
public class SysUserServiceImpl implements SysUserService {
	
    @Resource
	private BaseService baseService;
    @Resource
    private RoleDao roleDao;
    @Resource
    private SysUserDao sysUserDao;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void create(SysUser data, String[] roleIds) {
	    baseService.create(data);
		createRole(data.getUserId(), roleIds);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void createRole(String userId, String[] roleIds) {
		// 删除所有操作
		roleDao.deleteUserRoleByUserId(userId);
    	if (roleIds == null || roleIds.length == 0) {
    	} else {// 更新操作
    	    roleDao.createUserRole(userId, roleIds);
    	}
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void update(SysUser data, String[] roleIds) {
		update(data);
		createRole(data.getUserId(), roleIds);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void update(SysUser data) {
	    sysUserDao.update(data);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void resetPassword(String[] userIds, String userPass) {
	    sysUserDao.resetPassword(userIds, userPass);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void delete(String[] userIds) {
	    sysUserDao.deleteUser(userIds);
	    roleDao.deleteUserRoleByUserId(userIds);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer updateStatus(String[] userIds, String userStatus) {
	    return sysUserDao.updateStatus(userIds, userStatus);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public SysUser getLoninUser(String userId, String password) throws RefusedException {
		SysUser sysUser = (SysUser) baseService.get(SysUser.class, userId);
		if (sysUser == null) {
			throw new RefusedException("用户不存在！");
		} else {
			if (!sysUser.getPassword().equals(password)) {
				throw new RefusedException("用户或密码错误！");
			}
			sysUser.getRoles().size();
		}
		return sysUser;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)//只查事务申明
	public SysUser getUserWithRole(String userId) {
		SysUser sysUser = (SysUser) baseService.get(SysUser.class, userId);
		if (sysUser != null) {
			sysUser.getRoles().size();
		}
		return sysUser;
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public Integer updatePassword(String userId, String newPassword) {
		return sysUserDao.updatePassword(userId, newPassword);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public List<SysUser> getUserByRoleIds(List<String> roleIdList) {
		return sysUserDao.getUserByRoleIds(roleIdList);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public SysUser findByUsername(String userName) {
	    return sysUserDao.findByUsername(userName);
	}
	
	@Override
	public Set<String> findStringRoles(SysUser user) {
	    Set<String> roleIds = user.getRoleIds();
	    if (roleIds == null) {
	        Set<Role> roles = user.getRoles();
	        roleIds = Sets.newHashSet(Collections2.transform(roles, new Function<Role, String>() {
	            @Override
	            public String apply(Role input) {
	                return input.getRoleId();
	            }
	        }));
	    }
	    return roleIds;
	}

	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
		return sysUserDao.queryPage(pageNo, pageSize, param);
	}
}

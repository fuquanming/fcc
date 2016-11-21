package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.dao.RoleDao;
import com.fcc.web.sys.dao.SysUserDao;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
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
@Service
public class SysUserServiceImpl implements SysUserService {
	
    @Resource
	private BaseService baseService;
    @Resource
    private RoleDao roleDao;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private CacheService cacheService;
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#add(com.fcc.web.sys.model.SysUser, java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void add(SysUser data, String[] roleIds) {
	    baseService.create(data);
		addRole(data.getUserId(), roleIds);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#addRole(java.lang.String, java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void addRole(String userId, String[] roleIds) {
		// 删除所有操作
		roleDao.deleteRoleByUserId(userId);
    	if (roleIds == null || roleIds.length == 0) {
    	} else {// 更新操作
    	    roleDao.addRole(userId, roleIds);
    	}
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#edit(com.fcc.web.sys.model.SysUser, java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void edit(SysUser data, String[] roleIds) {
		edit(data);
		addRole(data.getUserId(), roleIds);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#edit(com.fcc.web.sys.model.SysUser)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void edit(SysUser data) {
	    sysUserDao.update(data);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#resetPassword(java.lang.String[], java.lang.String)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void resetPassword(String[] userIds, String userPass) {
	    sysUserDao.resetPassword(userIds, userPass);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#delete(java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void delete(String[] userIds) {
	    sysUserDao.deleteUser(userIds);
	    roleDao.deleteRoleByUserId(userIds);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#editStatus(java.lang.String[], java.lang.String)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer editStatus(String[] userIds, String userStatus) {
	    return sysUserDao.updateStatus(userIds, userStatus);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#getLoninUser(java.lang.String, java.lang.String)
     **/
	@Override
    @Transactional(readOnly = true)
	public SysUser getLoninUser(String userId, String password) throws RefusedException {
		SysUser sysUser = (SysUser) baseService.get(SysUser.class, userId);
		if (sysUser == null) {
			throw new RefusedException(Constants.StatusCode.Login.errorUserName);
		} else {
			if (!sysUser.getPassword().equals(password)) {
				throw new RefusedException(Constants.StatusCode.Login.errorPassword);
			}
			sysUser.getRoles().size();
		}
		return sysUser;
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#getUserWithRole(java.lang.String)
     **/
	@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SysUser getUserWithRole(String userId) {
		SysUser sysUser = (SysUser) baseService.get(SysUser.class, userId);
		if (sysUser != null) {
			sysUser.getRoles().size();
		}
		return sysUser;
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#updatePassword(java.lang.String, java.lang.String)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer updatePassword(String userId, String newPassword) {
		return sysUserDao.updatePassword(userId, newPassword);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#getUserByRoleIds(java.util.List)
     **/
	@Override
    @Transactional(readOnly = true)
	public List<SysUser> getUserByRoleIds(List<String> roleIdList) {
		return sysUserDao.getUserByRoleIds(roleIdList);
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#findByUsername(java.lang.String)
     **/
	@Override
    @Transactional(readOnly = true)
	
	public SysUser findByUsername(String userName) {
	    return sysUserDao.findByUsername(userName);
	}
	
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#findStringRoles(com.fcc.web.sys.model.SysUser)
     **/
	@Override
    public Set<String> findStringRoles(SysUser user) {
	    Set<String> roleIds = user.getRoleIds();
	    if (roleIds == null) {
	        Set<Role> roles = user.getRoles();
	        roleIds = Sets.newHashSet(Collections2.transform(roles, new Function<Role, String>() {
	            public String apply(Role input) {
	                return input.getRoleId();
	            }
	        }));
	    }
	    return roleIds;
	}

	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#queryPage(int, int, java.util.Map)
     **/
	@Override
    @Transactional(readOnly = true)
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
		return sysUserDao.queryPage(pageNo, pageSize, param);
	}
	
	@Override
	public TreeSet<Module> getSysUserModule(SysUser sysUser) {
	    Set<Role> roleSet = sysUser.getRoles();// 角色集合
	    TreeSet<Module> moduleSet = new TreeSet<Module>();// 模块集合
        for (Role role : roleSet) {
            String roleId = role.getRoleId();
            Map<String, RoleModuleRight> rightMap = cacheService.getRoleModuleRightMap().get(roleId);
            Set<String> moduleIdSet = rightMap.keySet();
            for (String moduleId : moduleIdSet) {
                Module module = cacheService.getModuleMap().get(moduleId);
                moduleSet.add(module);
                if (!Module.ROOT.getModuleId().equals(module.getParentId())) {
                    moduleSet.add(cacheService.getModuleMap().get(module.getParentId()));
                }
            }
        }
	    return moduleSet;
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#getSysUserMenu(com.fcc.web.sys.model.SysUser)
     **/
	@Override
    public List<EasyuiTreeNode> getSysUserMenu(SysUser sysUser) {
	    Set<Module> moduleSet = getSysUserModule(sysUser);
	    Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>(moduleSet.size());
	    List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
	    for (Module m : moduleSet) {
            EasyuiTreeNode node = new EasyuiTreeNode();
            node.setId(m.getModuleId());
            node.setText(m.getModuleName());
            String desc = m.getModuleDesc();
            if (desc != null && !"".equals(desc)) {
                Map<String, Object> attributes = new HashMap<String, Object>(1);
                attributes.put("src", desc);
                node.setAttributes(attributes);
            }
            nodeMap.put(node.getId(), node);
            
            String parendId = m.getParentId();
            
            EasyuiTreeNode cacheNode = nodeMap.get(parendId);
            if (cacheNode != null) {
                List<EasyuiTreeNode> children = cacheNode.getChildren();
                if (children == null) children = new ArrayList<EasyuiTreeNode>();
                children.add(node);
                cacheNode.setChildren(children);
            } else {
                // 添加一级菜单
                nodeList.add(node);
            }
        }
	    return nodeList;
	}
}

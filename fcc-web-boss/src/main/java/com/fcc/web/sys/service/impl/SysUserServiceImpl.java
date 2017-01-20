package com.fcc.web.sys.service.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.coder.Coder;
import com.fcc.commons.coder.CoderEnum;
import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.dao.TreeableDao;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.dao.RoleDao;
import com.fcc.web.sys.dao.SysKeyDao;
import com.fcc.web.sys.dao.SysUserDao;
import com.fcc.web.sys.enums.SyskeyType;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysDict;
import com.fcc.web.sys.model.SysKey;
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
    private SysKeyDao sysKeyDao;
    @Resource
    private TreeableDao treeableDao;
    @Resource
    private CacheService cacheService;
	
    @Override
    @Transactional(readOnly = true)
    public String getDefaultUserPass() {
        String userPass = null;
        SysDict sysDict = (SysDict)treeableDao.getTreeableByName(SysDict.class, SyskeyType.userPassword.name());
        if (sysDict != null) {
            userPass = sysDict.getNodeCode();
        } else {
            userPass = Constants.defaultUserPass;
        }
        return userPass;
    }
    /**
     * 数据库获取Key 
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private Key getKey(String userId) {
        Key key = null;
        Map<String, Object> param = new HashMap<String, Object>(2);
        param.put("linkType", SyskeyType.userPassword.name());
        param.put("linkId", userId);
        List<SysKey> list = sysKeyDao.query(1, 1, param, false);
        if (list.size() > 0) {
            SysKey sysKey = list.get(0);
            key = Coder.byteToKey(CoderEnum.DES3, EncryptionUtil.decodeBase64(sysKey.getKeyValue()));
        } else {
            SysKey sysKey = new SysKey();
            sysKey.setLinkId(userId);
            sysKey.setLinkType(SyskeyType.userPassword.name());
            key = EncryptionUtil.getKey3DES(userId + RandomStringUtils.random(5, true, true), Constants.ENCODING);
            sysKey.setKeyValue(EncryptionUtil.encodeBase64(key.getEncoded()));
            baseService.add(sysKey);
        }
        return key;
    }
    
    private String getEncodePass(Key key, String userPass) {
        if (userPass == null || "".equals(userPass)) userPass = getDefaultUserPass();
        return EncryptionUtil.encodeMD5(EncryptionUtil.encrypt3DES(key, userPass, Constants.ENCODING));
    }
    
    
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.SysUserService#add(com.fcc.web.sys.model.SysUser, java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void add(SysUser data, String[] roleIds) {
	    // 获取密码
	    SysKey sysKey = new SysKey();
	    sysKey.setLinkId(data.getUserId());
	    sysKey.setLinkType(SyskeyType.userPassword.name());
	    Key key = EncryptionUtil.getKey3DES(data.getUserId() + RandomStringUtils.random(5, true, true), Constants.ENCODING);
	    sysKey.setKeyValue(EncryptionUtil.encodeBase64(key.getEncoded()));
	    
	    baseService.add(sysKey);
	    data.setPassword(getEncodePass(key, null));
	    baseService.add(data);
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
	public void resetPassword(String userId, String userPass) {
        userPass = getEncodePass(getKey(userId), userPass);
        sysUserDao.resetPassword(userId, userPass);
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
	    sysKeyDao.delete(SyskeyType.userPassword.name(), userIds);
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
		    // 判断密码
		    System.out.println(getEncodePass(getKey(userId), password));
			if (!getEncodePass(getKey(userId), password).equalsIgnoreCase(sysUser.getPassword())) {
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
                if (module.getModuleStatus()) {
                    Module parentModule = cacheService.getModuleMap().get(module.getParentId());
                    if (parentModule != null && parentModule.getModuleStatus() == true) {
                        moduleSet.add(parentModule);
                        moduleSet.add(module);
                    } else if (Module.ROOT.getModuleId().equals(module.getParentId())) {
                        moduleSet.add(module);
                    }
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

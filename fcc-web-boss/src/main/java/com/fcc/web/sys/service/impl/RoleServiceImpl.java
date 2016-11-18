package com.fcc.web.sys.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.dao.RoleDao;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;

/**
 * <p>Description:系统管理 角色</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private BaseService baseService;

    @Resource
    private RoleDao roleDao;
    @Resource
    private RoleModuleRightDao roleModuleRightDao;

    @Resource
    private RoleModuleRightService roleModuleRightService;

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#create(com.fcc.web.sys.model.Role)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void create(Role role) {
        baseService.create(role);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#create(com.fcc.web.sys.model.Role, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void create(Role role, String[] moduleRight) throws RefusedException {
        baseService.create(role);
        roleModuleRightService.createRight(role.getRoleId(), moduleRight);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#update(com.fcc.web.sys.model.Role, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void update(Role role, String[] moduleRight) throws RefusedException {
        baseService.update(role);
        roleModuleRightService.createRight(role.getRoleId(), moduleRight);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#delete(java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void delete(String[] roleIds) throws Exception {
        if (roleIds != null && roleIds.length > 0) {
            roleModuleRightDao.deleteByRoleIds(roleIds);
            roleDao.delete(roleIds);
            roleDao.deleteUserRoleByRoleId(roleIds);
        }
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#getRole(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
    //只查事务申明
    public Role getRole(String roleId) {
        Role role = null;
        if (Role.ROOT.getRoleId().equals(roleId)) {
            role = Role.ROOT;
        } else {
            role = (Role) baseService.get(Role.class, roleId);
        }
        return role;
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#getRoleWithModuleRight(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
    //只查事务申明
    public Role getRoleWithModuleRight(String roleId) {
        Role role = getRole(roleId);
        if (role != null) {
            List<RoleModuleRight> dataList = roleModuleRightService.getRoleModuleRight(roleId);
            role.setRoleModuleRights(new HashSet<RoleModuleRight>(dataList.size()));
            role.getRoleModuleRights().addAll(dataList);
        }
        return role;
    };

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleService#queryPage(int, int, java.util.Map)
     **/
    @Override
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
        return roleDao.queryPage(pageNo, pageSize, param);
    }
}

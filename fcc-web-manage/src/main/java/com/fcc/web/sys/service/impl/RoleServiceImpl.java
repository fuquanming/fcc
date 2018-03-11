package com.fcc.web.sys.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.dao.RoleDao;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.service.CacheService;
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
    
    @Resource
    private CacheService cacheService;

    /**
     * @see com.fcc.web.sys.service.RoleService#add(com.fcc.web.sys.model.Role)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Role role) {
        baseService.add(role);
    }

    /**
     * @see com.fcc.web.sys.service.RoleService#add(com.fcc.web.sys.model.Role, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Role role, String[] moduleRight) throws RefusedException {
        baseService.add(role);
        roleModuleRightService.addRight(role.getRoleId(), moduleRight);
    }

    /**
     * @see com.fcc.web.sys.service.RoleService#edit(com.fcc.web.sys.model.Role, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Role role, String[] moduleRight) throws RefusedException {
        baseService.edit(role);
        roleModuleRightService.addRight(role.getRoleId(), moduleRight);
    }

    /**
     * @see com.fcc.web.sys.service.RoleService#delete(java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] roleIds) throws Exception {
        if (roleIds != null && roleIds.length > 0) {
            roleModuleRightDao.deleteByRoleIds(roleIds);
            roleDao.delete(roleIds);
            roleDao.deleteRoleByRoleId(roleIds);
        }
    }

    /**
     * @see com.fcc.web.sys.service.RoleService#getRole(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
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
     * @see com.fcc.web.sys.service.RoleService#getRoleWithModuleRight(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
    public Role getRoleWithModuleRight(String roleId) {
        Role role = getRole(roleId);
        if (role != null) {
            List<RoleModuleRight> dataList = roleModuleRightService.getRoleModuleRight(roleId);
            role.setRoleModuleRights(new HashSet<RoleModuleRight>(dataList.size()));
            role.getRoleModuleRights().addAll(dataList);
        }
        return role;
    };
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoleByUserType(String userType) {
        return roleDao.getRoleByUserType(userType);
    }

    /**
     * @see com.fcc.web.sys.service.RoleService#queryPage(int, int, java.util.Map)
     **/
    @Override
    @Transactional(readOnly = true)
    public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
        return roleDao.queryPage(pageNo, pageSize, param);
    }

    @Override
    public String getRoleId(Map<String, List<String>> moduleRightMap) {
        String roleId = null;
        if (moduleRightMap != null) {
            Set<String> moduleIdSet = moduleRightMap.keySet();
            Map<String, RoleModuleRight> dataMap = new HashMap<String, RoleModuleRight>();
            for (Iterator<String> it = moduleIdSet.iterator(); it.hasNext();) {
                String moduleId = it.next();
                List<String> operateIdList = moduleRightMap.get(moduleId);
                RoleModuleRight right = new RoleModuleRight();
                right.setRoleId(roleId);
                right.setModuleId(moduleId);
                long rightVal = 0;
                for (String operateId : operateIdList) {
                    Operate operate = cacheService.getOperateMap().get(operateId);
                    if (operate != null) {
                        rightVal += operate.getOperateValue();
                    }
                }
                right.setRightValue(rightVal);
                dataMap.put(moduleId, right);
            }
            // 比较缓存的dataMap
            Map<String, Map<String, RoleModuleRight>> roleMap = cacheService.getRoleModuleRightMap();
            Set<String> roleSet = roleMap.keySet();
            for (Iterator<String> it = roleSet.iterator(); it.hasNext();) {
                String rId = it.next();
                Map<String, RoleModuleRight> roleRightMap = roleMap.get(rId);
                if (roleRightMap.size() != dataMap.size()) continue;// 模块数量不对
                Set<String> moduleSet = roleRightMap.keySet();
                boolean moduleFlag = true;
                for (Iterator<String> iterator = moduleSet.iterator(); iterator.hasNext();) {
                    String moduleId = iterator.next();
                    RoleModuleRight right = roleRightMap.get(moduleId);
                    // dataMap.moduleId
                    RoleModuleRight dataRight = dataMap.get(moduleId);
                    if (dataRight == null) {
                        moduleFlag = false;
                        break;// 缓存角色无该模块
                    }
                    if (right.getRightValue() != dataRight.getRightValue()) {
                        moduleFlag = false;
                        break;// 缓存角色无该模块权限
                    }
                }
                if (moduleFlag == false) continue;
                roleId = rId;
                break;
            }
            if (roleId == null) {
                if (dataMap.size() > 0) {
                    roleId = RandomStringUtils.random(10, true, true);
                    // 赋值
                    cacheService.getRoleModuleRightMap().put(roleId, dataMap);
                }
            }
        }
        return roleId;
    }
}
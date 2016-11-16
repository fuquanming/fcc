package com.fcc.web.sys.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.LoginService;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
@Service
public class BaseLoginServiceImpl implements LoginService {

    @Resource
    private RoleModuleRightService roleModuleRightService;
    @Resource
    private RoleService roleService;
    @Resource
    private SysUserService sysUserService;
    @Override
    public void postLogin(HttpServletRequest request) {
        
    }

    @Override
    public TreeSet<Module> getUserModule(SysUser sysUser) {
        Set<String> roleIds = sysUserService.findStringRoles(sysUser);
        
        List<RoleModuleRight> rightList = roleModuleRightService.getModuleRightByRoleId(roleIds);
        int rightSize = rightList.size();
        Set<String> moduleIdSet = new HashSet<String>(rightSize);
        // 角色模块权限
        Map<String, Map<String, Long>> roleModuleRightMap = new HashMap<String, Map<String,Long>>(rightSize);
        for (RoleModuleRight data : rightList) {
            String roleId = data.getRoleId();
            String moduleId = data.getModuleId();
            // 模块权限
            Map<String, Long> moduleRightMap = roleModuleRightMap.get(roleId);
            if (moduleRightMap == null) moduleRightMap = new HashMap<String, Long>();
            
            moduleRightMap.put(moduleId, data.getRightValue());
            roleModuleRightMap.put(roleId, moduleRightMap);
            
            moduleIdSet.add(moduleId);
        }
        
        return null;
    }

}

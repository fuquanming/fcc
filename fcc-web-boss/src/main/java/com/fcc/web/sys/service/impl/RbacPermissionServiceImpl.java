package com.fcc.web.sys.service.impl;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.service.RbacPermissionService;
import com.fcc.web.sys.service.RoleModuleRightService;

/**
 * <p>Description:RBAC权限许可类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class RbacPermissionServiceImpl implements RbacPermissionService {
	@Resource
	private RoleModuleRightService roleModuleRightService;
	@Resource
	private BaseService baseService;
	
//	private Module getModule(String moduleId) {
//	    Module module = (Module) baseService.get(Module.class, moduleId);
//	    if (module != null) module.getOperates().size();
//	    return module;
//	}
	
	/**
	 * 鉴权 数据库查询
	 * @param userRoleSet 用户的角色列表
	 * @param moduleId 模块ID
	 * @param operateId 操作ID
	 * @return
	 */
	public boolean checkPermission(Set<Role> userRoleSet, String moduleId, String operateId){
		if(userRoleSet == null || userRoleSet.size() == 0 
				|| moduleId == null || operateId == null 
				|| moduleId.equals("") || operateId.equals("")){
			return false;
		}
		
		//确认对应的模块是否有相关操作
		Module mod = CacheUtil.moduleMap.get(moduleId);
		if (mod == null) return false;
		//获取系统操作的对象
		Operate op = null;
		Iterator<Operate> it = mod.getOperates().iterator();
		while (it.hasNext()){
			Operate tmp = it.next();
			if(tmp.getOperateId().equals(operateId)){
				op = tmp;
				break;
			}
		}
		
		if(op != null){ //该模块的确拥有该操作
		    Iterator<Role> roleIt = userRoleSet.iterator();
			while (roleIt.hasNext()){
				Role role = roleIt.next();
				//获取角色-模块的权值
				RoleModuleRight rmr = roleModuleRightService.getModuleRightByKey(role.getRoleId() , moduleId);
				//进行权值&比较
				if(rmr != null){
					if((rmr.getRightValue() & op.getOperateValue()) > 0){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 鉴权 缓存查询
	 * @param userRoleSet 用户的角色列表
	 * @param moduleId 模块ID
	 * @param operateId 操作ID
	 * @return true 可以访问
	 */
	public boolean checkPermissionCache(Set<Role> userRoleSet, String moduleId, String operateId) {
		if(userRoleSet == null || userRoleSet.size() == 0 
				|| moduleId == null || operateId == null 
				|| moduleId.equals("") || operateId.equals("")){
			return false;
		}
		
		//确认对应的模块是否有相关操作
		Module mod = CacheUtil.moduleMap.get(moduleId);
//		Module mod = getModule(moduleId);
		if (mod == null) return false;
		//获取系统操作的对象
		Operate op = null;
		Iterator<Operate> it = mod.getOperates().iterator();
		while (it.hasNext()){
			Operate tmp = it.next();
			if(tmp.getOperateId().equals(operateId)){
				op = tmp;
				break;
			}
		}
		
		if(op != null){ //该模块的确拥有该操作
		    Iterator<Role> roleIt = userRoleSet.iterator();
			while (roleIt.hasNext()){
				Role role = roleIt.next();
				//获取角色-模块的权值
				Set<RoleModuleRight> rightSet = role.getRoleModuleRights();
				if (rightSet != null) {
					for (Iterator<RoleModuleRight> rightIt = rightSet.iterator(); rightIt.hasNext();) {
						RoleModuleRight rmr = rightIt.next();
						//进行权值&比较
						if(rmr != null && rmr.getModuleId().equals(moduleId)){
							if((rmr.getRightValue() & op.getOperateValue()) > 0){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}

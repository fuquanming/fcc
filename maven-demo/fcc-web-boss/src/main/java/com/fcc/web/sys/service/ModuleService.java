package com.fcc.web.sys.service;

import java.util.Collection;
import java.util.List;

import com.fcc.web.sys.model.Module;
/**
 * <p>Description:系统模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ModuleService {
	/**
	 * 保存模块及操作
	 * @param data
	 * @param operateIds
	 */
	public void add(Module data, String[] operateIds);
	/**
	 * 保存模块操作
	 * @param moduleId
	 * @param oIds
	 */
	public void addOperate(String moduleId, String[] operateIds);
	/**
	 * 更新模块及操作
	 * @param data
	 * @param operateIds
	 */
	public void edit(Module data, String[] operateIds);
	/**
	 * 删除模块
	 * 删除模块对于的所有角色
	 * 删除模块所有操作
	 * @param module
	 */
	public void delete(String moduleId);
	
	 /**
      * 根据ID获得模块实体
      * @param moduleId 模块ID
      * @return 模块实体
      */
     public Module getModuleById(String moduleId);
	
     
     
//	/**
//	 * 获取所有模块及操作
//	 * @return
//	 */
//	public List<Module> getModuleWithOperate();
//
//	/**
//	 * 返回当前模块的子模块
//	 * @param moduleID 当前组织的ID
//	
//	 * @return
//	 */
//	public List<Module> findChildModulesWithOperation(String moduleId);
//
//	/**
//	 * 获取带操作列表的权限对象
//	 * @param moduleID
//	 * @return
//	 */
//	public Module loadModuleWithOperate(String moduleId);
//	
//	// 模块修改
//	/**
//	 * 返回当前模块的信息
//	 * @param moduleID 当前模块的ID
//	
//	 * @return
//	 */
//	public List<Module> findModules(Collection<String> moduleIdList);
//	/**
//	 * 返回当前模块的子模块
//	 * @param parentModuleId 当前模块的ID
//	 * @param allChildren 当allChildren = false ,仅返回当前模块的直接子模块 ; 当allChildren = true ,则返回当前模块的所有子孙模块
//	 * @return
//	 */
//	public List<Module> findChildModules(String parentModuleId,
//			boolean allChildren);
//	

//
////	/**
////	 * 根据module对象获得父模块实体
////	 * @param module 
////	 * @return 父模块实体
////	 */
////	public Module getParentModule(Module module);
////	
////	/**
////	 * 根据moduleId获得父模块实体
////	 * @param moduleId 
////	 * @return 父模块实体
////	 */
////	public Module getParentModule(String moduleId);
//	
//	/**
//	 * 查询模块中地址
//	 */
//	public List<Module> getModuleByUrl();
}
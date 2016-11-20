package com.fcc.web.sys.service;

import java.util.HashMap;
import java.util.List;

import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;

public interface ModuleService {

    void add(Module data, String[] operateIds);

    void edit(Module data, String[] operateIds);

    void delete(String moduleId);

    void addOperate(String moduleId, String[] operateIds);

    Module getModuleById(String moduleId);

    List<Module> getModuleWithOperate();

    List<Module> findChildModulesWithOperation(String moduleId);

    Module loadModuleWithOperate(String moduleId);

    HashMap<String, Module> getAllModule();

    List<Module> getModuleByUrl();

    /**
     * 系统模块 
     * @param sysUser
     * @return
     */
    List<EasyuiTreeGridModule> getModuleTreeGrid(SysUser sysUser);
    
    /**
     * 系统模块
     * @param sysUser
     * @return
     */
    List<EasyuiTreeGridModule> getModuleTreeGrid(SysUser sysUser, String nodeStatus, Role role);
    
    /**
     * 取得模块通过用户
     * @param sysUser       用户
     * @param nodeStatus    节点状态
     * @param isOperate     是否输出模块操作
     * @param role          修改的角色
     * @return List<EasyuiTreeNode>
     */
    List<EasyuiTreeNode> getModuleTree(SysUser sysUser, String nodeStatus, boolean isOperate, Role role);

}
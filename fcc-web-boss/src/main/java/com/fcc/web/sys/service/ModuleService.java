package com.fcc.web.sys.service;

import java.util.List;

import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;

public interface ModuleService {
    /**
     * 保存模块
     * @param data          模块
     * @param operateIds    模块操作
     */
    void add(Module data, String[] operateIds);
    /**
     * 修改模块
     * @param data          模块
     * @param operateIds    模块操作
     */
    void edit(Module data, String[] operateIds);
    /**
     * 删除模块
     * @param moduleId
     */
    void delete(String moduleId);
    /**
     * 保存模块操作
     * @param moduleId      模块ID
     * @param operateIds    模块操作
     */
    void addOperate(String moduleId, String[] operateIds);
    /**
     * 更新模块是否显示
     * @param ids
     * @param moduleStatus
     */
    Integer editModuleStatus(String[] ids, boolean moduleStatus);
    /**
     * 获取模块通过ID
     * @param moduleId      模块ID
     * @return
     */
    Module getModuleById(String moduleId);
    /**
     * 获取所有模块及模块操作
     * @return
     */
    List<Module> getModuleWithOperate();
    /**
     * 查询模块及模块操作通过ID
     * @param moduleId      模块ID
     * @return
     */
    Module loadModuleWithOperate(String moduleId);
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
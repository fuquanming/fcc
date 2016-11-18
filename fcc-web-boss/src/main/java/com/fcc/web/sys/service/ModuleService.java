package com.fcc.web.sys.service;

import java.util.HashMap;
import java.util.List;

import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.web.sys.model.Module;
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
    List<EasyuiTreeGridModule> getMenu(SysUser sysUser);

}
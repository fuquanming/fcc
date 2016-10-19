package com.fcc.web.sys.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.dao.ModuleDao;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.service.ModuleService;

/**
 * <p>Description:系统模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private BaseService baseService;

    @Resource
    private ModuleDao moduleDao;

    @Resource
    private RoleModuleRightDao roleModuleRightDao;

    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void create(Module data, String[] operateIds) {
        baseService.create(data);
        createOperate(data.getModuleId(), operateIds);
    }

    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void update(Module data, String[] operateIds) {
        baseService.update(data);
        createOperate(data.getModuleId(), operateIds);
    }

    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void delete(String moduleId) {
        // 删除模块
        moduleDao.delete(moduleId, true);
        // 删除模块操作
        moduleDao.deleteModuleOperate(moduleId, true);
        // 删除模块对于的所有角色
        roleModuleRightDao.deleteByModuleId(moduleId);
    }

    @Transactional(rollbackFor = Exception.class)
    //事务申明
    public void createOperate(String moduleId, String[] operateIds) {
        // 删除所有操作
        moduleDao.deleteModuleOperate(moduleId, false);
        if (operateIds == null || operateIds.length == 0) {
        } else {// 更新操作
            moduleDao.createModuleOperate(moduleId, operateIds);
        }
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<Module> getModuleWithOperate() {
        return moduleDao.getModuleWithOperate();
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<Module> findChildModulesWithOperation(String moduleId) {
        return moduleDao.findChildModulesWithOperation(moduleId);
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public Module loadModuleWithOperate(String moduleId) {
        Module theModule = null;
        if (moduleId != null) {
            theModule = (Module) baseService.get(Module.class, moduleId);
            if (theModule != null) {
                theModule.getOperates().size();
            }
        }
        return theModule;
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public HashMap<String, Module> getAllModule() {
        List<Module> list = baseService.getAll(Module.class);
        HashMap<String, Module> map = new HashMap<String, Module>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Module m = list.get(i);
            m.getOperates().size();
            map.put(m.getModuleId(), m);
        }
        list.clear();
        return map;
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<Module> findModules(List<String> moduleIdList) {
       return moduleDao.findModules(moduleIdList);
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<Module> findChildModules(String parentModuleId, boolean allChildren) {
        return moduleDao.findChildModules(parentModuleId, allChildren);
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public Module getModuleById(String moduleId) {
        Module module = null;
        if (moduleId != null) {
            if (Module.ROOT.getModuleId().equals(moduleId)) {
                module = Module.ROOT;
            } else {
                module = (Module) baseService.get(Module.class, moduleId);
            }
        }
        return module;
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public Module getParentModule(Module module) {
        Module parentModule = null;
        if (module != null) {
            parentModule = getParentModule(module.getModuleId());
        }
        return parentModule;
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public Module getParentModule(String moduleId) {
        Module parentModule = null;
        if (moduleId != null) {
            String parentModuleId = Module.getModuleParentId(moduleId);
            parentModule = getModuleById(parentModuleId);
        }
        return parentModule;
    }

    @Transactional(readOnly = true)
    //只查事务申明
    public List<Module> getModuleByUrl() {
        return moduleDao.getModuleByUrl();
    }
}

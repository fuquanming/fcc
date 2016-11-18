package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.dao.ModuleDao;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
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
    @Resource
    private CacheService cacheService;

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#add(com.fcc.web.sys.model.Module, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Module data, String[] operateIds) {
        baseService.create(data);
        addOperate(data.getModuleId(), operateIds);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#edit(com.fcc.web.sys.model.Module, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Module data, String[] operateIds) {
        baseService.update(data);
        addOperate(data.getModuleId(), operateIds);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#delete(java.lang.String)
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String moduleId) {
        // 删除模块
        moduleDao.delete(moduleId, true);
        // 删除模块操作
        moduleDao.deleteModuleOperate(moduleId, true);
        // 删除模块对于的所有角色
        roleModuleRightDao.deleteByModuleId(moduleId);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#addOperate(java.lang.String, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOperate(String moduleId, String[] operateIds) {
        // 删除所有操作
        moduleDao.deleteModuleOperate(moduleId, false);
        if (operateIds == null || operateIds.length == 0) {
        } else {// 更新操作
            moduleDao.createModuleOperate(moduleId, operateIds);
        }
    }
    
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#getModuleById(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
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

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#getModuleWithOperate()
     **/
    @Override
    @Transactional(readOnly = true)
    public List<Module> getModuleWithOperate() {
        return moduleDao.getModuleWithOperate();
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#findChildModulesWithOperation(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
    public List<Module> findChildModulesWithOperation(String moduleId) {
        return moduleDao.findChildModulesWithOperation(moduleId);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#loadModuleWithOperate(java.lang.String)
     **/
    @Override
    @Transactional(readOnly = true)
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

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#getAllModule()
     **/
    @Override
    @Transactional(readOnly = true)
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

//    @Transactional(readOnly = true) //只查事务申明
//    
//    public List<Module> findModules(Collection<String> moduleIdList) {
//       return moduleDao.findModules(moduleIdList);
//    }
//
//    @Transactional(readOnly = true) //只查事务申明
//    
//    public List<Module> findChildModules(String parentModuleId, boolean allChildren) {
//        return moduleDao.findChildModules(parentModuleId, allChildren);
//    }


//    @Transactional(readOnly = true)
//    //只查事务申明
//    public Module getParentModule(Module module) {
//        Module parentModule = null;
//        if (module != null) {
//            parentModule = getParentModule(module.getModuleId());
//        }
//        return parentModule;
//    }
//
//    @Transactional(readOnly = true)
//    //只查事务申明
//    public Module getParentModule(String moduleId) {
//        Module parentModule = null;
//        if (moduleId != null) {
//            String parentModuleId = Module.getModuleParentId(moduleId);
//            parentModule = getModuleById(parentModuleId);
//        }
//        return parentModule;
//    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#getModuleByUrl()
     **/
    @Override
    @Transactional(readOnly = true)
    public List<Module> getModuleByUrl() {
        return moduleDao.getModuleByUrl();
    }
    
    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#getMenu(com.fcc.web.sys.model.SysUser)
     **/
    @Override
    public List<EasyuiTreeGridModule> getMenu(SysUser sysUser) {
        List<EasyuiTreeGridModule> nodeList = new ArrayList<EasyuiTreeGridModule>();
        TreeSet<Module> menuSet = new TreeSet<Module>();
        menuSet.addAll(cacheService.getModuleMap().values());
        Map<String, EasyuiTreeGridModule> nodeMap = new HashMap<String, EasyuiTreeGridModule>();
        for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
            Module m = it.next();
            String moduleDesc = m.getModuleDesc();
            if (moduleDesc == null || "".equals(moduleDesc)) {// 没有地址的
            } else {
                // 只能获取本身拥有的模块权限
//                  if (!menuSetCache.contains(m)) {
//                      continue;
//                  }
            }
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setId(m.getModuleId());
            node.setText(m.getModuleName());
            node.setModuleDesc(moduleDesc);
            node.setModuleSort(m.getModuleSort());
            Set<Operate> operateSet = m.getOperates();
            if (operateSet != null && operateSet.size() > 0) {
                StringBuilder idSb = new StringBuilder(50);
                StringBuilder nameSb = new StringBuilder(50);
                for (Operate o : operateSet) {
                    idSb.append(o.getOperateId()).append(",");
                    nameSb.append(o.getOperateName()).append(",");
                }
                String operateIds = idSb.toString();
                String operateNames = nameSb.toString();
                if (operateIds.length() > 0) operateIds = operateIds.substring(0, operateIds.length() - 1);
                if (operateNames.length() > 0) operateNames = operateNames.substring(0, operateNames.length() - 1);
                node.setOperateIds(operateIds);
                node.setOperateNames(operateNames);
            }
            nodeMap.put(node.getId(), node);
            
            String parendId = m.getParentId();
            EasyuiTreeGridModule cacheNode = nodeMap.get(parendId);
            if (cacheNode != null) {
                List<EasyuiTreeNode> children = cacheNode.getChildren();
                if (children == null) children = new ArrayList<EasyuiTreeNode>();
                children.add(node);
                cacheNode.setChildren(children);
            } else {
                nodeList.add(node);// 移除根目录后，添加节点
            }
        }
        return nodeList;
    }
}

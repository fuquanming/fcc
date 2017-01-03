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
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.ModuleService;
import com.fcc.web.sys.service.RbacPermissionService;
import com.fcc.web.sys.service.SysUserService;

/**
 * <p>Description:系统模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
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
    @Resource
    private SysUserService sysUserService;
    @Resource
    private RbacPermissionService rbacPermissionService;

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#add(com.fcc.web.sys.model.Module, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Module data, String[] operateIds) {
        baseService.add(data);
        addOperate(data.getModuleId(), operateIds);
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.ModuleService#edit(com.fcc.web.sys.model.Module, java.lang.String[])
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Module data, String[] operateIds) {
        baseService.edit(data);
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
        moduleDao.deleteOperate(moduleId, true);
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
        moduleDao.deleteOperate(moduleId, false);
        if (operateIds == null || operateIds.length == 0) {
        } else {// 更新操作
            moduleDao.addOperate(moduleId, operateIds);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer editModuleStatus(String[] ids, boolean moduleStatus) {
        return moduleDao.editModuleStatus(ids, moduleStatus);
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

    @Override
    public List<EasyuiTreeGridModule> getModuleTreeGrid(SysUser sysUser, String nodeStatus, Role role) {
        if (nodeStatus == null || "".equals(nodeStatus)) nodeStatus = EasyuiTreeNode.STATE_OPEN;
        
        List<EasyuiTreeGridModule> nodeList = new ArrayList<EasyuiTreeGridModule>();
        TreeSet<Module> menuSet = null;
        // 所有模块
        if (sysUser == null) {
            menuSet = new TreeSet<Module>();
            menuSet.addAll(cacheService.getModuleMap().values());
        } else {
            // 用户拥有的模块
            menuSet = sysUserService.getSysUserModule(sysUser);
        }
        Map<String, EasyuiTreeGridModule> nodeMap = new HashMap<String, EasyuiTreeGridModule>();
        for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
            Module m = it.next();
            String moduleDesc = m.getModuleDesc();
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setId(m.getModuleId());
            node.setText(m.getModuleName());
            node.setModuleDesc(moduleDesc);
            node.setModuleSort(m.getModuleSort());
            
            Map<String, Object> attributes = new HashMap<String, Object>(2);
            node.setAttributes(attributes);
            
            attributes.put("parentIds", m.getParentIds());
            attributes.put("show", m.getModuleStatus());
            
            Set<Operate> operateSet = m.getOperates();
            if (operateSet != null && operateSet.size() > 0) {
                // 判断用户拥有的模块
                if (sysUser != null) {
                    Set<Operate> userOperateSet = new TreeSet<Operate>();
                    for (Operate o : operateSet) {
                        boolean flag = rbacPermissionService.checkPermissionCache(sysUser.getRoles(), m.getModuleId(), o.getOperateId());
                        if (flag) {
                            // 判断角色是否选中
                            if (role != null) {
                                Map<String, RoleModuleRight> rightMap = cacheService.getRoleModuleRightMap().get(role.getRoleId());
                                if (rightMap != null) {
                                    RoleModuleRight right = rightMap.get(m.getModuleId());
                                    if (right != null && (right.getRightValue() & o.getOperateValue()) > 0) {
                                        // 选中
                                        attributes.put(o.getOperateId(), true);
                                    }
                                }
                            }
                            userOperateSet.add(o);
                        }
                    }
                    attributes.put("operate", userOperateSet);
                } else {
                    // 判断角色是否选中
                    for (Operate o : operateSet) {
                        if (role != null) {
                            Map<String, RoleModuleRight> rightMap = cacheService.getRoleModuleRightMap().get(role.getRoleId());
                            if (rightMap != null) {
                                RoleModuleRight right = rightMap.get(m.getModuleId());
                                if (right != null && (right.getRightValue() & o.getOperateValue()) > 0) {
                                    // 选中
                                    attributes.put(o.getOperateId(), true);
                                }
                            }
                        }
                    }
                    attributes.put("operate", operateSet);
                }
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
                node.setState(nodeStatus);
            }
        }
        return nodeList;
    }
    
    @Override
    public List<EasyuiTreeNode> getModuleTree(SysUser sysUser, String nodeStatus, boolean isOperate, Role role) {
        List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
        TreeSet<Module> menuSet = null;
        
        Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
        // 该用户权限
        // 是否过滤用户模块
        boolean checkUser = false;
        TreeSet<Module> moduleSet = null;
        if (sysUser != null) {
            checkUser = true;
            moduleSet = sysUserService.getSysUserModule(sysUser);
        } else {
            menuSet = new TreeSet<Module>();
            menuSet.addAll(cacheService.getModuleMap().values());
        }

        for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
            Module m = it.next();
            // 只能获取本身拥有的模块权限
            if (checkUser) {
                if (!moduleSet.contains(m)) {
                    continue;
                }
            }
            
            EasyuiTreeNode node = new EasyuiTreeNode();
            node.setId(m.getModuleId());
            node.setText(m.getModuleName());
            node.setState(nodeStatus);
            node.setAttributes(new HashMap<String, Object>());
            if (isOperate) {
                Set<Operate> operateSet = m.getOperates();
                if (operateSet != null && operateSet.size() > 0) {
                    for (Operate o : operateSet) {
                        // 只能获取本身拥有的操作权限
                        boolean operateFlag = true;
                        if (checkUser) {
                            operateFlag = rbacPermissionService.checkPermissionCache(sysUser.getRoles(), m.getModuleId(), o.getOperateId());
                        }
                        if (operateFlag) {
                            EasyuiTreeNode nodeOperate = new EasyuiTreeNode();
                            nodeOperate.setId(m.getModuleId() + ":" + o.getOperateId());
                            nodeOperate.setText(o.getOperateName());
                            nodeOperate.setAttributes(new HashMap<String, Object>());
                            nodeOperate.getAttributes().put("operateValue", o.getOperateValue() + "");
                            // 角色判断是否选择
                            if (role != null) {
                                RoleModuleRight right = cacheService.getRoleModuleRightMap().get(role.getRoleId()).get(m.getModuleId());
                                if (right == null || (right.getRightValue() & o.getOperateValue()) <= 0) {
                                    operateFlag = false; 
                                }
                            } else {
                                operateFlag = false;
                            }
                            nodeOperate.setChecked(operateFlag);
                            
                            List<EasyuiTreeNode> children = node.getChildren();
                            if (children == null) children = new ArrayList<EasyuiTreeNode>();
                            children.add(nodeOperate);
                            node.setChildren(children);
                        }
                    }
                }
            }
            nodeMap.put(node.getId(), node);
            String parendId = m.getParentId();
            EasyuiTreeNode cacheNode = nodeMap.get(parendId);
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

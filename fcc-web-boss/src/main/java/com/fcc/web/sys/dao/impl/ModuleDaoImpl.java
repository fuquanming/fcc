/*
 * @(#)ModuleDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.web.sys.dao.ModuleDao;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
@SuppressWarnings("unchecked")
@Repository
public class ModuleDaoImpl implements ModuleDao {

    @Resource
    private BaseDao baseDao;
    
    @Override
    public void addOperate(String moduleId, String[] operateIds) {
        int length = operateIds.length;
        for (int i = 0; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into sys_rbac_mod2op (module_id, operate_id, orderno) values('")
            .append(moduleId).append("','").append(operateIds[i]).append("',").append(i + 1).append(")");
            baseDao.executeSql(sb.toString());
        }
    }
    
    /**
     * 
     * @see com.fcc.web.sys.dao.ModuleDao#delete(java.lang.String)
     **/
    @Override
    public Integer delete(String moduleId, boolean isAll) {
        if (isAll) {
            return baseDao.executeHql("delete from Module where moduleId like ?", moduleId + "%");    
        } else {
            return baseDao.executeHql("delete from Module where moduleId = ?", moduleId);
        }
    }

    @Override
    public Integer deleteOperate(String moduleId, boolean isAll) {
        if (isAll) {
            return baseDao.executeSql("delete from sys_rbac_mod2op where module_Id like ?", moduleId + "%");
        } else {
            return baseDao.executeSql("delete from sys_rbac_mod2op where module_Id = ?", moduleId);
        }
    }
    
    @Override
    public Integer editShow(String[] ids, boolean show) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("moduleId", ids);
        params.put("show", show);
        return baseDao.executeHql("update Module set show=:show where moduleId in(:moduleId)", params);
    }
    
    @Override
    public List<Module> getModuleWithOperate() {
        List<Module> moduleList = new ArrayList<Module>();
        String bHql = "select m.module_id,m.module_name,m.module_level,m.module_sort,m.module_desc,m.parent_id,m.parent_ids,m.is_show,mo.operate_id from sys_rbac_module m left join sys_rbac_mod2op mo on m.module_id=mo.module_id order by module_level asc,module_sort asc,parent_Ids";
        Map<String, Object> param = null;
        List<Object> list = baseDao.findSQL(bHql, param);
        if (list.size() > 0) {
            List<Operate> operateList = baseDao.find("from Operate", param);
            Map<String, Operate> operateMap = new HashMap<String, Operate>();
            for (Operate data : operateList) {
                operateMap.put(data.getOperateId(), data);
            }
            
            int size = list.size();
            
            for (int i = 0; i < size; i++) {
                Object[] obj = (Object[])list.get(i);
                String moduleId = (String) obj[0];
                String moduleName = (String) obj[1];
                Integer moduleLevel = Integer.valueOf(obj[2].toString());
                Integer moduleSort = Integer.valueOf(obj[3].toString());
                String moduleDesc = (String) obj[4];
                String parentId = (String) obj[5];
                String parentIds = (String) obj[6];
                String show = obj[7].toString();
                String operateId = (String) obj[8];
                
                Module module = null;
                int moduleSize = moduleList.size();
                boolean isNew = true;
                if (moduleSize > 0) {
                    module = moduleList.get(moduleList.size() - 1);
                    if (module.getModuleId().equals(moduleId)) {
                        isNew = false;
                    }
                }
                if (isNew) {
                    module = new Module();
                    module.setModuleId(moduleId);
                    module.setModuleName(moduleName);
                    module.setModuleLevel(moduleLevel);
                    module.setModuleSort(moduleSort);
                    module.setModuleDesc(moduleDesc);
                    module.setParentId(parentId);
                    module.setParentIds(parentIds);
                    module.setShow("1".equals(show) ? true : false);
                    if (module.getOperates() == null) module.setOperates(new TreeSet<Operate>());
                    moduleList.add(module);
                }
                Operate operate = operateMap.get(operateId);
                if (operate != null) {
                    module.getOperates().add(operate);
                }
            }
        }
        return moduleList;
    }
    
//    @Override
//    public List<Module> findChildModules(String parentModuleId, boolean allChildren) {
//        if (parentModuleId != null) {
//            StringBuilder sb = new StringBuilder();
//            Map<String, Object> param = new HashMap<String, Object>();
//            sb.append("from Module where 1=1 ");
//            if (Module.ROOT.getModuleId().equals(parentModuleId)) {
//                if (allChildren) {
//                    // Do Nothing
//                } else {
//                    sb.append(" and moduleId not like:=moduleId ");
//                    param.put("moduleId", "%-%");
//                }
//            } else {
//                if (allChildren) {
//                    sb.append(" and moduleId like:=moduleId ");
//                    param.put("moduleId", parentModuleId + "-%");
//                } else {
//                    sb.append(" and moduleId like:=moduleId ");
//                    param.put("moduleId", parentModuleId + "-____");
//                }
//            }
//            sb.append(" order by moduleLevel asc, moduleSort asc");
//            return baseDao.find(sb.toString(), param);
//        }
//        return null;
//    }
}
/*
 * @(#)OrganizationDaoImpl.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.web.sys.dao.OrganizationDao;
import com.fcc.web.sys.model.Organization;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
@SuppressWarnings("unchecked")
@Repository
public class OrganizationDaoImpl implements OrganizationDao {

    @Resource
    private BaseDao baseDao;
    /**
     * 
     * @see com.fcc.web.sys.dao.OrganizationDao#delete(java.lang.String)
     **/
    @Override
    public Integer delete(String organId) {
        // 删除组织机构
        Organization organ = (Organization) baseDao.get(Organization.class, organId);
        if (organ != null) {
            return baseDao.executeHql("delete from Organization where parentIds like ?", organ.getParentIds() + "%");
        }
        return 0; 
    }
    
    @Override
    public List<Organization> findOrgans(Collection<String> organIdList) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("organId", organIdList);
        return baseDao.find("from Organization where organId in(:organId)", param);
    }
    
    @Override
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> param = null;
        sb.append("from Organization where 1=1 ");
        if (allChildren) {
            Organization organ = (Organization) baseDao.get(Organization.class, parentOrganId);
            if (organ != null) {
                param = new HashMap<String, Object>(1);
                sb.append(" and parentIds like:organId ");
                param.put("organId", organ.getParentIds() + "-%");
            }
        } else {
//            sb.append(" and organId like:organId ");
//            param.put("organId", parentOrganId + "-____");
            param = new HashMap<String, Object>(1);
            sb.append(" and parentId=:organId");
            if (parentOrganId == null || "".equals(parentOrganId)) {
                param.put("organId", Organization.ROOT.getOrganId());
            } else {
                param.put("organId", parentOrganId);
            }
        }
        return baseDao.find(sb.toString(), param);
    }
}

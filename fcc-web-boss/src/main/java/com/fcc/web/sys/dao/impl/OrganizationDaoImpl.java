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
        return baseDao.executeHql("delete from Organization where organId like ?", organId + "%");
    }
    
    @Override
    public List<Organization> getOrganByOrganId() {
        Map<String, Object> param = null;
        return baseDao.find("from Organization order by organId", param);
    }

    @Override
    public List<Organization> findChildOrgans(String organId) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        StringBuilder sb = new StringBuilder();
        sb.append("from Organization where 1=1 ");
        if (!Organization.ROOT.getOrganId().equals(organId)) {
            sb.append(" and organId like:organId ");
            param.put("organId", organId + "-%");
        }
        sb.append(" order by organLevel asc, organSort asc");
        return baseDao.find(sb.toString(), param);
    }
    
    @Override
    public List<Organization> findOrgans(Collection<String> organIdList) {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("organId", organIdList);
        return baseDao.find("from Organization where organId in(:organId)", param);
    }
    
    @Override
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren) {
        if (parentOrganId != null) {
            StringBuilder sb = new StringBuilder();
            Map<String, Object> param = new HashMap<String, Object>();
            sb.append("from Organization where 1=1 ");
            if (Organization.ROOT.getOrganId().equals(parentOrganId)) {
                if (allChildren) {
                    // Do Nothing
                } else {
                    sb.append(" and organId not like:organId ");
                    param.put("organId", "%-%");
                }
            } else {
                if (allChildren) {
                    sb.append(" and organId like:organId ");
                    param.put("organId", parentOrganId + "-%");
                } else {
                    sb.append(" and organId like:organId ");
                    param.put("organId", parentOrganId + "-____");
                }
            }
            sb.append(" order by organLevel asc, organSort asc");
            return baseDao.find(sb.toString(), param);
        }
        return null;
    }
}

/*
 * @(#)OrganizationDao.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-app-web Maven Webapp
 * 创建日期 : 2016年10月17日
 * 修改历史 : 
 *     1. [2016年10月17日]创建文件 by fqm
 */
package com.fcc.web.sys.dao;

import java.util.Collection;
import java.util.List;

import com.fcc.web.sys.model.Organization;

/**
 * 系统组织机构
 * @version v1.0
 * @author 傅泉明
 */
public interface OrganizationDao {

    public Integer delete(String organId);
    
    public List<Organization> getOrganByOrganId();
    
    public List<Organization> findChildOrgans(String organId);
    
    public List<Organization> findOrgans(Collection<String> organIdList);
    
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren);
}

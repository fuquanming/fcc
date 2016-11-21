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

import java.util.List;

import com.fcc.web.sys.model.Organization;

/**
 * 系统组织机构
 * @version v1.0
 * @author 傅泉明
 */
public interface OrganizationDao {
    /**
     * 删除组织机构
     * @param organId   机构ID
     * @return
     */
    public Integer delete(String organId);
    /**
     * 查询子机构
     * @param parentOrganId     父机构ID
     * @param allChildren       是否全部子机构
     * @return
     */
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren);
}

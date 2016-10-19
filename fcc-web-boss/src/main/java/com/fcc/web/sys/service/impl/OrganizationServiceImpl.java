package com.fcc.web.sys.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.web.sys.dao.OrganizationDao;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.service.OrganizationService;

/**
 * <p>Description:组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
	@Resource
	private BaseService baseService;
	@Resource
	private OrganizationDao organizationDao;
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void create(Organization data) {
		baseService.create(data);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void update(Organization data) {
		baseService.update(data);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
    public void delete(String organId) {
	    organizationDao.delete(organId);
    }
	
	@Transactional(readOnly = true)//只查事务申明
	public List<Organization> getOrgans() {
		return organizationDao.getOrganByOrganId();
	}
	
	@Transactional(readOnly = true)//只查事务申明
    public List<Organization> findChildOrgans(String organId) {
    	return organizationDao.findChildOrgans(organId);
    }

	@Transactional(readOnly = true)//只查事务申明
    public List<Organization> findOrgans(Collection<String> organIdList) {
		return organizationDao.findOrgans(organIdList);
    }
	
	@Transactional(readOnly = true)//只查事务申明
    public List<Organization> findChildOrgans(String parentOrganId, boolean allChildren) {
		return organizationDao.findChildOrgans(parentOrganId, allChildren);
	}
	@Transactional(readOnly = true)//只查事务申明
    public Organization getOrganById(String organId) {
		Organization module = null;
        if(organId != null){
        	if(Organization.ROOT.getOrganId().equals(organId)){
        		module = Organization.ROOT;
        	}else{
        		module = (Organization) baseService.get(Organization.class, organId);
        	}
        }
        return module;
    }
	@Transactional(readOnly = true)//只查事务申明
    public Organization getParentOrgan(Organization organ) {
		Organization parentOrgan = null;
        if(organ != null){
        	parentOrgan = getParentOrgan(organ.getOrganId());
        }
        return parentOrgan;
    }
	@Transactional(readOnly = true)//只查事务申明
    public Organization getParentOrgan(String organId) {
		Organization parentOrgan = null;
        if(organId != null){
        	String parentOrganId = Organization.getParentOrganId(organId);
        	parentOrgan = getOrganById(parentOrganId);
        }
        return parentOrgan;
    }
}

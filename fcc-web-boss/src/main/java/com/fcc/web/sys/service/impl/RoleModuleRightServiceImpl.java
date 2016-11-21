package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.dao.RoleModuleRightDao;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.service.RoleModuleRightService;

/**
 * 
 * @author 傅泉明
 *
 * @date 2007-9-20
 */
@SuppressWarnings("unchecked")
@Service
public class RoleModuleRightServiceImpl implements RoleModuleRightService {
	@Resource
	private BaseService baseService;
	@Resource
	private RoleModuleRightDao roleModuleRightDao;
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#getRightByModuleId(java.lang.String)
     **/
	@Override
    @Transactional(readOnly = true)
	public List<RoleModuleRight> getRightByModuleId(String moduleId) {
		if (moduleId != null && !"".equals(moduleId)) {
			return roleModuleRightDao.getModuleRightByModuleId(moduleId);
		}
		return null;
	}
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#getRoleModuleRights()
     **/
	@Override
    @Transactional(readOnly = true)
	public List<RoleModuleRight> getRoleModuleRights(){
		return baseService.getAll(RoleModuleRight.class);
	}
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#deleteRightByRole(java.lang.String)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void deleteRightByRole(String roleId){
		roleModuleRightDao.deleteByRoleIds(new String[]{roleId});
	}	
	
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#editRight(java.lang.String, java.lang.String, java.lang.Long)
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void editRight(String roleId, String moduleId, Long rightValue) {
		if (rightValue <= 0) {
			roleModuleRightDao.deleteByModuleId(moduleId, roleId);
			return;
		}
		RoleModuleRight data = roleModuleRightDao.getModuleRightByKey(roleId, moduleId);
		if (data != null) {
			data.setRightValue(rightValue);
			baseService.update(data);
		} else {
			RoleModuleRight rmr = new RoleModuleRight();
			rmr.setModuleId(moduleId);
			rmr.setRoleId(roleId);
			rmr.setRightValue(rightValue);
			baseService.create(rmr);
		}
	}
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#addRight(java.lang.String, java.lang.String[])
     **/
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void addRight(String roleId, String[] moduleRight) throws RefusedException {
		int i = 0;
		List<String> moduleIdList = new ArrayList<String>();
		if (moduleRight == null || (moduleRight.length == 1 && moduleRight[0].equals(""))) {
			// 删除所有权限
		} else {
			for( ; i < moduleRight.length ; i+=1){
				String[] tmp = StringUtils.split(moduleRight[i], ":");
				if (tmp.length < 2) {
					throw new RefusedException("参数不正确");
				}
				editRight(roleId, tmp[0], Long.parseLong(tmp[1]));
				moduleIdList.add(tmp[0]);
			}
		}
		if (moduleIdList.size() > 0){
			// 删除其他权限
			roleModuleRightDao.deleteByNoModuleId(moduleIdList, roleId);
		} else {
			// 删除所有权限
		    roleModuleRightDao.deleteByRoleIds(new String[]{roleId});
		}
		
	}
	/**
     * //TODO 添加override说明
     * @see com.fcc.web.sys.service.RoleModuleRightService#getRoleModuleRight(java.lang.String)
     **/
	@Override
    @Transactional(readOnly = true)
	public List<RoleModuleRight> getRoleModuleRight(String roleId) {
		return roleModuleRightDao.getRoleModuleRight(roleId);
	}

}

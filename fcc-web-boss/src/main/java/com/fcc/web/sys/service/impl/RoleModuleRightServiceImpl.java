package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

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
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public List<RoleModuleRight> getModuleRightByModuleId(String moduleId) {
		if (moduleId != null && !"".equals(moduleId)) {
			return roleModuleRightDao.getModuleRightByModuleId(moduleId);
		}
		return null;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public List<RoleModuleRight> getModuleRightByModuleIds(String[] moduleIds) {
		if (moduleIds != null && moduleIds.length > 0) {
			return roleModuleRightDao.getModuleRightByModuleIds(moduleIds);
		}
		return null;
	}
	
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public List<RoleModuleRight> getModuleRightByRoleId(Collection<String> roleIds) {
//		if (roleIds != null && roleIds.length > 0) {
//			return getBaseDao().getSession().createQuery("select rm.moduleId from RoleModuleRight as rm where rm.roleId in(:roleId) and rm.rightValue>0 order by moduleId ")
//								.setParameterList("roleId", roleIds)
//								.list();
//		}
		if (roleIds != null && roleIds.isEmpty() == false) {
			return roleModuleRightDao.getModuleRightByRoleId(roleIds);
		}
		return null;
	}
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public RoleModuleRight getModuleRightByKey(String roleId , String moduleId){
		RoleModuleRight mr = null;
		if(roleId != null && moduleId != null){
			return roleModuleRightDao.getModuleRightByKey(roleId, moduleId);
		}
		return mr;
	}
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public List<RoleModuleRight> getModuleRight(){
		return baseService.getAll(RoleModuleRight.class);
	}
	@Transactional(rollbackFor = Exception.class)//事务申明
	@Override
	public void deleteModuleRightByRole(String roleId){
//		getBaseDao().delete(new String[]{roleId}, RoleModuleRight.class, "roleId");
		roleModuleRightDao.deleteByRoleIds(new String[]{roleId});
	}	
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	@Override
	public void updateModuleRight(String roleId, String moduleId, Long rightValue) {
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
	@Transactional(rollbackFor = Exception.class)//事务申明
	@Override
	public void createRight(String roleId, String[] moduleRight) throws RefusedException {
		int i = 0;
		List<String> moduleIdList = new ArrayList<String>();
		if (moduleRight == null || (moduleRight.length == 1 && moduleRight[0].equals(""))) {
			// 删除所有权限
		} else {
			for( ; i < moduleRight.length ; i+=1){
				String[] tmp = moduleRight[i].split(":");
				if (tmp.length < 2) {
					throw new RefusedException("参数不正确");
				}
				updateModuleRight(roleId, tmp[0], Long.parseLong(tmp[1]));
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
	@Transactional(readOnly = true)//只查事务申明
	@Override
	public List<RoleModuleRight> getRoleModuleRight(String roleId) {
		return roleModuleRightDao.getRoleModuleRight(roleId);
	}

}

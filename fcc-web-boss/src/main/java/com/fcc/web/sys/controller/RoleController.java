package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 角色</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "角色管理")
@Controller
@RequestMapping("/manage/sys/role")
public class RoleController extends AppWebController {

	private static Logger logger = Logger.getLogger(RoleController.class);
	@Resource
	private RoleService roleService;
	@Resource
	private BaseService baseService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	@Resource
	private SysUserService sysUserService;
	/** 显示角色列表 */
	@ApiOperation(value = "显示角色列表")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		// 判断当前用户是否是管理员
//		if (isAdmin(request)) {
//			// 获取所有有该权限的用户列表，可以根据创建者查询其创建的角色
//			String servletPath = request.getServletPath();
//			if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
//			Module module = CacheUtil.moduleUrlMap.get(servletPath);
//			if (module != null) {
//				try {
//					List<RoleModuleRight> list = roleModuleRightService.getModuleRightByModuleId(module.getModuleId());
//					List<String> roleIdList = new ArrayList<String>();
//					for (RoleModuleRight r : list) {
//						roleIdList.add(r.getRoleId());
//					}
//					if (roleIdList.size() > 0) {
//						List<SysUser> sysUserList = sysUserService.getUserByRoleIds(roleIdList);
//						if (sysUserList != null && sysUserList.size() > 0) {
//							request.setAttribute("userList", sysUserList);
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error(e);
//				}
//			}
//		}
//		request.setAttribute("sysUser", getSysUser(request));// 当前用户
		return "manage/sys/role_list";
	}
	
	/** 显示角色查询页面 */
	@ApiOperation(value = "显示角色列表页面")
	@RequestMapping(value = "/toView.do", method = RequestMethod.GET)
	@Permissions("view")
	public String toView(HttpServletRequest request) {
		getRole(request);
		return "manage/sys/role_view";
	}
	
	/** 显示角色新增页面 */
	@ApiOperation(value = "显示新增角色页面")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
	public String toAdd(HttpServletRequest request) {
		return "manage/sys/role_add";
	}
	
	/** 显示角色修改页面 */
	@ApiOperation(value = "显示修改角色页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request) {
		getRole(request);
		return "manage/sys/role_edit";
	}
	
	@ApiOperation(value = "新增角色")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(HttpServletRequest request, 
	        @ApiParam(required = true, value = "角色名称") @RequestParam(name = "roleName", defaultValue = "") String roleName,
	        @ApiParam(required = false, value = "角色权限值") @RequestParam(name = "rightValue", defaultValue = "") String rightValue,
	        @ApiParam(required = false, value = "角色备注") @RequestParam(name = "roleDesc", defaultValue = "") String roleDesc) {
		Message message = new Message();
		try {
			if (roleName == null || "".equals(roleName)) throw new RefusedException(Constants.StatusCode.Role.emptyRoleName);
			Role role = new Role();
			role.setRoleId(UUID.randomUUID().toString().substring(0, 6));
			role.setRoleName(roleName);
			role.setRoleDesc(roleDesc);
			role.setCreateTime(new Date());
			role.setCreateUser(getSysUser(request).getUserId());
			if (rightValue != null && !"".equals(rightValue)) {
				String[] moduleRigth = rightValue.split(",");
				roleService.create(role, moduleRigth);
			} else {
				roleService.create(role);
			}
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增角色失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改角色")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request, 
	        @ApiParam(required = false, value = "角色权限值") @RequestParam(name = "rightValue", defaultValue = "") String rightValue,
            Role role) {
		Message message = new Message();
		try {
			String roleName = role.getRoleName();
			if (roleName == null || "".equals(roleName)) throw new RefusedException(Constants.StatusCode.Role.emptyRoleName);
			Role dbRole = null;
			String roleId = role.getRoleId();
			if (StringUtils.isNotEmpty(roleId)) {
				dbRole = (Role) baseService.get(Role.class, roleId);
				dbRole.setRoleName(roleName);
				dbRole.setRoleDesc(role.getRoleDesc());
			} else {
				throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
			}
			
//			SysUser user = getSysUser(request);
//			if (!isAdmin(request)) {
//				// 不能修改其他人创建的用户
//				if (!user.getUserId().equals(dbRole.getCreateUser())) {
//					throw new RefusedException("不能修改他人创建的角色！");
//				}
//			}
			
			if (rightValue == null || "".equals(rightValue)) {
				// 清空权限
				roleService.update(dbRole, null);
			} else {
				String[] moduleRigth = rightValue.split(",");
				roleService.update(dbRole, moduleRigth);
			}
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改角色失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除角色")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "角色ID、用，分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
		Message message = new Message();
		try {
		    if (StringUtils.isEmpty(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			String[] ids = id.split(",");
			roleService.delete(ids);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
			reloadModuleCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除角色失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 角色 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询角色")
	@RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(HttpServletRequest request, EasyuiDataGrid dg,
	        @ApiParam(required = false, value = "角色名称") @RequestParam(name = "searchName", defaultValue = "") String roleName) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
//			String createUser = request.getParameter("createUser");
			Map<String, Object> param = null;
            if (!StringUtils.isEmpty(roleName)) {
                param = new HashMap<String, Object>();
                param.put("roleName", roleName);
            }
//			param.put("createUser", (createUser == null || "".equals(createUser)) ? getSysUser(request).getUserId() : createUser);
			ListPage listPage = roleService.queryPage(dg.getPage(), dg.getRows(), param);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<Role>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	/**
	 * 返回角色及角色权限 json 
	 * @param dg
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "查询角色权限")
	@RequestMapping(value = "/roleModuleRight.do", method = RequestMethod.POST)
	@ResponseBody
	public Role roleModuleRight(HttpServletRequest request,
	        @ApiParam(required = true, value = "角色ID") @RequestParam(name = "id", defaultValue = "") String roleId) {
		Role role = new Role();
		try {
			role = roleService.getRoleWithModuleRight(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询角色权限失败！", e);
			role.setRoleName("dataError");
		}
		return role;
	} 
	
	private void getRole(HttpServletRequest request) {
		Role role = null;
		String roleId = request.getParameter("id");
		Map<String, RoleModuleRight> rightMap = null;
		try {
			if (StringUtils.isNotEmpty(roleId)) {
				role = roleService.getRoleWithModuleRight(roleId);
				rightMap = new HashMap<String, RoleModuleRight>();
				Iterator<RoleModuleRight> it = role.getRoleModuleRights().iterator();
				while (it.hasNext()) {
					RoleModuleRight right = it.next();
					rightMap.put(right.getModuleId(), right);
				}
				request.getSession().setAttribute(Constants.SysUserSession.sessionRole, role);
				request.getSession().setAttribute(Constants.SysUserSession.sessionRoleRightMap, rightMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}

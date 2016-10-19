package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;

/**
 * <p>Description: 管理系统 角色</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/role")
public class RoleAction extends AppWebController {

	private static Logger logger = Logger.getLogger(RoleAction.class);
	@Resource
	private RoleService roleService;
	@Resource
	private BaseService baseService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	@Resource
	private SysUserService sysUserService;
	/** 显示角色列表 */
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		// 判断当前用户是否是管理员
		if (isAdmin(request)) {
			// 获取所有有该权限的用户列表，可以根据创建者查询其创建的角色
			String servletPath = request.getServletPath();
			if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
			Module module = CacheUtil.moduleUrlMap.get(servletPath);
			if (module != null) {
				try {
					List<RoleModuleRight> list = roleModuleRightService.getModuleRightByModuleId(module.getModuleId());
					List<String> roleIdList = new ArrayList<String>();
					for (RoleModuleRight r : list) {
						roleIdList.add(r.getRoleId());
					}
					if (roleIdList.size() > 0) {
						List<SysUser> sysUserList = sysUserService.getUserByRoleIds(roleIdList);
						if (sysUserList != null && sysUserList.size() > 0) {
							request.setAttribute("userList", sysUserList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
		}
		request.setAttribute("sysUser", getSysUser(request));// 当前用户
		return "/WEB-INF/manage/sys/role_list";
	}
	
	/** 显示角色查询页面 */
	@RequestMapping("/toView")
	public String toView(HttpServletRequest request) {
		getRole(request);
		return "/WEB-INF/manage/sys/role_view";
	}
	
	/** 显示角色新增页面 */
	@RequestMapping("/toAdd")
	public String toAdd(HttpServletRequest request) {
		return "/WEB-INF/manage/sys/role_add";
	}
	
	/** 显示角色修改页面 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpServletRequest request) {
		getRole(request);
		return "/WEB-INF/manage/sys/role_edit";
	}
	
	@RequestMapping("/add")
	public ModelAndView add(Role role, HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			String rightValue = request.getParameter("rightValue");
			String roleDesc = request.getParameter("roleDesc");
			String roleName = role.getRoleName();
			if (roleName == null || "".equals(roleName)) {
				throw new RefusedException("请输入角色名称！");
			}
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
			message.setMsg("保存成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("保存失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	@RequestMapping("/edit")
	public ModelAndView edit(Role role, HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.addObject(message);
		try {
			String rightValue = request.getParameter("rightValue");
			String roleDesc = request.getParameter("roleDesc");
			String roleName = role.getRoleName();
			if (roleName == null || "".equals(roleName)) {
				throw new RefusedException("请输入角色名称！");
			}
			Role dbRole = null;
			String roleId = role.getRoleId();
			if (StringUtils.isNotEmpty(roleId)) {
				dbRole = (Role) baseService.get(Role.class, roleId);
				dbRole.setRoleName(roleName);
				dbRole.setRoleDesc(roleDesc);
			} else {
				throw new RefusedException("修改的角色不存在！");
			}
			
			SysUser user = getSysUser(request);
			if (!isAdmin(request)) {
				// 不能修改其他人创建的用户
				if (!user.getUserId().equals(dbRole.getCreateUser())) {
					throw new RefusedException("不能修改他人创建的角色！");
				}
			}
			
			if (rightValue == null || "".equals(rightValue)) {
				// 清空权限
				roleService.update(dbRole, null);
			} else {
				String[] moduleRigth = rightValue.split(",");
				roleService.update(dbRole, moduleRigth);
			}
			message.setSuccess(true);
			message.setMsg("修改成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	@RequestMapping("/delete")
	public ModelAndView delete(HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要删除的记录！");
			}
			String[] ids = id.split(",");
			roleService.delete(ids);
			message.setMsg("删除成功！");
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("删除失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	/**
	 * 角色 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			String roleName = request.getParameter("searchName");
//			String createUser = request.getParameter("createUser");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("roleName", roleName);
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
	@RequestMapping("/roleModuleRight")
	@ResponseBody
	public Role roleModuleRight(HttpServletRequest request) {
		Role role = new Role();
		String roleId = request.getParameter("id");
		try {
			role = roleService.getRoleWithModuleRight(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
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
				request.getSession().setAttribute(Constanst.SysUserSession.SESSION_ROLE, role);
				request.getSession().setAttribute(Constanst.SysUserSession.SESSION_ROLE_RIGHT_MAP, rightMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}

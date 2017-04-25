package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.fcc.commons.web.annotation.Logical;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.ModuleService;
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

	private Logger logger = Logger.getLogger(RoleController.class);
	@Resource
	private RoleService roleService;
	@Resource
	private BaseService baseService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private ModuleService moduleService;
	
	/** 显示角色列表 */
	@ApiOperation(value = "显示角色列表")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		// 判断当前用户是否是管理员
	    if (isGroup()) {
	        if (getSysUser().isAdmin()) {
	            // 获取所有有该权限的用户列表，可以根据创建者查询其创建的角色
	            String servletPath = request.getServletPath();
	            if (servletPath.substring(0, 1).equals("/")) servletPath = servletPath.substring(1);
	            String moduleId = cacheService.getModuleUrlMap().get(servletPath);
	            Module module = cacheService.getModuleMap().get(moduleId);
	            if (module != null) {
	                try {
	                    List<RoleModuleRight> list = roleModuleRightService.getRightByModuleId(module.getModuleId());
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
	    }
		request.setAttribute("sysUser", getSysUser());// 当前用户
		return "manage/sys/role_list";
	}
	
	/** 显示角色查询页面 */
	@ApiOperation(value = "显示角色列表页面")
	@RequestMapping(value = "/toView.do", method = RequestMethod.GET)
	@Permissions("view")
	public String toView(HttpServletRequest request) {
	    Role role = getRole(request);
		request.getSession().setAttribute(Constants.SysUserSession.sessionRole, role);
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
		Role role = getRole(request);
		request.getSession().setAttribute(Constants.SysUserSession.sessionRole, role);
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
			if (roleName == null || "".equals(roleName)) throw new RefusedException(StatusCode.Role.emptyRoleName);
			Role role = new Role();
			role.setRoleId(UUID.randomUUID().toString().substring(0, 6));
			role.setRoleName(roleName);
			role.setRoleDesc(roleDesc);
			role.setCreateTime(new Date());
			role.setCreateUser(getSysUser().getUserId());
			if (rightValue != null && !"".equals(rightValue)) {
				String[] moduleRigth = StringUtils.split(rightValue, ",");
				roleService.add(role, moduleRigth);
			} else {
				roleService.add(role);
			}
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增角色失败！", e);
			message.setMsg(StatusCode.Sys.fail);
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
			if (roleName == null || "".equals(roleName)) throw new RefusedException(StatusCode.Role.emptyRoleName);
			Role dbRole = null;
			String roleId = role.getRoleId();
			if (StringUtils.isNotEmpty(roleId)) {
				dbRole = (Role) baseService.get(Role.class, roleId);
				dbRole.setRoleName(roleName);
				dbRole.setRoleDesc(role.getRoleDesc());
			} else {
				throw new RefusedException(StatusCode.Sys.emptyUpdateId);
			}
			
			SysUser user = getSysUser();
			if (!user.isAdmin()) {
				// 不能修改其他人创建的用户
				if (!user.getUserId().equals(dbRole.getCreateUser())) {
					throw new RefusedException(StatusCode.Role.errorMySelfRoleId);
				}
			}
			
			if (rightValue == null || "".equals(rightValue)) {
				// 清空权限
				roleService.edit(dbRole, null);
			} else {
				String[] moduleRigth = StringUtils.split(rightValue, ",");
				roleService.edit(dbRole, moduleRigth);
			}
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改角色失败！", e);
			message.setMsg(StatusCode.Sys.fail);
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
		    if (StringUtils.isEmpty(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ",");
			roleService.delete(ids);
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
			reloadModuleCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除角色失败！", e);
			message.setMsg(StatusCode.Sys.fail);
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
			String createUser = request.getParameter("createUser");
			Map<String, Object> param = new HashMap<String, Object>(2);
            if (!StringUtils.isEmpty(roleName)) {
                param.put("roleName", roleName);
            }
            if (isGroup()) {
                if (getSysUser().isAdmin()) {
                    param.put("createUser", (createUser == null || "".equals(createUser)) ? null : createUser);
                } else {
                    param.put("createUser", getSysUser().getUserId());
                }
            }
			ListPage listPage = roleService.queryPage(dg.getPage(), dg.getRows(), param);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
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
	
	private Role getRole(HttpServletRequest request) {
		Role role = null;
		String roleId = request.getParameter("id");
		try {
			if (StringUtils.isNotEmpty(roleId)) {
				role = (Role) baseService.get(Role.class, roleId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return role;
	}
	
	@ApiOperation(value = "查询模块树形数据")
    @RequestMapping(value = "/tree.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("edit")
    public List<EasyuiTreeNode> tree(HttpServletRequest request,
            @ApiParam(required = false, value = "节点状态，open、closed") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
        if (StringUtils.isEmpty(nodeStatus)) nodeStatus = EasyuiTreeNode.STATE_OPEN;
        List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
        try {
            Role role = (Role) request.getSession().getAttribute(Constants.SysUserSession.sessionRole);
            nodeList = moduleService.getModuleTree(getSysUser(), nodeStatus, true, role);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询模块树形失败", e);
            nodeList = new ArrayList<EasyuiTreeNode>();
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setMsg(e.getMessage());
            nodeList.add(node);
        }
        request.getSession().removeAttribute(Constants.SysUserSession.sessionRole);
        return nodeList;
    }
	
	/**
     * 模块 列表 返回json 给 easyUI 
     * @return
     */
    @ApiOperation(value = "查询模块树形列表")
    @RequestMapping(value = "/treegrid.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @Permissions(value = {"add", "edit"}, logical = Logical.OR)
    public List<EasyuiTreeGridModule> treegrid(HttpServletRequest request,
            @ApiParam(required = false, value = "节点状态，open、closed") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
        List<EasyuiTreeGridModule> nodeList = null;
        try {
            Role role = (Role) request.getSession().getAttribute(Constants.SysUserSession.sessionRole);
            SysUser sysUser = null;
            if (isGroup()) {
                sysUser = getSysUser();// 组模式校验用户权限
            }
            nodeList = moduleService.getModuleTreeGrid(sysUser, nodeStatus, role);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询模块树形列表失败", e);
            nodeList = new ArrayList<EasyuiTreeGridModule>();
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setMsg(e.getMessage());
            nodeList.add(node);
        }
        request.getSession().removeAttribute(Constants.SysUserSession.sessionRole);
        return nodeList;
    }
}

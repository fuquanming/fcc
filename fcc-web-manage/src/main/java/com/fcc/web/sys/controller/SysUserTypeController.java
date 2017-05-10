package com.fcc.web.sys.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.fcc.web.sys.common.StatusCode;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.view.SysUserType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 系统用户</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/manage/sys/userType")
public class SysUserTypeController extends AppWebController {

	private Logger logger = Logger.getLogger(SysUserTypeController.class);
	@Resource
    private BaseService baseService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private RoleService roleService;
	
	/** 显示系统用户类型角色列表 */
	@ApiOperation(value = "显示用户类型角色页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
	    request.setAttribute("userTypeMap", Constants.userTypes);
		return "manage/sys/sysUserType/userType_list";
	}
	
	/** 显示用户类型角色修改页面 */
	@ApiOperation(value = "显示修改用户类型角色页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "用户类型ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
			SysUserType sysUserType = new SysUserType();
			sysUserType.setUserType(id);
			sysUserType.setUserTypeName(Constants.userTypes.get(id));
			Set<Role> roles = new HashSet<Role>();
			roles.addAll(roleService.getRoleByUserType(id));
			sysUserType.setRoles(roles);
			request.setAttribute("data", sysUserType);
			
			request.setAttribute("roleList", roleService.queryPage(1, 0, null).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载显示修改用户类型页面数据失败！", e);
		}
		return "manage/sys/sysUserType/userType_edit";
	}
	
	@ApiOperation(value = "修改用户类型角色")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request, SysUserType sysUserType,
	        @ApiParam(required = true, value = "权限ID") @RequestParam(name = "roleValue", defaultValue = "") String roleValue) {
		Message message = new Message();
		try {
            String[] roleIds = null;
			if (roleValue != null && !"".equals(roleValue)) {
				roleIds = StringUtils.split(roleValue, ",");
			}
            
			sysUserService.addUserTypeRole(sysUserType.getUserType(), roleIds);
            
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改用户失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除用户类型角色")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "操作ID、用，分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
		Message message = new Message();
		try {
			if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ",");
			sysUserService.deleteUserTypeRole(ids);
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户失败！", e);
			message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 系统用户 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询用户类型角色")
	@RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "用户类型") @RequestParam(name = "userType", defaultValue = "") String userType) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			ListPage listPage = sysUserService.queryUserTypePage(dg.getPage(), dg.getRows(), userType);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			List<SysUserType> dataList = listPage.getDataList();
			for (SysUserType data : dataList) {
				StringBuffer sb = new StringBuffer();
				for (Role role : data.getRoles()) {
					sb.append(role.getRoleName()).append(",");
				}
				String roleNames = sb.toString();
				if (roleNames.length() > 0) roleNames = roleNames.substring(0, roleNames.length() - 1);
				data.setRoleNames(roleNames);
			}
			json.setRows(dataList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载查询用户类型角色数据失败！", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
}

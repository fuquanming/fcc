package com.fcc.web.sys.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.OrganizationService;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.view.SysUserView;

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
@RequestMapping("/manage/sys/user")
public class SysUserController extends AppWebController {

	private static Logger logger = Logger.getLogger(SysUserController.class);
	@Resource
    private BaseService baseService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private RoleService roleService;
	@Resource
	private OrganizationService organService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	
	/** 显示系统用户列表 */
	@ApiOperation(value = "显示用户列表页面")
	@GetMapping("/view.do")
	public String view(HttpServletRequest request) {
		// 判断当前用户是否是管理员
//		if (isAdmin(request)) {
//			// 获取所有有该权限的用户列表，可以根据创建者查询其创建的用户
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
//			request.setAttribute("isAdmin", true);
//		}
		request.setAttribute("userStatusLock", Constants.UserStatus.lock);
		request.setAttribute("userStatusActivation", Constants.UserStatus.activation);
		request.setAttribute("userStatusOff", Constants.UserStatus.off);
		request.setAttribute("sysUser", getSysUser(request));// 当前用户
		return "manage/sys/user_list";
	}
	
	/** 显示用户查看页面 */
	@ApiOperation(value = "显示用户信息页面")
    @GetMapping("/toView.do")
	public String toView(HttpServletRequest request,
	        @ApiParam(required = true, value = "用户ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
			SysUser data = sysUserService.getUserWithRole(id);
			request.setAttribute("data", data);
			if (data != null) {
				String organId = data.getDept();
				if (organId != null) {
					request.setAttribute("organ", organService.getOrganById(data.getDept()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载显示用户信息页面数据失败！", e);
		}
		return "manage/sys/user_view";
	}
	
	/** 显示用户新增页面 */
	@ApiOperation(value = "显示新增用户页面")
    @GetMapping(value = "/toAdd.do")
	public String toAdd(HttpServletRequest request) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("createUser", getSysUser(request).getUserId());
			request.setAttribute("roleList", roleService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载新增用户页面数据失败！", e);
		}
		return "manage/sys/user_add";
	}
	
	/** 显示用户修改页面 */
	@ApiOperation(value = "显示修改用户页面")
    @GetMapping(value = "/toEdit.do")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
			SysUser data = sysUserService.getUserWithRole(id);
			request.setAttribute("data", data);
			if (data != null) {
				String organId = data.getDept();
				if (organId != null) {
					request.setAttribute("organ", organService.getOrganById(data.getDept()));
				}
			}
			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("createUser", data.getCreateUser());
			request.setAttribute("roleList", roleService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载显示修改用户页面数据失败！", e);
		}
		return "manage/sys/user_edit";
	}
	
	@ApiOperation(value = "新增用户")
    @PostMapping(value = "/add.do")
	public ModelAndView add(HttpServletRequest request, SysUser sysUser,
	        @ApiParam(required = true, value = "机构ID") @RequestParam(name = "organId", defaultValue = "") String dept,
	        @ApiParam(required = true, value = "角色权限，模块ID:权值,模块ID:权值") @RequestParam(name = "roleValue", defaultValue = "") String roleValue) {
		Message message = new Message();
		try {
			String[] roleIds = null;
			if (roleValue != null && !"".equals(roleValue)) {
				roleIds = roleValue.split(",");
			}
			String userId = sysUser.getUserId();
			if (userId == null || "".equals(userId)) throw new RefusedException(Constants.StatusCode.SysUser.emptyUserId);
			if (dept == null || "".equals(dept)) throw new RefusedException(Constants.StatusCode.SysUser.emptyOrganID);
			SysUser user = getSysUser(request);
//			// 组织机构
//			if (OrganUtil.checkParent(user, dept)) {// 判断是否删除上级组织机构
//				throw new RefusedException("不能选择上级机构！");
//			}
			
			// 判断userId
			SysUser tempUser = (SysUser) baseService.get(SysUser.class, userId);
			if (tempUser != null) throw new RefusedException(Constants.StatusCode.SysUser.exitsUserId);
			
			sysUser.setPassword(EncryptionUtil.encodeMD5("888888").toLowerCase());
			sysUser.setDept(dept);
			sysUser.setRegDate(new Timestamp(System.currentTimeMillis()));
			sysUser.setCreateTime(sysUser.getRegDate());
			sysUser.setCreateUser(user.getUserId());
			sysUserService.create(sysUser, roleIds);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增用户失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改用户")
    @PostMapping(value = "/edit.do")
	public ModelAndView edit(HttpServletRequest request, SysUser sysUser,
	        @ApiParam(required = true, value = "机构ID") @RequestParam(name = "organId", defaultValue = "") String dept,
	        @ApiParam(required = true, value = "角色权限，模块ID:权值,模块ID:权值") @RequestParam(name = "roleValue", defaultValue = "") String roleValue) {
		Message message = new Message();
		try {
		    if (dept == null || "".equals(dept)) throw new RefusedException(Constants.StatusCode.SysUser.emptyOrganID);
			String[] roleIds = null;
			if (roleValue != null && !"".equals(roleValue)) {
				roleIds = roleValue.split(",");
			}
//			// 组织机构
//			SysUser user = getSysUser(request);
//			if (OrganUtil.checkParent(user, dept)) {// 判断是否选择上级组织机构
//				throw new RefusedException("不能选择上级机构！");
//			}
			
			sysUser.setDept(dept);
			sysUserService.update(sysUser, roleIds);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改用户失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除用户")
    @PostMapping(value = "/delete.do")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "操作ID、用，分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
		Message message = new Message();
		try {
			if (id == null || "".equals(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			String[] ids = id.split(",");
			sysUserService.delete(ids);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "重置用户密码")
    @PostMapping("/resetPassword.do")
    public ModelAndView resetPassword(HttpServletRequest request,
            @ApiParam(required = true, value = "操作ID、用,分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
        Message message = new Message();
        try {
            if (id == null || "".equals(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
            String[] ids = id.split(",");
            sysUserService.resetPassword(ids, EncryptionUtil.encodeMD5("888888").toLowerCase());
            message.setMsg(Constants.StatusCode.Sys.success);
            message.setSuccess(true);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("重置用户密码失败！", e);
            message.setMsg(Constants.StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
	
	@ApiOperation(value = "锁定用户")
	@PostMapping("/lock.do")
	public ModelAndView lock(HttpServletRequest request,
	        @ApiParam(required = true, value = "操作ID、用,分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id,
	        @ApiParam(required = true, value = "用户状态") @RequestParam(name = "userStatus", defaultValue = "") String userStatus) {
		Message message = new Message();
		try {
			if (id == null || "".equals(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
			String[] ids = id.split(",");
			sysUserService.updateStatus(ids, userStatus);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("锁定解锁用户失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 系统用户 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询用户")
    @PostMapping(value = "/datagrid.do") 
    @ResponseBody
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "用户账号") @RequestParam(name = "userId", defaultValue = "") String userId,
	        @ApiParam(required = false, value = "机构ID") @RequestParam(name = "organId", defaultValue = "") String organId,
	        @ApiParam(required = false, value = "用户名称") @RequestParam(name = "userName", defaultValue = "") String userName) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
//			String createUser = request.getParameter("createUser");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("dept", organId);
			param.put("userName", userName);
//			param.put("createUser", (createUser == null || "".equals(createUser)) ? getSysUser(request).getUserId() : createUser);
			if (isAdmin(request)) {
				param.put("isAdmin", "Y");
			}
			ListPage listPage = sysUserService.queryPage(dg.getPage(), dg.getRows(), param);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			
			List<SysUser> dataList = listPage.getDataList();
			List<SysUserView> list = new ArrayList<SysUserView>();
			for (SysUser data : dataList) {
				SysUserView view = new SysUserView();
				StringBuffer sb = new StringBuffer();
				for (Role role : data.getRoles()) {
					sb.append(role.getRoleName()).append(",");
				}
				String roleNames = sb.toString();
				if (roleNames.length() > 0) roleNames = roleNames.substring(0, roleNames.length() - 1);
				BeanUtils.copyProperties(data, view);
				view.setRoleNames(roleNames);
				list.add(view);
			}
			
			json.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载查询用户数据失败！", e);
			json.setTotal(0L);
			json.setRows(new ArrayList<SysUserView>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	
	/** 系统用户菜单 */
	@ApiOperation(value = "查询用户菜单")
	@PostMapping("/userMenu.do")
	@ResponseBody
	public List<EasyuiTreeNode> userMenu(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
			TreeSet<Module> menuSet = (TreeSet<Module>) session.getAttribute(Constants.SysUserSession.menu);
			String moduleId = request.getParameter("moduleId");// 获取该模块下所有模块
			if (StringUtils.isNotEmpty(moduleId)) {
				TreeSet<Module> moduleSet = new TreeSet<Module>();
				for (Module m : menuSet) {
					if (m.getModuleId().equals(moduleId)) {
						continue;
					} else if (m.getModuleId().startsWith(moduleId)) {
						moduleSet.add(m);
					}
				}
				menuSet = moduleSet;
			}
			if (menuSet == null) menuSet = CacheUtil.loadUserMenu(request);
			nodeList = (List<EasyuiTreeNode>) session.getAttribute(Constants.SysUserSession.menuUi);
			if (nodeList == null || StringUtils.isNotEmpty(moduleId)) nodeList = CacheUtil.loadUserMenuUI(request, menuSet);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			nodeList.clear();
			EasyuiTreeNode node = new EasyuiTreeNode();
			node.setMsg(e.getMessage());
			nodeList.add(node);
			session.removeAttribute(Constants.SysUserSession.menuUi);
			session.removeAttribute(Constants.SysUserSession.menu);
		}
		return nodeList;
	}
}
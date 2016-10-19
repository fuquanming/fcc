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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.utils.EncryptionUtil;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.OrganizationService;
import com.fcc.web.sys.service.RoleModuleRightService;
import com.fcc.web.sys.service.RoleService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.util.OrganUtil;
import com.fcc.web.sys.view.SysUserView;

/**
 * <p>Description: 管理系统 系统用户</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/manage/sys/user")
public class SysUserAction extends AppWebController {

	private static Logger logger = Logger.getLogger(SysUserAction.class);
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
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		// 判断当前用户是否是管理员
		if (isAdmin(request)) {
			// 获取所有有该权限的用户列表，可以根据创建者查询其创建的用户
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
			request.setAttribute("isAdmin", true);
		}
		request.setAttribute("userStatusLock", Constanst.USER_STATE_LOCK);
		request.setAttribute("userStatusActivation", Constanst.USER_STATE_ACTIVATION);
		request.setAttribute("userStatusOff", Constanst.USER_STATE_OFF);
		request.setAttribute("sysUser", getSysUser(request));// 当前用户
		return "/WEB-INF/manage/sys/user_list";
	}
	
	/** 显示用户查看页面 */
	@RequestMapping("/toView")
	public String toView(HttpServletRequest request) {
		String id = request.getParameter("id");
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
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/user_view";
	}
	
	/** 显示用户新增页面 */
	@RequestMapping("/toAdd")
	public String toAdd(HttpServletRequest request) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("createUser", getSysUser(request).getUserId());
			request.setAttribute("roleList", roleService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/user_add";
	}
	
	/** 显示用户修改页面 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpServletRequest request) {
		String id = request.getParameter("id");
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
			param.put("createUser", data.getCreateUser());
			request.setAttribute("roleList", roleService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/user_edit";
	}
	
	@RequestMapping("/add")
	public ModelAndView add(SysUser sysUser, HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			String dept = request.getParameter("organId");
			String roleValue = request.getParameter("roleValue");
			String[] roleIds = null;
			if (roleValue != null && !"".equals(roleValue)) {
				roleIds = roleValue.split(",");
			}
			String userId = sysUser.getUserId();
			if (userId == null || "".equals(userId)) {
				throw new RefusedException("请输入帐号！");
			}
			if (dept == null || "".equals(dept)) {
				throw new RefusedException("请选择组织机构！");
			}
			SysUser user = getSysUser(request);
			// 组织机构
			if (OrganUtil.checkParent(user, dept)) {// 判断是否删除上级组织机构
				throw new RefusedException("不能选择上级机构！");
			}
			
			// 判断userId
			SysUser tempUser = (SysUser) baseService.get(SysUser.class, userId);
			if (tempUser != null) {
				throw new RefusedException("帐号：" + userId + "，已存在！");
			}
			
			sysUser.setPassword(EncryptionUtil.encodeMD5("888888").toLowerCase());
			sysUser.setDept(dept);
			sysUser.setRegDate(new Timestamp(System.currentTimeMillis()));
			sysUser.setCreateTime(sysUser.getRegDate());
			sysUser.setCreateUser(user.getUserId());
			sysUserService.create(sysUser, roleIds);
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
	public ModelAndView edit(SysUser sysUser, HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			SysUser user = getSysUser(request);
			String dept = request.getParameter("organId");
			String roleValue = request.getParameter("roleValue");
			String[] roleIds = null;
			if (roleValue != null && !"".equals(roleValue)) {
				roleIds = roleValue.split(",");
			}
			// 组织机构
			if (OrganUtil.checkParent(user, dept)) {// 判断是否选择上级组织机构
				throw new RefusedException("不能选择上级机构！");
			}
			
			sysUser.setDept(dept);
			sysUserService.update(sysUser, roleIds);
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
	
	@RequestMapping("/resetPassword")
	public ModelAndView resetPassword(HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要操作的记录！");
			}
			String[] ids = id.split(",");
			sysUserService.resetPassword(ids, EncryptionUtil.encodeMD5("888888").toLowerCase());
			message.setMsg("重置成功！");
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("重置失败！");
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
			sysUserService.delete(ids);
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
	
	@RequestMapping("/lock")
	public ModelAndView lock(HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String id = request.getParameter("ids");
		String userStatus = request.getParameter("userStatus");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要操作的记录！");
			}
			String[] ids = id.split(",");
			sysUserService.updateStatus(ids, userStatus);
			message.setMsg("更新成功！");
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("更新失败！");
			message.setObj(e.getMessage());
		}
		return mav;
	}
	
	/**
	 * 系统用户 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			String userId = request.getParameter("userId");
			String organId = request.getParameter("organId");
			String userName = request.getParameter("userName");
			String createUser = request.getParameter("createUser");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("dept", organId);
			param.put("userName", userName);
			param.put("createUser", (createUser == null || "".equals(createUser)) ? getSysUser(request).getUserId() : createUser);
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
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<SysUserView>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	
	/** 系统用户菜单 */
	@RequestMapping("/userMenu")
	@ResponseBody
	public List<EasyuiTreeNode> userMenu(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
			TreeSet<Module> menuSet = (TreeSet<Module>) session.getAttribute(Constanst.SysUserSession.USER_MENU);
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
			nodeList = (List<EasyuiTreeNode>) session.getAttribute(Constanst.SysUserSession.USER_MENU_UI);
			if (nodeList == null || StringUtils.isNotEmpty(moduleId)) nodeList = CacheUtil.loadUserMenuUI(request, menuSet);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			nodeList.clear();
			EasyuiTreeNode node = new EasyuiTreeNode();
			node.setMsg(e.getMessage());
			nodeList.add(node);
			session.removeAttribute(Constanst.SysUserSession.USER_MENU_UI);
			session.removeAttribute(Constanst.SysUserSession.USER_MENU);
		}
		return nodeList;
	}
}

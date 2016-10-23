package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.common.Constanst;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.model.Role;
import com.fcc.web.sys.model.RoleModuleRight;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.ModuleService;
import com.fcc.web.sys.service.OperateService;
import com.fcc.web.sys.service.RbacPermissionService;
import com.fcc.web.sys.service.RoleModuleRightService;

/**
 * <p>Description: 管理系统 模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/module")
public class ModuleAction extends AppWebController {

	private static Logger logger = Logger.getLogger(ModuleAction.class);
	@Resource
	private ModuleService moduleService;
	@Resource
	private OperateService operateService;
	@Resource
	private RbacPermissionService rbacPermissionService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		return "/WEB-INF/manage/sys/module_list";
	}
	
	/** 显示角色新增页面 */
	@RequestMapping("/toAdd")
	public String toAdd(HttpServletRequest request) {
		try {
			String parentId = request.getParameter("parentId");
			Module parentModule = moduleService.getModuleById(parentId);
			Map<String, Object> param = null;
			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
			request.setAttribute("parentModule", parentModule);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/module_add";
	}
	
	/** 显示角色新增页面 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			Module data = moduleService.loadModuleWithOperate(id);
			request.setAttribute("data", data);
			Map<String, Object> param = null;
			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/module_edit";
	}
	/** 为当前用户添加该模块权限 */
	private void updateUserModule(String[] operateIds, Module data, HttpServletRequest request) {
		// 为当前用户添加该模块权限
		long operateVal = 0;
		if (operateIds != null) {
			for (String oVal : operateIds) {
				Operate o = CacheUtil.operateMap.get(oVal);
				if (o != null) operateVal += o.getOperateValue();
			}
		}
		SysUser sysUser = getSysUser(request);
		Iterator<Role> it = sysUser.getRoles().iterator();
		Role role = null;
		if (it.hasNext()) {
			role = it.next();
			if (role != null && role.getRoleId() != null) {
			    roleModuleRightService.updateModuleRight(role.getRoleId(), data.getModuleId(), operateVal);
			    // 更新系统缓存角色
			}
		}
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Message add(HttpServletRequest request,
	        @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
		try {
			String parentId = request.getParameter("parentId");
			String moduleName = request.getParameter("moduleName");
			String moduleDesc = request.getParameter("moduleDesc");
			String operateValue = request.getParameter("operateValue");
			if (moduleName == null || "".equals(moduleName)) {
				throw new RefusedException("请输入模块名称！");
			}
			if (parentId == null || "".equals(parentId)) {
				parentId = Module.ROOT.getModuleId();
			}
			String[] operateIds = null;
			if (operateValue != null && !"".equals(operateValue)) {
				operateIds = operateValue.split(",");
			}
			Module parentModule = moduleService.getModuleById(parentId);
			if (parentModule == null) throw new RefusedException("请选择父模块");
			
			Module data = new Module();
			if (Module.ROOT.getModuleId().equals(parentId)) {
				data.setModuleId(RandomStringUtils.random(8, true, false));
			} else {
				data.setModuleId(parentId + "-" + RandomStringUtils.random(4, true, false));
			}
			data.setModuleDesc(moduleDesc);
			data.setModuleName(moduleName);
			data.setModuleSort(moduleSort);
			data.setModuleLevel(parentModule.getModuleLevel() + 1);
			moduleService.create(data, operateIds);
			
//			updateUserModule(operateIds, data, request);
			
			message.setSuccess(true);
			message.setMsg("保存成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("保存失败！");
		}
		return message;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public Message edit(HttpServletRequest request,
	        @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
		try {
			String moduleId = request.getParameter("id");
			String moduleName = request.getParameter("moduleName");
			String moduleDesc = request.getParameter("moduleDesc");
			String operateValue = request.getParameter("operateValue");
			if (moduleId == null || "".equals(moduleId)) {
				throw new RefusedException("请选择要修改的记录！");
			} else if (moduleName == null || "".equals(moduleName)) {
				throw new RefusedException("请输入模块名称！");
			} else if (Module.ROOT.getModuleId().equals(moduleId)) {
				throw new RefusedException("不能修改根节点！");
			}
			Module data = moduleService.getModuleById(moduleId);
			if (data == null) {
				throw new RefusedException("修改的记录不存在！");
			}
			String[] operateIds = null;
			if (operateValue != null && !"".equals(operateValue)) {
				operateIds = operateValue.split(",");
			}
			data.setModuleDesc(moduleDesc);
			data.setModuleName(moduleName);
			data.setModuleSort(moduleSort);
			moduleService.update(data, operateIds);
			
//			updateUserModule(operateIds, data, request);
			
			message.setSuccess(true);
			message.setMsg("修改成功！");
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
		}
		return message;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(HttpServletRequest request) {
		Message message = new Message();
		String moduleId = request.getParameter("id");
		try {
			if (moduleId == null || "".equals(moduleId)) {
				throw new RefusedException("请选择要删除的模块！");
			} else if (Module.ROOT.getModuleId().equals(moduleId)) {
				throw new RefusedException("无法删除根结点！");
			}
			moduleService.delete(moduleId);
			message.setMsg("删除成功！");
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("删除失败！");
		}
		return message;
	}
	
	/**
	 * 模块 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/treegrid")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<EasyuiTreeGridModule> treegrid(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeGridModule> nodeList = new ArrayList<EasyuiTreeGridModule>();
		try {
			List<Module> moduleList = moduleService.getModuleWithOperate();
			if (moduleList != null) {
				TreeSet<Module> menuSet = new TreeSet<Module>();
				menuSet.addAll(moduleList);
				
				EasyuiTreeGridModule rootModule = new EasyuiTreeGridModule();
				rootModule.setId(Module.ROOT.getModuleId());
				rootModule.setText(Module.ROOT.getModuleName());
				rootModule.setModuleSort(Module.ROOT.getModuleSort());
				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
				nodeList.add(rootModule);
				
				
				Map<String, EasyuiTreeGridModule> nodeMap = new HashMap<String, EasyuiTreeGridModule>();
				nodeMap.put(rootModule.getId(), rootModule);
				
				// 该用户权限
				TreeSet<Module> menuSetCache = (TreeSet<Module>) request.getSession().getAttribute(Constanst.SysUserSession.USER_MENU);
//				SysUser sysUserCache = getSysUser();
				
				for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
					Module m = it.next();
					
					String moduleDesc = m.getModuleDesc();
					if (moduleDesc == null || "".equals(moduleDesc)) {// 没有地址的
					} else {
						// 只能获取本身拥有的模块权限
//						if (!menuSetCache.contains(m)) {
//							continue;
//						}
					}
					
					EasyuiTreeGridModule node = new EasyuiTreeGridModule();
					node.setId(m.getModuleId());
					node.setText(m.getModuleName());
					node.setModuleDesc(moduleDesc);
					node.setModuleSort(m.getModuleSort());
					Set<Operate> operateSet = m.getOperates();
					if (operateSet != null && operateSet.size() > 0) {
						StringBuilder idSb = new StringBuilder();
						StringBuilder nameSb = new StringBuilder();
						for (Operate o : operateSet) {
							idSb.append(o.getOperateId()).append(",");
							nameSb.append(o.getOperateName()).append(",");
						}
						String operateIds = idSb.toString();
						String operateNames = nameSb.toString();
						if (operateIds.length() > 0) operateIds = operateIds.substring(0, operateIds.length() - 1);
						if (operateNames.length() > 0) operateNames = operateNames.substring(0, operateNames.length() - 1);
						node.setOperateIds(operateIds);
						node.setOperateNames(operateNames);
					}
					nodeMap.put(node.getId(), node);
					
					String parendId = m.getParentId();
					EasyuiTreeGridModule cacheNode = nodeMap.get(parendId);
					if (cacheNode != null) {
						List<EasyuiTreeNode> children = cacheNode.getChildren();
						if (children == null) children = new ArrayList<EasyuiTreeNode>();
						children.add(node);
						cacheNode.setChildren(children);
					}
				}
				nodeMap.clear();
				nodeMap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			nodeList.clear();
			EasyuiTreeGridModule node = new EasyuiTreeGridModule();
			node.setMsg(e.getMessage());
			nodeList.add(node);
		}
		return nodeList;
	}
	
	/**
	 * 模块 Tree 返回json 给 easyUI
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<EasyuiTreeNode> tree(HttpServletRequest request, HttpServletResponse response) {
		// 是否角色
		Role role = (Role) request.getSession().getAttribute(Constanst.SysUserSession.SESSION_ROLE);
		Map<String, RoleModuleRight> rightMap = (Map<String, RoleModuleRight>) request.getSession().getAttribute(Constanst.SysUserSession.SESSION_ROLE_RIGHT_MAP);
		
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
			List<Module> moduleList = moduleService.getModuleWithOperate();
			if (moduleList != null) {
				TreeSet<Module> menuSet = new TreeSet<Module>();
				menuSet.addAll(moduleList);
				
				EasyuiTreeNode rootModule = new EasyuiTreeNode();
				rootModule.setId(Module.ROOT.getModuleId());
				rootModule.setText(Module.ROOT.getModuleName());
				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
				nodeList.add(rootModule);
				
				
				Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
				nodeMap.put(rootModule.getId(), rootModule);
				// 该用户权限
				TreeSet<Module> menuSetCache = (TreeSet<Module>) request.getSession().getAttribute(Constanst.SysUserSession.USER_MENU);
				SysUser sysUserCache = getSysUser(request);

				for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
					Module m = it.next();
					// 只能获取本身拥有的模块权限
					if (ConfigUtil.isDev() == false) {
    					if (!menuSetCache.contains(m)) {
    						continue;
    					}
					}
					
					EasyuiTreeNode node = new EasyuiTreeNode();
					node.setId(m.getModuleId());
					node.setText(m.getModuleName());
					node.setAttributes(new HashMap<String, Object>());
					Set<Operate> operateSet = m.getOperates();
					if (operateSet != null && operateSet.size() > 0) {
						for (Operate o : operateSet) {
							EasyuiTreeNode nodeOperate = new EasyuiTreeNode();
							nodeOperate.setId(m.getModuleId() + ":" + o.getOperateId());
							nodeOperate.setText(o.getOperateName());
							nodeOperate.setAttributes(new HashMap<String, Object>());
							nodeOperate.getAttributes().put("operateValue", o.getOperateValue() + "");
							
							// 只能获取本身拥有的操作权限
							boolean operateFlag = true;
							if (ConfigUtil.isDev() == false) {
							    operateFlag = rbacPermissionService.checkPermissionCache(sysUserCache.getRoles(), m.getModuleId(), o.getOperateId());
							}
							if (role != null) {
								RoleModuleRight right = rightMap.get(m.getModuleId());
								if (right != null) {
									if ((o.getOperateValue() & right.getRightValue()) > 0) {
										nodeOperate.setChecked(true);
									}
								}
							}
							
							List<EasyuiTreeNode> children = node.getChildren();
							if (children == null) children = new ArrayList<EasyuiTreeNode>();
							if (operateFlag) children.add(nodeOperate);
							node.setChildren(children);
						}
					}
					nodeMap.put(node.getId(), node);
					String parendId = m.getParentId();
					EasyuiTreeNode cacheNode = nodeMap.get(parendId);
					if (cacheNode != null) {
						List<EasyuiTreeNode> children = cacheNode.getChildren();
						if (children == null) children = new ArrayList<EasyuiTreeNode>();
						children.add(node);
						cacheNode.setChildren(children);
					}
					
				}
				nodeMap.clear();
				nodeMap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			nodeList.clear();
			EasyuiTreeNode node = new EasyuiTreeNode();
			node.setMsg(e.getMessage());
			nodeList.add(node);
		}
		request.getSession().removeAttribute(Constanst.SysUserSession.SESSION_ROLE);
		request.getSession().removeAttribute(Constanst.SysUserSession.SESSION_ROLE_RIGHT_MAP);
		return nodeList;
	}
}

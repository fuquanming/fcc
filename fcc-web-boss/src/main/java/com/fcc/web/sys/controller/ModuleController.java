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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.common.Constants;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "系统模块")
@Controller
@RequestMapping("/manage/sys/module")
public class ModuleController extends AppWebController {

	private static Logger logger = Logger.getLogger(ModuleController.class);
	@Resource
	private ModuleService moduleService;
	@Resource
	private OperateService operateService;
	@Resource
	private RbacPermissionService rbacPermissionService;
	@Resource
	private RoleModuleRightService roleModuleRightService;
	
	@ApiOperation(value = "显示模块列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	public String view(HttpServletRequest request) {
	    request.setAttribute("module_root", Module.ROOT.getModuleId());
		return "manage/sys/module_list";
	}
	
	/** 显示角色新增页面 */
	@ApiOperation(value = "显示新增模块页面")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request,
	        @ApiParam(required = true, value = "父模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId) {
//		try {
//			Module parentModule = moduleService.getModuleById(parentId);
//			Map<String, Object> param = null;
//			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
//			request.setAttribute("parentModule", parentModule);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("初始化新增模块数据失败！", e);
//		}
		return "manage/sys/module_add";
	}
	
	/** 显示模块修改页面 */
	@ApiOperation(value = "显示修改模块页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id", defaultValue = "") String id) {
//		try {
//			Module data = moduleService.loadModuleWithOperate(id);
//			request.setAttribute("data", data);
//			Map<String, Object> param = null;
//			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("初始化修改模块数据失败！", e);
//		}
		return "manage/sys/module_edit";
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
	
	@ApiOperation(value = "新增模块")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request,
	        @ApiParam(required = false, value = "上级模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
	        @ApiParam(required = true, value = "模块名称") @RequestParam(name = "moduleName", defaultValue = "") String moduleName,
	        @ApiParam(required = false, value = "模块描述") @RequestParam(name = "moduleDesc", defaultValue = "") String moduleDesc,
	        @ApiParam(required = false, value = "模块操作") @RequestParam(name = "operateValue", defaultValue = "") String operateValue,
	        @ApiParam(required = false, value = "模块排序") @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
//		try {
//			if (StringUtils.isEmpty(moduleName)) throw new RefusedException(Constants.StatusCode.Module.emptyModuleName);
//			if (StringUtils.isEmpty(parentId)) parentId = Module.ROOT.getModuleId();
//			String[] operateIds = null;
//			if (!StringUtils.isEmpty(operateValue)) {
//				operateIds = operateValue.split(",");
//			}
//			Module parentModule = moduleService.getModuleById(parentId);
//			if (parentModule == null) throw new RefusedException(Constants.StatusCode.Module.emptyParentModule);
//			
//			Module data = new Module();
//			if (Module.ROOT.getModuleId().equals(parentId)) {
////				data.setModuleId(RandomStringUtils.random(8, true, false));
//			} else {
////				data.setModuleId(parentId + "-" + RandomStringUtils.random(4, true, false));
//			    data.setParentId(parentId);
//            }
//            data.setParentIds(data.buildParendIds(parentModule, data.getModuleId()));
//            
//			data.setModuleDesc(moduleDesc);
//			data.setModuleName(moduleName);
//			data.setModuleSort(moduleSort);
//			data.setModuleLevel(parentModule.getModuleLevel() + 1);
//			moduleService.add(data, operateIds);
//			
////			updateUserModule(operateIds, data, request);
//			message.setSuccess(true);
//			message.setMsg(Constants.StatusCode.Sys.success);
//		} catch (RefusedException e) {
//			message.setMsg(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("新增模块失败！", e);
//			message.setMsg(Constants.StatusCode.Sys.fail);
//			message.setObj(e.getMessage());
//		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改模块")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	public ModelAndView edit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id") String moduleId,
	        @ApiParam(required = false, value = "上级模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "模块名称") @RequestParam(name = "moduleName") String moduleName,
            @ApiParam(required = false, value = "模块描述") @RequestParam(name = "moduleDesc") String moduleDesc,
            @ApiParam(required = false, value = "模块操作") @RequestParam(name = "operateValue") String operateValue,
	        @ApiParam(required = false, value = "模块排序") @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
//		try {
//			if (StringUtils.isEmpty(moduleId)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
//			if (moduleName == null || "".equals(moduleName)) throw new RefusedException(Constants.StatusCode.Module.emptyModuleName);
//			if (Module.ROOT.getModuleId().equals(moduleId)) throw new RefusedException(Constants.StatusCode.Module.errorRootModuleId);
//			
//			if (StringUtils.isEmpty(parentId)) parentId = Module.ROOT.getModuleId();
//			Module parentModule = moduleService.getModuleById(parentId);
//            if (parentModule == null) throw new RefusedException(Constants.StatusCode.Module.emptyParentModule);
//            
//			Module data = moduleService.getModuleById(moduleId);
//			if (data == null) throw new RefusedException(Constants.StatusCode.Module.errorModuleId);
//			
//			if (Module.ROOT.getModuleId().equals(parentId)) {
//			    data.setParentId(null);
//            } else {
//                data.setParentId(parentId);
//            }
//            data.setParentIds(data.buildParendIds(parentModule, data.getModuleId()));
//			
//			String[] operateIds = null;
//			if (!StringUtils.isEmpty(operateValue)) operateIds = operateValue.split(",");
//			data.setModuleDesc(moduleDesc);
//			data.setModuleName(moduleName);
//			data.setModuleSort(moduleSort);
//			moduleService.edit(data, operateIds);
//			
////			updateUserModule(operateIds, data, request);
//			
//			message.setSuccess(true);
//			message.setMsg(Constants.StatusCode.Sys.success);
//		} catch (RefusedException e) {
//			message.setMsg(e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("修改模块失败！", e);
//			message.setMsg(Constants.StatusCode.Sys.fail);
//			message.setObj(e.getMessage());
//		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除模块")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "ids") String moduleId) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(moduleId)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			if (Module.ROOT.getModuleId().equals(moduleId)) throw new RefusedException(Constants.StatusCode.Module.errorRootModuleId);
			moduleService.delete(moduleId);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除模块失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 模块 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询模块树形列表")
	@RequestMapping(value = "/treegrid.do", method = RequestMethod.POST)
	@ResponseBody
	public List<EasyuiTreeGridModule> treegrid(HttpServletRequest request) {
		List<EasyuiTreeGridModule> nodeList = new ArrayList<EasyuiTreeGridModule>();
//		try {
//			List<Module> moduleList = moduleService.getModuleWithOperate();
//			if (moduleList != null) {
//				TreeSet<Module> menuSet = new TreeSet<Module>();
//				menuSet.addAll(moduleList);
//				
//				// 移除根目录
////				EasyuiTreeGridModule rootModule = new EasyuiTreeGridModule();
////				rootModule.setId(Module.ROOT.getModuleId());
////				rootModule.setText(Module.ROOT.getModuleName());
////				rootModule.setModuleSort(Module.ROOT.getModuleSort());
////				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
////				nodeList.add(rootModule);
//				
//				
//				Map<String, EasyuiTreeGridModule> nodeMap = new HashMap<String, EasyuiTreeGridModule>();
////				nodeMap.put(rootModule.getId(), rootModule);// 移除根目录
//				
//				// 该用户权限
//				TreeSet<Module> menuSetCache = (TreeSet<Module>) request.getSession().getAttribute(Constants.SysUserSession.menu);
////				SysUser sysUserCache = getSysUser();
//				
//				for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
//					Module m = it.next();
//					
//					String moduleDesc = m.getModuleDesc();
//					if (moduleDesc == null || "".equals(moduleDesc)) {// 没有地址的
//					} else {
//						// 只能获取本身拥有的模块权限
////						if (!menuSetCache.contains(m)) {
////							continue;
////						}
//					}
//					
//					EasyuiTreeGridModule node = new EasyuiTreeGridModule();
//					node.setId(m.getModuleId());
//					node.setText(m.getModuleName());
//					node.setModuleDesc(moduleDesc);
//					node.setModuleSort(m.getModuleSort());
//					Set<Operate> operateSet = m.getOperates();
//					if (operateSet != null && operateSet.size() > 0) {
//						StringBuilder idSb = new StringBuilder();
//						StringBuilder nameSb = new StringBuilder();
//						for (Operate o : operateSet) {
//							idSb.append(o.getOperateId()).append(",");
//							nameSb.append(o.getOperateName()).append(",");
//						}
//						String operateIds = idSb.toString();
//						String operateNames = nameSb.toString();
//						if (operateIds.length() > 0) operateIds = operateIds.substring(0, operateIds.length() - 1);
//						if (operateNames.length() > 0) operateNames = operateNames.substring(0, operateNames.length() - 1);
//						node.setOperateIds(operateIds);
//						node.setOperateNames(operateNames);
//					}
//					nodeMap.put(node.getId(), node);
//					
//					String parendId = m.getParentId();
//					EasyuiTreeGridModule cacheNode = nodeMap.get(parendId);
//					if (cacheNode != null) {
//						List<EasyuiTreeNode> children = cacheNode.getChildren();
//						if (children == null) children = new ArrayList<EasyuiTreeNode>();
//						children.add(node);
//						cacheNode.setChildren(children);
//					} else {
//                        nodeList.add(node);// 移除根目录后，添加节点
//                    }
//				}
//				nodeMap.clear();
//				nodeMap = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("查询模块树形列表失败", e);
//			nodeList.clear();
//			EasyuiTreeGridModule node = new EasyuiTreeGridModule();
//			node.setMsg(e.getMessage());
//			nodeList.add(node);
//		}
		return nodeList;
	}
	
	/**
	 * 模块 Tree 返回json 给 easyUI
	 * @return
	 */
	@ApiOperation(value = "查询模块树形数据")
	@RequestMapping(value = "/tree.do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<EasyuiTreeNode> tree(HttpServletRequest request,
	        @ApiParam(required = false, value = "节点状态，open、closed") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
	    if (StringUtils.isEmpty(nodeStatus)) nodeStatus = EasyuiTreeNode.STATE_OPEN;
		// 是否角色
		Role role = (Role) request.getSession().getAttribute(Constants.SysUserSession.sessionRole);
		Map<String, RoleModuleRight> rightMap = (Map<String, RoleModuleRight>) request.getSession().getAttribute(Constants.SysUserSession.sessionRoleRightMap);
		
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
//		try {
//			List<Module> moduleList = moduleService.getModuleWithOperate();
//			if (moduleList != null) {
//				TreeSet<Module> menuSet = new TreeSet<Module>();
//				menuSet.addAll(moduleList);
//				
//				// 移除根目录
////				EasyuiTreeNode rootModule = new EasyuiTreeNode();
////				rootModule.setId(Module.ROOT.getModuleId());
////				rootModule.setText(Module.ROOT.getModuleName());
////				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
////				nodeList.add(rootModule);
//				
//				
//				Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
////				nodeMap.put(rootModule.getId(), rootModule);// 移除根目录
//				// 该用户权限
//				TreeSet<Module> menuSetCache = (TreeSet<Module>) request.getSession().getAttribute(Constants.SysUserSession.menu);
//				SysUser sysUserCache = getSysUser(request);
//
//				for (Iterator<Module> it = menuSet.iterator(); it.hasNext();) {
//					Module m = it.next();
//					// 只能获取本身拥有的模块权限
//					if (ConfigUtil.isDev() == false) {
//    					if (!menuSetCache.contains(m)) {
//    						continue;
//    					}
//					}
//					
//					EasyuiTreeNode node = new EasyuiTreeNode();
//					node.setId(m.getModuleId());
//					node.setText(m.getModuleName());
//					node.setState(nodeStatus);
//					node.setAttributes(new HashMap<String, Object>());
//					Set<Operate> operateSet = m.getOperates();
//					if (operateSet != null && operateSet.size() > 0) {
//						for (Operate o : operateSet) {
//							EasyuiTreeNode nodeOperate = new EasyuiTreeNode();
//							nodeOperate.setId(m.getModuleId() + ":" + o.getOperateId());
//							nodeOperate.setText(o.getOperateName());
//							nodeOperate.setAttributes(new HashMap<String, Object>());
//							nodeOperate.getAttributes().put("operateValue", o.getOperateValue() + "");
//							
//							// 只能获取本身拥有的操作权限
//							boolean operateFlag = true;
//							if (ConfigUtil.isDev() == false) {
//							    operateFlag = rbacPermissionService.checkPermissionCache(sysUserCache.getRoles(), m.getModuleId(), o.getOperateId());
//							}
//							if (role != null) {
//								RoleModuleRight right = rightMap.get(m.getModuleId());
//								if (right != null) {
//									if ((o.getOperateValue() & right.getRightValue()) > 0) {
//										nodeOperate.setChecked(true);
//									}
//								}
//							}
//							
//							List<EasyuiTreeNode> children = node.getChildren();
//							if (children == null) children = new ArrayList<EasyuiTreeNode>();
//							if (operateFlag) children.add(nodeOperate);
//							node.setChildren(children);
//						}
//					}
//					nodeMap.put(node.getId(), node);
//					String parendId = m.getParentId();
//					EasyuiTreeNode cacheNode = nodeMap.get(parendId);
//					if (cacheNode != null) {
//						List<EasyuiTreeNode> children = cacheNode.getChildren();
//						if (children == null) children = new ArrayList<EasyuiTreeNode>();
//						children.add(node);
//						cacheNode.setChildren(children);
//					} else {
//                        nodeList.add(node);// 移除根目录后，添加节点
//                    }
//					
//				}
//				nodeMap.clear();
//				nodeMap = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("获取模块树形数据失败！", e.getCause());
//			nodeList.clear();
//			EasyuiTreeNode node = new EasyuiTreeNode();
//			node.setMsg(e.getMessage());
//			nodeList.add(node);
//		}
//		request.getSession().removeAttribute(Constants.SysUserSession.sessionRole);
//		request.getSession().removeAttribute(Constants.SysUserSession.sessionRoleRightMap);
		return nodeList;
	}
}

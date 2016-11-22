package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Logical;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.view.EasyuiTreeGridModule;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.model.Role;
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

	private Logger logger = Logger.getLogger(ModuleController.class);
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
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/module_list";
	}
	
	/** 显示角色新增页面 */
	@ApiOperation(value = "显示新增模块页面")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
    public String toAdd(HttpServletRequest request,
	        @ApiParam(required = true, value = "父模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId) {
		try {
			Module parentModule = moduleService.getModuleById(parentId);
			if (parentModule != null) {
                if (Module.ROOT.getModuleId().equals(parentModule.getParentId())) {
                    parentModule.setParentId(null);
                }
            }
			request.setAttribute("parentModule", parentModule);
			Map<String, Object> param = null;
			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化新增模块数据失败！", e);
		}
		return "manage/sys/module_add";
	}
	
	/** 显示模块修改页面 */
	@ApiOperation(value = "显示修改模块页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
			Module data = moduleService.loadModuleWithOperate(id);
			if (data != null) {
			    if (Module.ROOT.getModuleId().equals(data.getParentId())) {
			        data.setParentId(null);
			    }
			}
			request.setAttribute("data", data);
			Map<String, Object> param = null;
			request.setAttribute("operateList", operateService.queryPage(1, 0, param).getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化修改模块数据失败！", e);
		}
		return "manage/sys/module_edit";
	}
	
	/** 为当前用户添加该模块权限 */
	private void updateUserModule(String[] operateIds, Module data, HttpServletRequest request) {
		// 为当前用户添加该模块权限
		SysUser sysUser = getSysUser(request);
		Iterator<Role> it = sysUser.getRoles().iterator();
		Role role = null;
		if (it.hasNext()) {
			role = it.next();
			if (role != null && role.getRoleId() != null) {
			    roleModuleRightService.editRight(role.getRoleId(), data.getModuleId(), Long.MAX_VALUE);
			    // 更新系统缓存角色
			}
		}
	}
	
	@ApiOperation(value = "新增模块")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(HttpServletRequest request,
	        @ApiParam(required = false, value = "上级模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
	        @ApiParam(required = true, value = "模块名称") @RequestParam(name = "moduleName", defaultValue = "") String moduleName,
	        @ApiParam(required = false, value = "模块描述") @RequestParam(name = "moduleDesc", defaultValue = "") String moduleDesc,
	        @ApiParam(required = false, value = "模块操作") @RequestParam(name = "operateValue", defaultValue = "") String operateValue,
	        @ApiParam(required = false, value = "模块排序") @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(moduleName)) throw new RefusedException(Constants.StatusCode.Module.emptyModuleName);
			if (StringUtils.isEmpty(parentId)) parentId = Module.ROOT.getModuleId();
			String[] operateIds = null;
			if (!StringUtils.isEmpty(operateValue)) {
				operateIds = StringUtils.split(operateValue, ",");
			}
			Module parentModule = moduleService.getModuleById(parentId);
			if (parentModule == null) throw new RefusedException(Constants.StatusCode.Module.emptyParentModule);
			
			Module data = new Module();
			if (Module.ROOT.getModuleId().equals(parentId)) {
				data.setModuleId(RandomStringUtils.random(4, true, false));
				data.setParentId(parentId);
			} else {
				data.setModuleId(RandomStringUtils.random(4, true, false));
			    data.setParentId(parentId);
            }
            data.setParentIds(data.buildParendIds(parentModule, data.getModuleId()));
            
			data.setModuleDesc(moduleDesc);
			data.setModuleName(moduleName);
			data.setModuleSort(moduleSort);
			data.setModuleLevel(parentModule.getModuleLevel() + 1);
			moduleService.add(data, operateIds);
			
			updateUserModule(operateIds, data, request);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
			
			reloadModuleCache();
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增模块失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改模块")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id") String moduleId,
	        @ApiParam(required = false, value = "上级模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "模块名称") @RequestParam(name = "moduleName") String moduleName,
            @ApiParam(required = false, value = "模块描述") @RequestParam(name = "moduleDesc") String moduleDesc,
            @ApiParam(required = false, value = "模块操作") @RequestParam(name = "operateValue") String operateValue,
	        @ApiParam(required = false, value = "模块排序") @RequestParam(name = "moduleSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(moduleId)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
			if (moduleName == null || "".equals(moduleName)) throw new RefusedException(Constants.StatusCode.Module.emptyModuleName);
			if (Module.ROOT.getModuleId().equals(moduleId)) throw new RefusedException(Constants.StatusCode.Module.errorRootModuleId);
			
			if (StringUtils.isEmpty(parentId)) parentId = Module.ROOT.getModuleId();
			Module parentModule = moduleService.getModuleById(parentId);
            if (parentModule == null) throw new RefusedException(Constants.StatusCode.Module.emptyParentModule);
            
			Module data = moduleService.getModuleById(moduleId);
			if (data == null) throw new RefusedException(Constants.StatusCode.Module.errorModuleId);
			
			if (Module.ROOT.getModuleId().equals(parentId)) {
			    data.setParentId(Module.ROOT.getModuleId());
            } else {
                data.setParentId(parentId);
            }
            data.setParentIds(data.buildParendIds(parentModule, data.getModuleId()));
			
			String[] operateIds = null;
			if (!StringUtils.isEmpty(operateValue)) operateIds = StringUtils.split(operateValue, ",");
			data.setModuleDesc(moduleDesc);
			data.setModuleName(moduleName);
			data.setModuleSort(moduleSort);
			data.setModuleLevel(parentModule.getModuleLevel() + 1);
			moduleService.edit(data, operateIds);
			
			updateUserModule(operateIds, data, request);
			
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
			
			reloadModuleCache();
			reloadRoleModuleRightCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改模块失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除模块")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "ids") String moduleId) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(moduleId)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			if (Module.ROOT.getModuleId().equals(moduleId)) throw new RefusedException(Constants.StatusCode.Module.errorRootModuleId);
			moduleService.delete(moduleId);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
			reloadModuleCache();
			reloadRoleModuleRightCache();
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
	@Permissions("view")
	public List<EasyuiTreeGridModule> treegrid(HttpServletRequest request) {
		List<EasyuiTreeGridModule> nodeList = null;
		try {
		    nodeList = moduleService.getModuleTreeGrid(null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询模块树形列表失败", e);
			nodeList = new ArrayList<EasyuiTreeGridModule>();
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
	@ApiOperation(value = "查询模块树形数据")
	@RequestMapping(value = "/tree.do", method = RequestMethod.POST)
	@ResponseBody
	@Permissions(value = {"add", "edit"}, logical = Logical.OR)
	public List<EasyuiTreeNode> tree(HttpServletRequest request,
	        @ApiParam(required = false, value = "节点状态，open、closed") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
	    if (StringUtils.isEmpty(nodeStatus)) nodeStatus = EasyuiTreeNode.STATE_OPEN;
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
		    // 查询所有模块
		    nodeList = moduleService.getModuleTree(null, nodeStatus, false, null);
		} catch (Exception e) {
		    e.printStackTrace();
            logger.error("查询模块树形失败", e);
            nodeList = new ArrayList<EasyuiTreeNode>();
            EasyuiTreeGridModule node = new EasyuiTreeGridModule();
            node.setMsg(e.getMessage());
            nodeList.add(node);
        }
		return nodeList;
	}
}

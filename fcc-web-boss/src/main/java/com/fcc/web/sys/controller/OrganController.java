package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiTreeGridOrgan;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.OrganizationService;
import com.fcc.web.sys.service.SysUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/organ")
public class OrganController extends AppWebController {

	private Logger logger = Logger.getLogger(OrganController.class);
	@Resource
	private OrganizationService organService;
	@Resource
	private SysUserService sysUserService;
	
	@ApiOperation(value = "显示机构列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		request.setAttribute("organId", getSysUser(request).getDept());
		return "manage/sys/organ_list";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示新增机构页面")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
	public String toAdd(HttpServletRequest request,
	        @ApiParam(required = true, value = "父模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId) {
		try {
			Organization data = organService.getOrganById(parentId);
			request.setAttribute("parent", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化新增机构数据失败！", e);
		}
		return "manage/sys/organ_add";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示修改机构页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
			Organization data = organService.getOrganById(id);
			request.setAttribute("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化修改机构数据失败！", e);
		}
		return "manage/sys/organ_edit";
	}
	
	@ApiOperation(value = "新增机构")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(HttpServletRequest request,
	        @ApiParam(required = false, value = "上级机构ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "机构名称") @RequestParam(name = "organName", defaultValue = "") String organName,
            @ApiParam(required = false, value = "机构编码") @RequestParam(name = "organCode", defaultValue = "") String organCode,
            @ApiParam(required = false, value = "机构排序") @RequestParam(name = "organSort", defaultValue = "1") int organSort,
            @ApiParam(required = false, value = "机构描述") @RequestParam(name = "organDesc", defaultValue = "") String organDesc,
            @ApiParam(required = false, value = "机构状态") @RequestParam(name = "organStatus", defaultValue = "true") String organStatus
            ) {
		Message message = new Message();
		try {
			if (organName == null || "".equals(organName)) throw new RefusedException(Constants.StatusCode.Organization.emptyOrganizationName);
			if (parentId == null || "".equals(parentId)) parentId = Organization.ROOT.getOrganId();
			Organization parent = organService.getOrganById(parentId);
			if (parent == null) throw new RefusedException(Constants.StatusCode.Organization.emptyParentOrganization);
			
//			String organId = getSysUser(request).getDept();
//			if (StringUtils.isNotEmpty(organId)) {// 非管理员
//				int indexOf = parentId.indexOf(organId);
//				if (indexOf != 0) {
//					throw new RefusedException("不能添加上级机构！");
//				}
//			}
			
			Organization data = new Organization();
			data.setOrganId(RandomStringUtils.random(6, true, false));
			data.setParentId(parentId);
			data.setParentIds(data.buildParendIds(parent, data.getOrganId()));
			
			data.setOrganCode(organCode);
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(organSort);
			data.setOrganLevel(parent.getOrganLevel() + 1);
			
			data.setOrganStatus(Boolean.valueOf(organStatus));
			
			organService.add(data);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增机构失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改机构")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request,
	        @ApiParam(required = true, value = "机构ID") @RequestParam(name = "id") String organId,
	        @ApiParam(required = false, value = "上级机构ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "机构名称") @RequestParam(name = "organName", defaultValue = "") String organName,
            @ApiParam(required = false, value = "机构编码") @RequestParam(name = "organCode", defaultValue = "") String organCode,
            @ApiParam(required = false, value = "机构描述") @RequestParam(name = "organDesc", defaultValue = "") String organDesc,
            @ApiParam(required = false, value = "机构排序") @RequestParam(name = "organSort", defaultValue = "1") int organSort,
            @ApiParam(required = false, value = "机构状态") @RequestParam(name = "organStatus", defaultValue = "true") String organStatus
            ) {
		Message message = new Message();
		try {
			if (organId == null || "".equals(organId)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
			if (organName == null || "".equals(organName)) throw new RefusedException(Constants.StatusCode.Organization.emptyOrganizationName);
			if (Organization.ROOT.getOrganId().equals(organId)) throw new RefusedException(Constants.StatusCode.Organization.errorRootOrganizationId);
//			if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否修改上级组织机构
//				throw new RefusedException("不能修改上级机构！");
//			}
			if (organId.equals(parentId)) throw new RefusedException(Constants.StatusCode.Organization.errorParentOneself);
			if (parentId == null || "".equals(parentId)) parentId = Organization.ROOT.getOrganId();
			
            Organization parent = organService.getOrganById(parentId);
            if (parent == null) throw new RefusedException(Constants.StatusCode.Organization.emptyParentOrganization);
			
			Organization data = organService.getOrganById(organId);
			if (data == null) throw new RefusedException(Constants.StatusCode.Organization.errorOrganizationId);
            data.setParentId(parentId);
            data.setParentIds(data.buildParendIds(parent, data.getOrganId()));
			
			data.setOrganCode(organCode);
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(organSort);
			data.setOrganLevel(parent.getOrganLevel() + 1);
			
			data.setOrganStatus(Boolean.valueOf(organStatus));
			
			organService.edit(data);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改机构失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "显示、隐藏机构")
    @RequestMapping(value = "/show.do", method = RequestMethod.POST)
    @Permissions("edit")
    public ModelAndView show(HttpServletRequest request,
            @ApiParam(required = true, value = "机构ID、用,分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id,
            @ApiParam(required = true, value = "机构状态") @RequestParam(name = "organStatus", defaultValue = "") String organStatus) {
        Message message = new Message();
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
            String[] ids = StringUtils.split(id, ",");
            boolean show = false;
            if ("show".equals(organStatus)) {
                show = true;
            }
            organService.editOrganStatus(ids, show);
            reloadModuleCache();
            message.setMsg(StatusCode.Sys.success);
            message.setSuccess(true);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("显示、隐藏机构失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
	
	@ApiOperation(value = "删除机构")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "ids") String organId) {
		Message message = new Message();
		try {
			if (organId == null || "".equals(organId)) new RefusedException(StatusCode.Sys.emptyDeleteId);
			if (Organization.ROOT.getOrganId().equals(organId)) throw new RefusedException(Constants.StatusCode.Organization.errorRootOrganizationId);
//			if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否删除上级组织机构
//				throw new RefusedException("不能删除上级机构！");
//			}
			// 判断是否有归属人员
			Map<String, Object> param = new HashMap<String, Object>(1);
			param.put("dept", organId);
			ListPage listPage = sysUserService.queryPage(1, 10, param);
			if (listPage.getTotalSize() > 0) throw new RefusedException(Constants.StatusCode.Organization.hasUser);
			
			organService.delete(organId);
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除机构失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 组织机构 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "所有机构树形列表结构")
	@RequestMapping(value = "/treegrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
	public List<EasyuiTreeGridOrgan> treegrid(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeGridOrgan> nodeList = null;
		try {
		    SysUser sysUser = null;
            if (isGroup()) {
                sysUser = getSysUser(request);
            }
		    nodeList = organService.getOrganTreeGrid(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载机构树形列表失败！", e);
			nodeList = new ArrayList<EasyuiTreeGridOrgan>(1);
			EasyuiTreeGridOrgan node = new EasyuiTreeGridOrgan();
			node.setMsg(e.getMessage());
			nodeList.add(node);
		}
		return nodeList;
	}
	
	/**
	 * 组织机构 Tree 返回json 给 easyUI
	 * @return
	 */
	@ApiOperation(value = "所有机构树形数据")
	@RequestMapping(value = "/tree.do", method = RequestMethod.POST)
    @ResponseBody
	public List<EasyuiTreeNode> tree(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeNode> nodeList = null;
		try {
		    SysUser sysUser = null;
		    if (isGroup()) {
		        sysUser = getSysUser(request);
		    }
		    nodeList = organService.getOrganTree(sysUser);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载机构树形失败！", e);
			nodeList = new ArrayList<EasyuiTreeNode>(1); 
			EasyuiTreeNode node = new EasyuiTreeNode();
			node.setMsg(e.getMessage());
			nodeList.add(node);
		}
		return nodeList;
	}

}

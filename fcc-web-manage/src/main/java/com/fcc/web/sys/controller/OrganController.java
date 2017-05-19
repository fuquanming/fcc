package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.model.Treeable;
import com.fcc.commons.web.service.TreeableService;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
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
	private TreeableService treeableService;
	@Resource
	private SysUserService sysUserService;
	
	@ApiOperation(value = "显示机构列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
//		request.setAttribute("organId", getSysUser(request).getDept());
		return "manage/sys/organ_list";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示新增机构页面")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
	public String toAdd(HttpServletRequest request,
	        @ApiParam(required = true, value = "父模块ID") @RequestParam(name = "parentId", defaultValue = "") String parentId) {
		try {
		    request.setAttribute("parent", treeableService.getTreeableById(Organization.class, parentId));
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
			Organization data = (Organization) treeableService.getTreeableById(Organization.class, id);
            request.setAttribute("data", data);
            request.setAttribute("parent", treeableService.getTreeableById(Organization.class, data.getParentId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化修改机构数据失败！", e);
		}
		return "manage/sys/organ_edit";
	}
	
	@ApiOperation(value = "新增机构")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(Organization data, HttpServletRequest request) {
		Message message = new Message();
		try {
		    String nodeName = request.getParameter("nodeNameStr");
            data.setNodeName(nodeName);
            String parentId = data.getParentId();
            Treeable parent = null;
            if (StringUtils.isEmpty(nodeName)) throw new RefusedException(StatusCode.Treeable.emptyName);
            if (StringUtils.isEmpty(parentId)) {
                parentId = Treeable.ROOT.getNodeId();
                parent = Treeable.ROOT;
            } else {
                parent = (Treeable) treeableService.getTreeableById(Organization.class, parentId);    
            }
            if (parent == null) throw new RefusedException(StatusCode.Treeable.emptyParent);
            String nodeCode = data.getNodeCode();
            if (nodeCode != null && !"".equals(nodeCode) && treeableService.checkNodeCode(Organization.class, data.getNodeCode(), null) == true) {
                throw new RefusedException(StatusCode.Treeable.existCode);
            }
            data.setNodeId(RandomStringUtils.random(6, true, true));
            data.setParentId(parentId);
            data.setCreateUser(getSysUser().getUserId());
            data.setCreateTime(new Date());
            
            treeableService.add(data, parent);
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
	public ModelAndView edit(Organization organ, HttpServletRequest request) {
		Message message = new Message();
		try {
		    String nodeId = request.getParameter("id");
		    organ.setNodeId(nodeId);
            String nodeName = request.getParameter("nodeNameStr");
            organ.setNodeName(nodeName);
            String parentId = organ.getParentId();
            if (nodeId == null || "".equals(nodeId)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
            if (nodeName == null || "".equals(nodeName)) throw new RefusedException(StatusCode.Treeable.emptyName);
            if (Treeable.ROOT.getNodeId().equals(nodeId)) throw new RefusedException(StatusCode.Treeable.errorRootId);
            if (nodeId.equals(parentId)) throw new RefusedException(StatusCode.Treeable.errorParentOneself);
            Treeable parent = null;
            if (parentId == null || "".equals(parentId)) {
                parentId = Treeable.ROOT.getNodeId();
                parent = Treeable.ROOT;
            } else {
                parent = (Organization) treeableService.getTreeableById(Organization.class, parentId);
            }
            if (parent == null) throw new RefusedException(StatusCode.Treeable.emptyParent);
            String nodeCode = organ.getNodeCode();
            if (nodeCode != null && !"".equals(nodeCode) && treeableService.checkNodeCode(Organization.class, organ.getNodeCode(), organ.getNodeId()) == true) {
                throw new RefusedException(StatusCode.Treeable.existCode);
            }
            organ.setParentId(parentId);
            
            Organization data = (Organization) treeableService.getTreeableById(Organization.class, nodeId);
            if (data == null) throw new RefusedException(StatusCode.Treeable.errorId);
            organ.setCreateTime(data.getCreateTime());
            organ.setCreateUser(data.getCreateUser());
            
            BeanUtils.copyProperties(organ, data);
            
            data.setUpdateTime(new Date());
            data.setUpdateUser(getSysUser().getUserId());
            
            treeableService.edit(data, parent);
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
            @ApiParam(required = true, value = "机构状态") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
        Message message = new Message();
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
            String[] ids = StringUtils.split(id, ",");
            boolean show = false;
            if ("show".equals(nodeStatus)) {
                show = true;
            }
            treeableService.editStatus(Organization.class, ids, show);
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
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "ids") String nodeId) {
		Message message = new Message();
		try {
//			if (organId == null || "".equals(organId)) new RefusedException(StatusCode.Sys.emptyDeleteId);
//			if (Organization.ROOT.getOrganId().equals(organId)) throw new RefusedException(Constants.StatusCode.Organization.errorRootOrganizationId);
////			if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否删除上级组织机构
////				throw new RefusedException("不能删除上级机构！");
////			}
//			// 判断是否有归属人员
//			Map<String, Object> param = new HashMap<String, Object>(1);
//			param.put("dept", organId);
//			ListPage listPage = sysUserService.queryPage(1, 10, param);
//			if (listPage.getTotalSize() > 0) throw new RefusedException(Constants.StatusCode.Organization.hasUser);
//			
//			organService.delete(organId);
//			message.setMsg(StatusCode.Sys.success);
//			message.setSuccess(true);
		    
		    if (nodeId == null || "".equals(nodeId)) new RefusedException(StatusCode.Sys.emptyDeleteId);
            if (Treeable.ROOT.getNodeId().equals(nodeId)) throw new RefusedException(StatusCode.Treeable.errorRootId);
//          if (OrganUtil.checkParent(getSysUser(request), nodeId)) {// 判断是否删除上级组织机构
//              throw new RefusedException("不能删除上级机构！");
//          }
            treeableService.delete(Organization.class, nodeId);
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
	public List<EasyuiTreeNode> treegrid(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeNode> nodeList = null;
		try {
//		    SysUser sysUser = null;
//            if (isGroup()) {
//                sysUser = getSysUser(request);
//            }
//		    nodeList = organService.getOrganTreeGrid(sysUser);
		    
//		    Map<String, Object> params = getParams(request);
//		    if (params.get(parentIdKey) == null) {
//		        String parentId = null;
//	            if (isGroup()) {
//	                parentId = getSysUser(request).getDept();
//	            } else {
//	                parentId = Treeable.ROOT.getNodeId();
//	            }
//	            params.put(parentIdKey, parentId);
//		    }
//            nodeList = treeableService.getTreeGrid(Organization.class, params);
		    
		    Map<String, Object> params = getParams(request);
            boolean searchFlag = false;
            if (params.size() > 0) {// 检索查询
                searchFlag = true;
            }
            String parentId = request.getParameter("parentId");
            if (StringUtils.isNotEmpty(parentId)) {
                params.put("parentId", parentId);
            } else {
                if (isGroup()) {
                    parentId = getSysUser().getDept();
                }
            }
            if (searchFlag) {
                nodeList = treeableService.getTreeGrid(Organization.class, params);
            } else {
                String all = request.getParameter("all");
                boolean allFlag = false;
                if (StringUtils.isNotEmpty(all)) {
                    allFlag = true;
                }
                nodeList = treeableService.getTree(Organization.class, parentId, allFlag, false);
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载机构树形列表失败！", e);
			nodeList = new ArrayList<EasyuiTreeNode>(1);
			EasyuiTreeNode node = new EasyuiTreeNode();
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
	public List<EasyuiTreeNode> tree(HttpServletRequest request,
            @ApiParam(required = false, value = "节点ID") @RequestParam(name = "id", defaultValue = "") String nodeId, 
            @ApiParam(required = false, value = "是否code为ID") @RequestParam(name = "codeIdFlag", defaultValue = "false") boolean codeIdFlag,
            @ApiParam(required = false, value = "是否全部节点") @RequestParam(name = "all", defaultValue = "true") String all,
            @ApiParam(required = false, value = "是否包含父节点") @RequestParam(name = "parent", defaultValue = "false") String parent) {
		List<EasyuiTreeNode> nodeList = null;
		try {
		    if (isGroup()) {
		        SysUser sysUser = getSysUser();
		        nodeId = sysUser.getDept();
		    }
//		    nodeList = organService.getOrganTree(sysUser);
		    if (codeIdFlag) {
		        nodeList = treeableService.getTreeCode(Organization.class, nodeId, Boolean.valueOf(all), Boolean.valueOf(parent));
		    } else {
		        nodeList = treeableService.getTree(Organization.class, nodeId, Boolean.valueOf(all), Boolean.valueOf(parent));
		    }
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

	private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            String nodeId = request.getParameter("nodeId");
            if (StringUtils.isNotEmpty(nodeId)) {
                param.put("nodeId", nodeId);
            }
            String nodeName = request.getParameter("nodeNameStr");
            if (StringUtils.isNotEmpty(nodeName)) {
                param.put("nodeName", "%" + nodeName + "%");
            }
            String nodeCode = request.getParameter("nodeCode");
            if (StringUtils.isNotEmpty(nodeCode)) {
                param.put("nodeCode", "%" + nodeCode + "%");
            }
            String nodeLevel = request.getParameter("nodeLevel");
            if (StringUtils.isNotEmpty(nodeLevel)) {
                param.put("nodeLevel", nodeLevel);
            }
            String nodeSort = request.getParameter("nodeSort");
            if (StringUtils.isNotEmpty(nodeSort)) {
                param.put("nodeSort", nodeSort);
            }
            String nodeDesc = request.getParameter("nodeDesc");
            if (StringUtils.isNotEmpty(nodeDesc)) {
                param.put("nodeDesc", nodeDesc);
            }
            String nodeStatus = request.getParameter("nodeStatus");
            if (StringUtils.isNotEmpty(nodeStatus)) {
                param.put("nodeStatus", nodeStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

}

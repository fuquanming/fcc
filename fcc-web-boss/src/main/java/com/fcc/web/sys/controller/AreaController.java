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
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.Area;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 地区</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/area")
public class AreaController extends AppWebController {

	private Logger logger = Logger.getLogger(AreaController.class);
	@Resource
	private TreeableService treeableService;
	
	@ApiOperation(value = "显示地区列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/area/area_list";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示新增地区")
	@RequestMapping(value = "/toAdd.do", method = RequestMethod.GET)
	@Permissions("add")
	public String toAdd(HttpServletRequest request,
	        @ApiParam(required = true, value = "父ID") @RequestParam(name = "parentId", defaultValue = "") String parentId) {
		try {
			request.setAttribute("parent", treeableService.getTreeableById(Area.class, parentId));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化新增地区失败！", e);
		}
		return "manage/sys/area/area_add";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示修改地区页面")
	@RequestMapping(value = "/toEdit.do", method = RequestMethod.GET)
	@Permissions("edit")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = true, value = "地区ID") @RequestParam(name = "id", defaultValue = "") String id) {
		try {
		    Area area = (Area) treeableService.getTreeableById(Area.class, id);
			request.setAttribute("data", area);
			request.setAttribute("parent", treeableService.getTreeableById(Area.class, area.getParentId()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化修改地区数据失败！", e);
		}
		return "manage/sys/area/area_edit";
	}
	
	@ApiOperation(value = "新增地区")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(Area area, HttpServletRequest request) {
		Message message = new Message();
		try {
		    String nodeName = request.getParameter("nodeNameStr");
		    area.setNodeName(nodeName);
		    String parentId = area.getParentId();
			if (StringUtils.isEmpty(nodeName)) throw new RefusedException(StatusCode.Treeable.emptyName);
			Treeable parent = null;
			if (StringUtils.isEmpty(parentId)) {
			    parentId = Treeable.ROOT.getNodeId();
			    parent = Treeable.ROOT;
            } else {
                parent = treeableService.getTreeableById(Area.class, parentId);
            }
			if (parent == null) throw new RefusedException(StatusCode.Treeable.emptyParent);
			if (treeableService.checkNodeCode(Area.class, area.getNodeCode(), null) == true) {
			    throw new RefusedException(StatusCode.Treeable.existCode);
			}
			
			area.setNodeId(RandomStringUtils.random(6, true, true));
			area.setParentId(parentId);
			area.setCreateUser(getSysUser().getUserId());
			area.setCreateTime(new Date());
			
			treeableService.add(area, parent);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增地区失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改机构")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(Area area, HttpServletRequest request) {
		Message message = new Message();
		try {
		    String nodeId = request.getParameter("id");
		    area.setNodeId(nodeId);
		    String nodeName = request.getParameter("nodeNameStr");
            area.setNodeName(nodeName);
		    String parentId = area.getParentId();
			if (nodeId == null || "".equals(nodeId)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
			if (nodeName == null || "".equals(nodeName)) throw new RefusedException(StatusCode.Treeable.emptyName);
			if (Treeable.ROOT.getNodeId().equals(nodeId)) throw new RefusedException(StatusCode.Treeable.errorRootId);
			if (nodeId.equals(parentId)) throw new RefusedException(StatusCode.Treeable.errorParentOneself);
			Treeable parent = null;
			if (parentId == null || "".equals(parentId)) {
			    parentId = Treeable.ROOT.getNodeId();
			    parent = Treeable.ROOT;
			} else {
			    parent = treeableService.getTreeableById(Area.class, parentId);
			}
            if (parent == null) throw new RefusedException(StatusCode.Treeable.emptyParent);
			
            Area data = (Area) treeableService.getTreeableById(Area.class, nodeId);
			if (data == null) throw new RefusedException(StatusCode.Treeable.errorId);
			if (treeableService.checkNodeCode(Area.class, area.getNodeCode(), data.getNodeId()) == true) {
                throw new RefusedException(StatusCode.Treeable.existCode);
            }
			BeanUtils.copyProperties(area, data);
			
			data.setUpdateTime(new Date());
			data.setUpdateUser(getSysUser().getUserId());
			
			treeableService.edit(data, parent);
			message.setSuccess(true);
			message.setMsg(StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改地区失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "显示、隐藏地区")
    @RequestMapping(value = "/show.do", method = RequestMethod.POST)
    @Permissions("edit")
    public ModelAndView show(HttpServletRequest request,
            @ApiParam(required = true, value = "ID、用,分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id,
            @ApiParam(required = true, value = "状态") @RequestParam(name = "nodeStatus", defaultValue = "") String nodeStatus) {
        Message message = new Message();
        try {
            if (id == null || "".equals(id)) throw new RefusedException(StatusCode.Sys.emptyUpdateId);
            String[] ids = StringUtils.split(id, ",");
            boolean show = false;
            if ("show".equals(nodeStatus)) {
                show = true;
            }
            treeableService.editStatus(Area.class, ids, show);
            reloadModuleCache();
            message.setMsg(StatusCode.Sys.success);
            message.setSuccess(true);
        } catch (RefusedException e) {
            message.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("显示、隐藏地区失败！", e);
            message.setMsg(StatusCode.Sys.fail);
            message.setObj(e.getMessage());
        }
        return getModelAndView(message);
    }
	
	@ApiOperation(value = "删除地区")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "ID、用,分割多个ID") @RequestParam(name = "ids") String nodeId) {
		Message message = new Message();
		try {
			if (nodeId == null || "".equals(nodeId)) new RefusedException(StatusCode.Sys.emptyDeleteId);
			if (Treeable.ROOT.getNodeId().equals(nodeId)) throw new RefusedException(StatusCode.Treeable.errorRootId);
			treeableService.delete(Area.class, nodeId);
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除地区失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 组织机构 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "所有地区树形列表结构")
	@RequestMapping(value = "/treegrid.do", method = RequestMethod.POST)
    @ResponseBody
    @Permissions("view")
	public List<EasyuiTreeNode> treegrid(EasyuiDataGrid dg, HttpServletRequest request, HttpServletResponse response) {
	    List<EasyuiTreeNode> nodeList = null;
        try {
            Map<String, Object> params = getParams(request);
            boolean searchFlag = false;
            if (params.size() > 0) {// 检索查询
                searchFlag = true;
            }
            String parentId = request.getParameter("parentId");
            if (StringUtils.isNotEmpty(parentId)) {
                params.put("parentId", parentId);
            }
            if (searchFlag) {
                nodeList = treeableService.getTreeGrid(Area.class, params);
            } else {
                String all = request.getParameter("all");
                boolean allFlag = false;
                if (StringUtils.isNotEmpty(all)) {
                    allFlag = true;
                }
                nodeList = treeableService.getTree(Area.class, parentId, allFlag, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载地区树形表格失败！", e);
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
	@ApiOperation(value = "所有地区树形数据")
	@RequestMapping(value = "/tree.do", method = RequestMethod.POST)
    @ResponseBody
	public List<EasyuiTreeNode> tree(HttpServletRequest request,
	        @ApiParam(required = true, value = "节点ID") @RequestParam(name = "id", defaultValue = "") String nodeId, 
	        @ApiParam(required = false, value = "是否全部节点") @RequestParam(name = "all", defaultValue = "true") String all,
	        @ApiParam(required = false, value = "是否包含父节点") @RequestParam(name = "parent", defaultValue = "false") String parent) {
		List<EasyuiTreeNode> nodeList = null;
		try {
		    nodeList = treeableService.getTree(Area.class, nodeId, Boolean.valueOf(all), Boolean.valueOf(parent));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("加载地区树形失败！", e);
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

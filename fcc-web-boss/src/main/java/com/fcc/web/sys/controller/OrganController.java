package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiTreeGridOrgan;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Organization;
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

	private static Logger logger = Logger.getLogger(OrganController.class);
	@Resource
	private OrganizationService organService;
	@Resource
	private SysUserService sysUserService;
	
	@ApiOperation(value = "显示机构列表页面")
	@GetMapping(value = "/view.do")
	public String view(HttpServletRequest request) {
		request.setAttribute("organId", getSysUser(request).getDept());
		return "manage/sys/organ_list";
	}
	
	/** 显示组织机构新增页面 */
	@ApiOperation(value = "显示新增机构页面")
	@GetMapping("/toAdd.do")
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
	@GetMapping("/toEdit.do")
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
	
	@ApiOperation(value = "显示修改机构页面")
	@PostMapping("/add.do")
	@ResponseBody
	public Message add(HttpServletRequest request,
	        @ApiParam(required = false, value = "上级机构ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "机构名称") @RequestParam(name = "organName", defaultValue = "") String organName,
            @ApiParam(required = false, value = "机构描述") @RequestParam(name = "organDesc", defaultValue = "") String organDesc,
            @ApiParam(required = false, value = "机构排序") @RequestParam(name = "organSort", defaultValue = "1") int organSort) {
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
			if (Organization.ROOT.getOrganId().equals(parentId)) {
				data.setOrganId(RandomStringUtils.random(8, true, false));
			} else {
				data.setOrganId(parentId + "-" + RandomStringUtils.random(4, true, false));
			}
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(organSort);
			data.setOrganLevel(parent.getOrganLevel() + 1);
			organService.create(data);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增机构失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return message;
	}
	
	@ApiOperation(value = "修改机构")
	@PostMapping("/edit.do")
	@ResponseBody
	public Message edit(HttpServletRequest request,
	        @ApiParam(required = true, value = "机构ID") @RequestParam(name = "id") String organId,
	        @ApiParam(required = false, value = "上级机构ID") @RequestParam(name = "parentId", defaultValue = "") String parentId,
            @ApiParam(required = true, value = "机构名称") @RequestParam(name = "organName", defaultValue = "") String organName,
            @ApiParam(required = false, value = "机构描述") @RequestParam(name = "organDesc", defaultValue = "") String organDesc,
            @ApiParam(required = false, value = "机构排序") @RequestParam(name = "organSort", defaultValue = "1") int organSort) {
		Message message = new Message();
		try {
			if (organId == null || "".equals(organId)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
			if (organName == null || "".equals(organName)) throw new RefusedException(Constants.StatusCode.Organization.emptyOrganizationName);
			if (Organization.ROOT.getOrganId().equals(organId)) throw new RefusedException(Constants.StatusCode.Organization.errorRootOrganizationId);
//			if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否修改上级组织机构
//				throw new RefusedException("不能修改上级机构！");
//			}
			Organization data = organService.getOrganById(organId);
			if (data == null) throw new RefusedException(Constants.StatusCode.Organization.errorOrganizationId);
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(organSort);
			organService.update(data);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改机构失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return message;
	}
	
	@ApiOperation(value = "删除机构")
    @PostMapping(value = "/delete.do")
    @ResponseBody
	public Message delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "模块ID") @RequestParam(name = "id") String organId) {
		Message message = new Message();
		try {
			if (organId == null || "".equals(organId)) new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			if (Organization.ROOT.getOrganId().equals(organId)) throw new RefusedException(Constants.StatusCode.Organization.errorRootOrganizationId);
//			if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否删除上级组织机构
//				throw new RefusedException("不能删除上级机构！");
//			}
			// 判断是否有归属人员
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dept", organId);
			ListPage listPage = sysUserService.queryPage(1, 10, param);
			if (listPage.getTotalSize() > 0) throw new RefusedException(Constants.StatusCode.Organization.hasUser);
			
			organService.delete(organId);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除机构失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return message;
	}
	
	/**
	 * 组织机构 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "所有机构树形列表结构")
    @PostMapping(value = "/treegrid.do")
    @ResponseBody
	public List<EasyuiTreeGridOrgan> treegrid(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeGridOrgan> nodeList = new ArrayList<EasyuiTreeGridOrgan>();
		try {
			List<Organization> dataList = null;
			
			// 系统管理员没有组织机构
//			Set<String> parentOrganIdSet = new HashSet<String>();
//			String organId = getSysUser(request).getDept();
//			if (isAdmin(request)) {// 管理员无组织机构
				dataList = organService.getOrgans();
//			} else {
//				String tempOrganId = organId;
//				while (true) {
//					String parentId = Organization.getParentOrganId(tempOrganId);
//					if (Organization.ROOT.getOrganId().equals(parentId)) {
//						break;
//					} else {
//						parentOrganIdSet.add(parentId);
//						tempOrganId = parentId;
//					}
//				}
//				// 制定ID查询
//				dataList = organService.findChildOrgans(organId);
//				parentOrganIdSet.add(organId);
//				dataList.addAll(organService.findOrgans(parentOrganIdSet));
//			}
			if (dataList != null) {
				TreeSet<Organization> dataSet = new TreeSet<Organization>();
				dataSet.addAll(dataList);
				// 移除根目录
//				EasyuiTreeGridOrgan rootModule = new EasyuiTreeGridOrgan();
//				rootModule.setId(OrganEntity.ROOT.getOrganId());
//				rootModule.setText(OrganEntity.ROOT.getOrganName());
//				rootModule.setOrganSort(OrganEntity.ROOT.getOrganSort());
//				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
//				nodeList.add(rootModule);
				
				Map<String, EasyuiTreeGridOrgan> nodeMap = new HashMap<String, EasyuiTreeGridOrgan>();
//				nodeMap.put(rootModule.getId(), rootModule);// 移除根目录
				
				for (Iterator<Organization> it = dataSet.iterator(); it.hasNext();) {
				    Organization m = it.next();
					
//					if (parentOrganIdSet.size() > 0) {// 管理员无组织机构 使用dataList = organService.getOrgans();
//						String dataOrganId = m.getOrganId();
//						if (parentOrganIdSet.contains(dataOrganId)) {
//						} else if (dataOrganId.startsWith(organId)) {
//						} else {
//							continue;
//						}
//					}
					
					EasyuiTreeGridOrgan node = new EasyuiTreeGridOrgan();
					node.setId(m.getOrganId());
					node.setText(m.getOrganName());
					node.setOrganDesc(m.getOrganDesc());
					node.setOrganSort(m.getOrganSort());
					node.setOrganLevel(m.getOrganLevel());
					nodeMap.put(node.getId(), node);
					
					String parendId = m.getParentId();
					EasyuiTreeGridOrgan cacheNode = nodeMap.get(parendId);
					if (cacheNode != null) {
						List<EasyuiTreeNode> children = cacheNode.getChildren();
						if (children == null) children = new ArrayList<EasyuiTreeNode>();
						children.add(node);
						cacheNode.setChildren(children);
					} else {
						nodeList.add(node);// 移除根目录后，添加节点
					}
				}
				nodeMap.clear();
				nodeMap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("所有机构树形列表结构失败！", e);
			nodeList.clear();
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
    @PostMapping(value = "/tree.do")
    @ResponseBody
	public List<EasyuiTreeNode> tree(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
			List<Organization> dataList = null;
			// 系统管理员没有组织机构
//			SysUser sysUser = getSysUser(request);
//			String organId = sysUser.getDept();
//			Set<String> parentOrganIdSet = new HashSet<String>();
//			if (StringUtils.isEmpty(organId)) {// 管理员无组织机构
				dataList = organService.getOrgans();
//			} else {
//				String tempOrganId = organId;
//				while (true) {
//					String parentId = Organization.getParentOrganId(tempOrganId);
//					if (Organization.ROOT.getOrganId().equals(parentId)) {
//						break;
//					} else {
//						parentOrganIdSet.add(parentId);
//						tempOrganId = parentId;
//					}
//				}
//				// 制定ID查询
//				dataList = organService.findChildOrgans(organId);
//				parentOrganIdSet.add(organId);
//				dataList.addAll(organService.findOrgans(parentOrganIdSet));
//			}
			
			if (dataList != null) {
				TreeSet<Organization> menuSet = new TreeSet<Organization>();
				menuSet.addAll(dataList);
				// 移除根目录
//				EasyuiTreeNode rootModule = new EasyuiTreeNode();
//				rootModule.setId(OrganEntity.ROOT.getOrganId());
//				rootModule.setText(OrganEntity.ROOT.getOrganName());
//				rootModule.setChildren(new ArrayList<EasyuiTreeNode>());
//				nodeList.add(rootModule);
				
				
				Map<String, EasyuiTreeNode> nodeMap = new HashMap<String, EasyuiTreeNode>();
//				nodeMap.put(rootModule.getId(), rootModule);// 移除根目录
				
				for (Iterator<Organization> it = menuSet.iterator(); it.hasNext();) {
				    Organization m = it.next();
					
//					if (parentOrganIdSet.size() > 0) {// 管理员无组织机构 使用dataList = organService.getOrgans();
//						String dataOrganId = m.getOrganId();
//						if (parentOrganIdSet.contains(dataOrganId)) {
//						} else if (dataOrganId.startsWith(organId)) {
//						} else {
//							continue;
//						}
//					}
					
					EasyuiTreeNode node = new EasyuiTreeNode();
					node.setId(m.getOrganId());
					node.setText(m.getOrganName());
					node.setAttributes(new HashMap<String, Object>());
					nodeMap.put(node.getId(), node);
					String parendId = m.getParentId();
					EasyuiTreeNode cacheNode = nodeMap.get(parendId);
					if (cacheNode != null) {
						List<EasyuiTreeNode> children = cacheNode.getChildren();
						if (children == null) children = new ArrayList<EasyuiTreeNode>();
						children.add(node);
						cacheNode.setChildren(children);
					} else {
						nodeList.add(node);// 移除根目录后，添加节点
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
		return nodeList;
	}

}

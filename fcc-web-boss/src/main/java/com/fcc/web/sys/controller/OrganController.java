package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.EasyuiTreeGridOrgan;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.OrganizationService;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.sys.util.OrganUtil;

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
	
	@RequestMapping("/view")
	public String view(HttpServletRequest request) {
		request.setAttribute("organId", getSysUser(request).getDept());
		return "/WEB-INF/manage/sys/organ_list";
	}
	
	/** 显示组织机构新增页面 */
	@RequestMapping("/toAdd")
	public String toAdd(HttpServletRequest request) {
		try {
			String parentId = request.getParameter("parentId");
			Organization data = organService.getOrganById(parentId);
			request.setAttribute("parent", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/organ_add";
	}
	
	/** 显示组织机构新增页面 */
	@RequestMapping("/toEdit")
	public String toEdit(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			Organization data = organService.getOrganById(id);
			request.setAttribute("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "/WEB-INF/manage/sys/organ_edit";
	}
	
	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request,
	        @RequestParam(name = "organSort", defaultValue = "1") int moduleSort) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		try {
			String parentId = request.getParameter("parentId");
			String organName = request.getParameter("organName");
			String organDesc = request.getParameter("organDesc");
			if (organName == null || "".equals(organName)) {
				throw new RefusedException("请输入组织机构名称！");
			}
			if (parentId == null || "".equals(parentId)) {
				parentId = Organization.ROOT.getOrganId();
			}
			Organization parent = organService.getOrganById(parentId);
			if (parent == null) throw new RefusedException("请选择父组织机构");
			
			String organId = getSysUser(request).getDept();
			if (StringUtils.isNotEmpty(organId)) {// 非管理员
				int indexOf = parentId.indexOf(organId);
				if (indexOf != 0) {
					throw new RefusedException("不能添加上级机构！");
				}
			}
			
			Organization data = new Organization();
			if (Organization.ROOT.getOrganId().equals(parentId)) {
				data.setOrganId(RandomStringUtils.random(8, true, false));
			} else {
				data.setOrganId(parentId + "-" + RandomStringUtils.random(4, true, false));
			}
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(moduleSort);
			data.setOrganLevel(parent.getOrganLevel() + 1);
			organService.create(data);
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
	public ModelAndView edit(HttpServletRequest request,
	        @RequestParam(name = "organSort", defaultValue = "1") int organSort) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String organId = request.getParameter("id");
		try {
			String organName = request.getParameter("organName");
			String organDesc = request.getParameter("organDesc");
			if (organId == null || "".equals(organId)) {
				throw new RefusedException("请选择要修改的记录！");
			} else if (organName == null || "".equals(organName)) {
				throw new RefusedException("请输入组织机构名称！");
			} else if (Organization.ROOT.getOrganId().equals(organId)) {
				throw new RefusedException("不能修改根节点！");
			} else if (OrganUtil.checkParent(getSysUser(request), organId)) {// 判断是否修改上级组织机构
				throw new RefusedException("不能修改上级机构！");
			}
			Organization data = organService.getOrganById(organId);
			if (data == null) {
				throw new RefusedException("修改的记录不存在！");
			}
			data.setOrganDesc(organDesc);
			data.setOrganName(organName);
			data.setOrganSort(organSort);
			organService.update(data);
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
	
	@RequestMapping("/delete")
	public ModelAndView delete(HttpServletRequest request) {
		Message message = new Message();
		ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
		mav.getModelMap().addAttribute(message);
		String id = request.getParameter("id");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要删除的组织机构！");
			} else if (Organization.ROOT.getOrganId().equals(id)) {
				throw new RefusedException("无法删除根结点！");
			} if (OrganUtil.checkParent(getSysUser(request), id)) {// 判断是否删除上级组织机构
				throw new RefusedException("不能删除上级机构！");
			}
			// 判断是否有归属人员
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dept", id);
			ListPage listPage = sysUserService.queryPage(1, 10, param);
			if (listPage.getTotalSize() > 0) throw new RefusedException("该组织机构下有人员！");
			
			organService.delete(id);
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
	
	/**
	 * 组织机构 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/treegrid")
	@ResponseBody
	public List<EasyuiTreeGridOrgan> treegrid(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeGridOrgan> nodeList = new ArrayList<EasyuiTreeGridOrgan>();
		try {
			List<Organization> dataList = null;
			
			// 系统管理员没有组织机构
			Set<String> parentOrganIdSet = new HashSet<String>();
			String organId = getSysUser(request).getDept();
			if (isAdmin(request)) {// 管理员无组织机构
				dataList = organService.getOrgans();
			} else {
				String tempOrganId = organId;
				while (true) {
					String parentId = Organization.getParentOrganId(tempOrganId);
					if (Organization.ROOT.getOrganId().equals(parentId)) {
						break;
					} else {
						parentOrganIdSet.add(parentId);
						tempOrganId = parentId;
					}
				}
				// 制定ID查询
				dataList = organService.findChildOrgans(organId);
				parentOrganIdSet.add(organId);
				dataList.addAll(organService.findOrgans(parentOrganIdSet));
			}
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
			logger.error(e);
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
	@RequestMapping("/tree")
	@ResponseBody
	public List<EasyuiTreeNode> tree(HttpServletRequest request, HttpServletResponse response) {
		List<EasyuiTreeNode> nodeList = new ArrayList<EasyuiTreeNode>();
		try {
			List<Organization> dataList = null;
			// 系统管理员没有组织机构
			SysUser sysUser = getSysUser(request);
			String organId = sysUser.getDept();
			Set<String> parentOrganIdSet = new HashSet<String>();
			if (StringUtils.isEmpty(organId)) {// 管理员无组织机构
				dataList = organService.getOrgans();
			} else {
				String tempOrganId = organId;
				while (true) {
					String parentId = Organization.getParentOrganId(tempOrganId);
					if (Organization.ROOT.getOrganId().equals(parentId)) {
						break;
					} else {
						parentOrganIdSet.add(parentId);
						tempOrganId = parentId;
					}
				}
				// 制定ID查询
				dataList = organService.findChildOrgans(organId);
				parentOrganIdSet.add(organId);
				dataList.addAll(organService.findOrgans(parentOrganIdSet));
			}
			
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

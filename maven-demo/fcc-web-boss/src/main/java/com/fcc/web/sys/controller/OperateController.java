package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.service.OperateService;

/**
 * <p>Description: 管理系统 模块操作</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/operate")
public class OperateController {

	private static Logger logger = Logger.getLogger(OperateController.class);
	@Resource
	private OperateService operateService;
	
	/** 显示模块操作列表 */
	@RequestMapping("/view")
	public String view() {
		return "/WEB-INF/manage/sys/operate_list";
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Message add(Operate operate, HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		try {
			operateService.create(operate);
			message.setSuccess(true);
			message.setMsg(operate.getOperateValue() + "");
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
	public Message edit(Operate operate, HttpServletRequest request) {
		Message message = new Message();
		try {
			operateService.update(operate);
			message.setSuccess(true);
			message.setMsg("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message.setMsg("修改失败！");
			message.setObj(e.getMessage());
		}
		return message;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message delete(HttpServletRequest request) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException("请选择要删除的记录！");
			}
			String[] ids = id.split(",");
			operateService.delete(ids);
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
	 * 模块操作 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
			String operateName = request.getParameter("searchName");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("operateName", operateName);
			ListPage listPage = operateService.queryPage(dg.getPage(), dg.getRows(), param);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			json.setTotal(0L);
			json.setRows(new ArrayList<Operate>());
			json.setMsg(e.getMessage());
		}
		return json;
	}
}

package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.service.OperateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 管理系统 模块操作</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Api(value = "模块操作")
@Controller
@RequestMapping("/manage/sys/operate")
public class OperateController extends AppWebController {

	private static Logger logger = Logger.getLogger(OperateController.class);
	@Resource
	private OperateService operateService;
	
	/** 显示模块操作列表 */
	@ApiOperation(value = "显示操作列表页面")
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	@Permissions("view")
	public String view() {
		return "manage/sys/operate_list";
	}
	
	@ApiOperation(value = "新增操作")
	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@Permissions("add")
	public ModelAndView add(HttpServletRequest request, Operate operate) {
		Message message = new Message();
		try {
		    if (StringUtils.isEmpty(operate.getOperateId())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateId);
		    if (StringUtils.isEmpty(operate.getOperateName())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateName);
			operateService.add(operate);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增操作失败！", e);
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "修改操作")
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request, Operate operate) {
		Message message = new Message();
		try {
		    if (StringUtils.isEmpty(operate.getOperateId())) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
            if (StringUtils.isEmpty(operate.getOperateName())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateName);
            operateService.edit(operate);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改操作失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getCause());
		}
		return getModelAndView(message);
	}
	
	@ApiOperation(value = "删除操作")
	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "操作ID、用，分割多个ID") @RequestParam(name = "ids", defaultValue = "") String id) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyDeleteId);
			String[] ids = StringUtils.split(id, ',');
			operateService.delete(ids);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
			reloadModuleCache();
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除操作失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getCause());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 模块操作 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询操作")
	@RequestMapping(value = "/datagrid.do", method = RequestMethod.POST)
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(HttpServletRequest request, EasyuiDataGrid dg,
	        @ApiParam(required = false, value = "操作名称") @RequestParam(name = "searchName", defaultValue = "") String operateName) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    Map<String, Object> param = null;
		    if (!StringUtils.isEmpty(operateName)) {
		        param = new HashMap<String, Object>();
		        param.put("operateName", operateName);
		    }
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

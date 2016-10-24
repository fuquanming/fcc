package com.fcc.web.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
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
public class OperateController {

	private static Logger logger = Logger.getLogger(OperateController.class);
	@Resource
	private OperateService operateService;
	
	/** 显示模块操作列表 */
	@ApiOperation(value = "操作列表页面")
	@GetMapping(value = "/view.do")
	public String view() {
		return "manage/sys/operate_list";
	}
	
	@ApiOperation(value = "新增操作")
	@PostMapping(value = "/add.do")
	@ResponseBody
	public Message add(HttpServletRequest request, Operate operate) {
		Message message = new Message();
		try {
		    if (StringUtils.isEmpty(operate.getOperateId())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateId);
		    if (StringUtils.isEmpty(operate.getOperateName())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateName);
			operateService.create(operate);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增操作失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getCause());
		}
		return message;
	}
	
	@ApiOperation(value = "修改操作")
	@PostMapping(value = "/edit.do")
	@ResponseBody
	public Message edit(HttpServletRequest request, Operate operate) {
		Message message = new Message();
		try {
		    if (StringUtils.isEmpty(operate.getOperateId())) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
            if (StringUtils.isEmpty(operate.getOperateName())) throw new RefusedException(Constants.StatusCode.Operate.emptyOperateName);
            operateService.update(operate);
			message.setSuccess(true);
			message.setMsg(Constants.StatusCode.Sys.success);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改操作失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getCause());
		}
		return message;
	}
	
	@ApiOperation(value = "删除操作")
	@PostMapping(value = "/delete.do")
	@ResponseBody
	public Message delete(HttpServletRequest request,
	        @ApiParam(required = true, value = "操作ID、用，分割多个ID") @RequestParam(name = "ids") String id) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(id)) throw new RefusedException(Constants.StatusCode.Sys.emptyUpdateId);
			String[] ids = id.split(",");
			operateService.delete(ids);
			message.setMsg(Constants.StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除操作失败！", e.getCause());
			message.setMsg(Constants.StatusCode.Sys.fail);
			message.setObj(e.getCause());
		}
		return message;
	}
	
	/**
	 * 模块操作 列表 返回json 给 easyUI 
	 * @return
	 */
	@ApiOperation(value = "查询操作")
	@PostMapping(value = "/datagrid.do") 
	@ResponseBody
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

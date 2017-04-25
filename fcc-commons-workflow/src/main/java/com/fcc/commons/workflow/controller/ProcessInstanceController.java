package com.fcc.commons.workflow.controller;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.service.ProcessInstanceService;

import io.swagger.annotations.ApiParam;

/**
 * <p>Description: 工作流-流程实例管理</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
@RequestMapping("/manage/sys/workflow/processInstance")
public class ProcessInstanceController extends WorkflowController {

	private Logger logger = Logger.getLogger(ProcessInstanceController.class);
	
	@Resource
	private ProcessInstanceService processInstanceService;
	
	/** 显示流程模型列表 */
	@RequestMapping("/view.do")
	@Permissions("view")
	public String view(HttpServletRequest request) {
		return "manage/sys/workflow/processInstance_list";
	}
	
	/** 显示流程实例详情 */
    @RequestMapping("/toView.do")
    @Permissions("view")
    public String toView(HttpServletRequest request,
            @ApiParam(required = false, value = "流程实例ID") @RequestParam(name = "id", defaultValue = "") String processInstanceId) {
        try {
            setProcessHistory(request, processInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "manage/sys/workflow/processInstance_view";
    }
	
	/** 挂起、激活流程定义 */
	@RequestMapping("/edit.do")
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request,
	        @ApiParam(required = true, value = "状态名称") @RequestParam(name = "status") String status,
	        @ApiParam(required = true, value = "流程实例ID") @RequestParam(name = "processInstanceId") String processInstanceId
            ) {
		Message message = new Message();
		try {
			if (StringUtils.isEmpty(status)) {
				throw new RefusedException("请输入状态名称！");
			}
			if (StringUtils.isEmpty(processInstanceId)) {
				throw new RefusedException("请输入流程实例ID！");
			}
			if ("activate".equals(status)) {
				processInstanceService.activateProcessInstanceById(processInstanceId);
				message.setMsg(StatusCode.Sys.success);// 激活成功！
			} else if ("suspend".equals(status)) {
				processInstanceService.suspendProcessInstanceById(processInstanceId);
				message.setMsg(StatusCode.Sys.success);// 挂起成功！
			}
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("操作失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/** 删除流程实例 */
	@RequestMapping("/delete.do")
	@Permissions("delete")
	public ModelAndView delete(HttpServletRequest request) {
		Message message = new Message();
		String id = request.getParameter("ids");
		try {
			if (id == null || "".equals(id)) {
				throw new RefusedException(StatusCode.Sys.emptyDeleteId);
			}
			String[] ids = id.split(",");
			for (String processInstanceId : ids) {
				processInstanceService.deleteProcessInstance(processInstanceId, "删除原因");
			}
			message.setMsg(StatusCode.Sys.success);
			message.setSuccess(true);
		} catch (RefusedException e) {
			message.setMsg(e.getMessage());
			message.setObj(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败！", e);
			message.setMsg(StatusCode.Sys.fail);
			message.setObj(e.getMessage());
		}
		return getModelAndView(message);
	}
	
	/**
	 * 流程模型 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagrid(EasyuiDataGrid dg, HttpServletRequest request,
	        @ApiParam(required = false, value = "流程业务ID") @RequestParam(name = "businessKey", defaultValue = "") String businessKey,
	        @ApiParam(required = false, value = "流程定义ID") @RequestParam(name = "definitionKey", defaultValue = "") String definitionKey
            ) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    WorkflowInstanceQuery query = null;
			if (StringUtils.isNotEmpty(businessKey)) {
			    query = workflowService.createInstanceQuery();
				query.processInstanceBusinessKey(businessKey);
			}
			if (StringUtils.isNotEmpty(definitionKey)) {
			    if (query == null) query = workflowService.createInstanceQuery();
				query.processDefinitionKey(definitionKey);
			}
			ListPage listPage = processInstanceService.queryPage(dg.getPage(), dg.getRows(), query);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询流程实例错误！", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
}

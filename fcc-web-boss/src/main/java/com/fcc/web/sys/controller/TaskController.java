package com.fcc.web.sys.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.commons.web.annotation.Permissions;
import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.EasyuiDataGrid;
import com.fcc.commons.web.view.EasyuiDataGridJson;
import com.fcc.commons.web.view.Message;
import com.fcc.commons.workflow.common.WorkflowDefinitionKey;
import com.fcc.commons.workflow.controller.WorkflowController;
import com.fcc.commons.workflow.filter.WorkflowTaskBusinessDataFilter;
import com.fcc.commons.workflow.model.WorkflowBean;
import com.fcc.commons.workflow.query.WorkflowInstanceQuery;
import com.fcc.commons.workflow.query.WorkflowTaskQuery;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.service.CacheService;

import io.swagger.annotations.ApiParam;

/**
 * <p>Description:任务管理</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */

@Controller
@RequestMapping(value={"/manage/sys/task"} )
@SuppressWarnings("unused")
public class TaskController extends WorkflowController {
	
	private static Logger logger = Logger.getLogger(TaskController.class);
	@Resource
	private BaseService baseService;
	//默认多列排序,example: username desc,createTime asc
	@Resource
	private CacheService cacheService;
	
    public TaskController() {
	}

	/** 显示流程任务列表 */
	@RequestMapping(value = {"/view.do"})
	@Permissions("view")
	public String view(HttpServletRequest request) {
	    request.setAttribute("definitionKeyMap", WorkflowDefinitionKey.definitionKeyMap);
		return "manage/sys/task/task_list";
	}
	
	/** 跳转 */
    @RequestMapping(value = {"/toView.do"})
    @Permissions("view")
    public String toView(HttpServletRequest request,
            @ApiParam(required = false, value = "流程实例ID") @RequestParam(name = "id", defaultValue = "") String processInstanceId) {
        try {
            getProcessHistory(request, processInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "manage/sys/task/task_view";
    }
	
	/** 跳转 */
	@RequestMapping(value = {"/toEdit.do"})
	@Permissions("edit")
	public String toEdit(HttpServletRequest request,
	        @ApiParam(required = false, value = "任务ID") @RequestParam(name = "id", defaultValue = "") String taskId) {
		try {
			ProcessTaskInfo taskInfo = workflowService.getProcessTask(taskId);
			// 获取任务信息
            if (taskInfo != null) {
                request.setAttribute("taskInfo", taskInfo);// 任务信息
                request.setAttribute("flowList", getTaskOutSequenceFlow(taskInfo));// 构建按钮
                // 审批意见
                request.setAttribute("commentList", this.getTaskComments(taskInfo.getProcessInstanceId()));
                // 流程业务ID
                String businessKey = workflowService.getProcessInstace(taskInfo.getProcessInstanceId()).getBusinessKey();
                // 获取流程数据
                Map<String, Object> map = workflowService.getTaskBusinessData(taskInfo, businessKey);
                if (map != null) {
                    for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                        String key = it.next();
                        request.setAttribute(key, map.get(key));// 绑定数据，页面等
                    }
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "manage/sys/task/task_edit";
	}
	
	/** 修改-签收，办理任务 */
	@RequestMapping(value = {"/edit.do"})
	@Permissions("edit")
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response,
	        @ApiParam(required = false, value = "任务ID") @RequestParam(name = "taskId", defaultValue = "") String taskId,
	        @ApiParam(required = false, value = "taskClaim：签收任务，taskExecute：执行任务") @RequestParam(name = "type", defaultValue = "") String type,
	        @ApiParam(required = false, value = "操作KEY") @RequestParam(name = "conditionKey", defaultValue = "") String conditionKey,
	        @ApiParam(required = false, value = "操作值") @RequestParam(name = "conditionValue", defaultValue = "") String conditionValue) {
		Message message = new Message();
		// taskClaim，任务签收，taskExecute 执行任务
		if ("taskClaim".equals(type)) {
			try {
				this.taskClaim(taskId, cacheService.getSysUser(request).getUserId());
				message.setSuccess(true);
				message.setMsg(StatusCode.Sys.success);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("签收失败!", e);
				message.setMsg(StatusCode.Sys.fail);
				message.setObj(e.getMessage());
			}
		} else if ("taskExecute".equals(type)) {
			try {
                String processInstanceId = request.getParameter("processInstanceId");
                String readonly = request.getParameter("readonly");
                String msg = request.getParameter("message");
                String leaveId = request.getParameter("leaveId");
                String processDefinitionKey = request.getParameter("processDefinitionKey");// 流程定义KEY
                Map<String, Object> variables = new HashMap<String, Object>(1);
                variables.put(conditionKey, conditionValue);
//                this.taskComplete(taskId, processInstanceId, variables, msg);
                workflowService.taskComplete(cacheService.getSysUser(request).getUserId(),
                        taskId, processInstanceId, variables, msg, request);

//				AmountApply amountApply = null;
//				String processInstanceId = request.getParameter("processInstanceId");
//				String readonly = request.getParameter("readonly");
//				String msg = request.getParameter("message");
//				Long amountApplyId = Long.valueOf(request.getParameter("amountApplyId"));
//				Map<String, Object> variables = new HashMap<String, Object>();
//				variables.put(request.getParameter("conditionKey"), request.getParameter("conditionValue"));
//				if ("false".equalsIgnoreCase(readonly)) {
//					// 修改申请
//					amountApply = (AmountApply) baseService.get(AmountApply.class, amountApplyId);
//					amountApply.setUserName(request.getParameter("userName"));
//					amountApply.setApplyRemark(request.getParameter("applyRemark"));
//					amountApply.setPrimaryAmount(Double.valueOf(request.getParameter("primaryAmount")));
//				}
//				variables.put(AmountApplyEnum.amount.toString(), Double.valueOf(request.getParameter("primaryAmount")));
//				variables.put(AmountApplyEnum.standard.toString(), Double.valueOf("100000"));
//				// 设置风控人员提交金额
//				String memberAmountStr = request.getParameter("memberAmount");
//				if (StringUtils.isNotEmpty(memberAmountStr)) {
//					String taskDefinitionKey = request.getParameter("taskDefinitionKey");
//					variables.put(taskDefinitionKey + "_amount", memberAmountStr);
//				}
//				amountApplyWorkflowService.taskComplete(taskId, processInstanceId, variables, msg, amountApply);
				message.setSuccess(true);
				message.setMsg(StatusCode.Sys.success);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("办理失败!", e);
				message.setMsg(StatusCode.Sys.fail);
				message.setObj(e.getMessage());
			}
		}
		return getModelAndView(message);
	}
	
	/**
	 * 列表 返回json 给 easyUI 
	 * @return
	 */
	@RequestMapping("/datagrid.do")
	@ResponseBody
	@Permissions("view")
	public EasyuiDataGridJson datagridProcessTask(EasyuiDataGrid dg, 
	        @ApiParam(required = false, value = "流程定义ID") @RequestParam(name = "definitionKey", defaultValue = "") String definitionKey,
	        @ApiParam(required = false, value = "任务类型") @RequestParam(name = "taskType", defaultValue = "") String taskType,
            HttpServletRequest request) {
		EasyuiDataGridJson json = new EasyuiDataGridJson();
		try {
		    String userId = SysUserAuthentication.getSysUser().getUserId();
		    if (definitionKey.equals("")) definitionKey = null;
		    String taskAssignee = null;// 待办理任务
		    String taskCandidateUser = null;// 待签收任务
		    if ("taskClaim".equals(taskType)) {// 待签收任务
		        taskCandidateUser = userId;
		    } else if ("taskExecute".equals(taskType)) {// 待办理任务
		        taskAssignee = userId;
		    }
		    WorkflowTaskQuery query = workflowService.createTaskQuery();
            if (taskAssignee == null && taskCandidateUser == null) query.taskCandidateOrAssigned(userId);
            query.taskAssignee(taskAssignee);
            query.taskCandidateUser(taskCandidateUser);
            query.processDefinitionKey(definitionKey);
			ListPage listPage = workflowService.queryPageProcessTask(dg.getPage(), dg.getRows(), query);
			json.setTotal(Long.valueOf(listPage.getTotalSize()));
			json.setRows(listPage.getDataList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询待办任务错误", e);
			json.setTotal(0L);
			json.setRows(Collections.EMPTY_LIST);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
}


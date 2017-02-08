package com.fcc.commons.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.workflow.view.ProcessTaskSequenceFlowInfo;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Controller
public class WorkflowController extends BaseController {

    @Resource
	private WorkflowService workflowService;
	
	/**
	 * 读取带跟踪的图片
	 * @param processInstanceId
	 * @param businessKey
	 * @param definitionKey
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/trace/img.do"})
	public void trace(@RequestParam(defaultValue = "", value = "processInstanceId") String processInstanceId, 
			@RequestParam(defaultValue = "", value = "businessKey") String businessKey, 
			@RequestParam(defaultValue = "", value = "definitionKey") String definitionKey, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			InputStream is = workflowService.trace(processInstanceId, businessKey, definitionKey);
			FileCopyUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 签收任务
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void taskClaim(String taskId, String userId) throws Exception {
		workflowService.taskClaim(taskId, userId);
	}
	
	/**
	 * 完成任务
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void taskComplete(String taskId, String processInstanceId, Map<String, Object> variables, String message) throws Exception {
		workflowService.taskComplete(taskId, processInstanceId, variables, message);
	}
	
	/**
	 * 获取任务出去的线
	 * @param processTaskInfo
	 * @return
	 */
	public List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo) {
		return workflowService.getTaskOutSequenceFlow(processTaskInfo);
	}
	
	/**
	 * 获取审批信息
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessTaskInfo> getComments(String processInstanceId) {
		return workflowService.getComments(processInstanceId);
	}
	
}

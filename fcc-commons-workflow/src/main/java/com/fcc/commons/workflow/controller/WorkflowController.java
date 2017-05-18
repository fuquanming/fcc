package com.fcc.commons.workflow.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.controller.BaseController;
import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.commons.workflow.query.WorkflowDefinitionQuery;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessDefinitionInfo;
import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessTaskCommentInfo;
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
	public WorkflowService workflowService;
	
//	/**
//	 * 读取带跟踪的图片
//	 * @param processInstanceId
//	 * @param businessKey
//	 * @param definitionKey
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping(value = {"/trace/img.do"})
//	public void trace(@RequestParam(defaultValue = "", value = "processInstanceId") String processInstanceId, 
//			@RequestParam(defaultValue = "", value = "businessKey") String businessKey, 
//			@RequestParam(defaultValue = "", value = "definitionKey") String definitionKey, 
//			HttpServletRequest request, HttpServletResponse response) {
//		try {
//			InputStream is = workflowService.trace(processInstanceId, businessKey, definitionKey);
//			FileCopyUtils.copy(is, response.getOutputStream());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
	public void taskComplete(String userId, String taskId, String processInstanceId, Map<String, Object> variables, String message) throws Exception {
		workflowService.taskComplete(userId, taskId, processInstanceId, variables, message);
	}
	
	/**
	 * 获取任务出去的线
	 * @param processTaskInfo
	 * @return
	 */
	public List<ProcessTaskSequenceFlowInfo> getTaskOutSequenceFlow(ProcessTaskInfo processTaskInfo) {
	    List<ProcessTaskSequenceFlowInfo> flowList = workflowService.getTaskOutSequenceFlow(processTaskInfo);
	    for (ProcessTaskSequenceFlowInfo info : flowList) {
            String[] conditions = info.analyzeConditionText(info.getConditionText());
            info.setConditionKey(conditions[0]);
            info.setConditionValue(conditions[1]);
        }
		return flowList;
	}
	
	/**
	 * 获取审批信息
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessTaskCommentInfo> getComments(String processInstanceId) {
		return workflowService.getComments(processInstanceId);
	}
	
	/**
     * 获取审批信息
     * @param processInstanceId
     * @return
     */
    public List<ProcessTaskInfo> getTasks(String processInstanceId) {
        return workflowService.getTasks(processInstanceId);
    }
    
    /**
     * 显示流程历史详情
     * @param request
     * @param processInstanceId
     */
    public void setProcessHistory(HttpServletRequest request, String processInstanceId) {
        ProcessHistoryInfo instanceInfo = workflowService.getHistoricProcessInstance(processInstanceId);
        if (instanceInfo == null) {
//            Leave data = (Leave) baseService.get(Leave.class, businessKey);
//            request.setAttribute("data", data);
            return;
        }
        // 流程业务ID
        String businessKey = instanceInfo.getBusinessKey();
        ProcessTaskInfo taskInfo = new ProcessTaskInfo();
        taskInfo.setProcessDefinitionKey(instanceInfo.getProcessDefinitionKey());
        request.setAttribute("taskInfo", taskInfo);// 任务信息
        // 获取流程数据
        Map<String, Object> map = workflowService.getTaskBusinessData(taskInfo, businessKey);
        if (map != null) {
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                request.setAttribute(key, map.get(key));// 绑定数据，页面等
            }
        }
    }
    
    /**
     * 设置 流程状态值、流程图片
     * @param request
     * @param processDefinitionKey   流程定义KEY
     */
    public void setWorkflowInfo(HttpServletRequest request, String processDefinitionKey) {
        request.setAttribute(WorkflowStatus.unstart.name(), WorkflowStatus.unstart);
        request.setAttribute(WorkflowStatus.start.name(), WorkflowStatus.start);
        request.setAttribute(WorkflowStatus.success.name(), WorkflowStatus.success);
        request.setAttribute(WorkflowStatus.fail.name(), WorkflowStatus.fail);
        request.setAttribute(WorkflowStatus.cannel.name(), WorkflowStatus.cannel);
        
        WorkflowDefinitionQuery workflowDefinitionQuery = workflowService.createDefinitionQuery();
        workflowDefinitionQuery.processDefinitionKey(processDefinitionKey);
        ListPage listPage = workflowService.queryPageProcessDefinition(1, 1, workflowDefinitionQuery);
        if (listPage.getDataSize() > 0) {
            request.setAttribute("processDefinitionId", ((ProcessDefinitionInfo)listPage.getDataList().get(0)).getProcessDefinitionId());
        }
    }
}

package com.fcc.web.workflow.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.workflow.common.WorkflowVariableEnum;
import com.fcc.commons.workflow.listener.TaskListener;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.model.SysUser;

import com.fcc.web.workflow.model.Leave;
/**
 * <p>Description:Leave</p>
 */

@Service
public class LeaveTaskListener implements TaskListener {
    @Resource
    private BaseService baseService;
    @Resource
    private WorkflowService workflowService;
    
    @PostConstruct
    public void init() {
        workflowService.getTaskListeners().add(this);
    }
    
    @Override
    public Map<String, Object> getBusinessData(ProcessTaskInfo taskInfo, String businessKey) {
        Map<String, Object> map = null;
        if (Leave.processDefinitionKey.equals(taskInfo.getProcessDefinitionKey())) {
            map = new HashMap<String, Object>(3);
            Leave data = (Leave) baseService.get(Leave.class, businessKey);
            map.put("requestUserName", ((SysUser)baseService.get(SysUser.class, data.getCreateUser())).getUserName());
            map.put("data", data);
            map.put(WorkflowVariableEnum.editTaskPage.name(), "/WEB-INF/manage/workflow/leave/leave_task_edit.jsp");
            // 判断是否可以修改绑定数据用于重新提交申请
            boolean readonly = true; 
            if ("leave_audit".equals(taskInfo.getTaskDefinitionKey())) {// 申请的流程ID，可以修改数据
                readonly = false;
            }
            map.put("readonly", readonly);
        }
        return map;
    }
    
    @Override
    public void beforeComplete(HttpServletRequest request, Map<String, Object> variables) {

    }
    
    @Override
    public void afterComplete(HttpServletRequest request, Map<String, Object> variables) {
        String processDefinitionKey = request.getParameter("processDefinitionKey");// 流程定义KEY
        String readonly = request.getParameter("readonly");// 是否只读
        if (Leave.processDefinitionKey.equals(processDefinitionKey)) {
            // 保存数据
            String leaveId = request.getParameter("dataId");
            Leave leave = (Leave) baseService.get(Leave.class, leaveId);
            if ("false".equalsIgnoreCase(readonly)) {
                try {
                    String startTimeString = request.getParameter("startTimeString");
                    leave.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeString));
                    String endTimeString = request.getParameter("endTimeString");
                    leave.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeString));
                    leave.setContent(DataFormater.noNullValue((request.getParameter("content"))));
                    leave.setUpdateTime(new Date());
                    leave.setUpdateUser(SysUserAuthentication.getSysUser().getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            ProcessTaskInfo task = workflowService.getCurrentTask(request.getParameter("processInstanceId"));
            String taskName = "";
            if (task != null) taskName = task.getName();
            leave.setProcessNodeName(taskName);
            baseService.edit(leave);
        }
    }
}
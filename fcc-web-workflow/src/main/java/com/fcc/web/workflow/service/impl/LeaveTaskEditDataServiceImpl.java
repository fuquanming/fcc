package com.fcc.web.workflow.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.workflow.filter.WorkflowTaskEditDataFilter;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.commons.data.DataFormater;

import com.fcc.web.sys.cache.SysUserAuthentication;

import com.fcc.web.workflow.model.Leave;
/**
 * <p>Description:Leave</p>
 */

@Service
public class LeaveTaskEditDataServiceImpl implements WorkflowTaskEditDataFilter {
    @Resource
    private BaseService baseService;
    @Resource
    private WorkflowService workflowService;
    
    @PostConstruct
    public void init() {
        workflowService.getTaskEditDataSet().add(this);
    }
    
    @Override
    public void edit(HttpServletRequest request, Map<String, Object> variables) {
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
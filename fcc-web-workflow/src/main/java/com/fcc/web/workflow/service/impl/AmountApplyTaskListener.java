package com.fcc.web.workflow.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.DataFormater;
import com.fcc.commons.workflow.common.WorkflowVariableEnum;
import com.fcc.commons.workflow.listener.TaskListener;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.workflow.common.AmountApplyEnum;
import com.fcc.web.workflow.model.AmountApply;
/**
 * <p>Description:AmountApply</p>
 */

@Service
public class AmountApplyTaskListener implements TaskListener {
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
        if (AmountApply.processDefinitionKey.equals(taskInfo.getProcessDefinitionKey())) {
            map = new HashMap<String, Object>(3);
            AmountApply data = (AmountApply) baseService.get(AmountApply.class, businessKey);
            map.put("requestUserName", ((SysUser)baseService.get(SysUser.class, data.getCreateUser())).getUserName());
            map.put("data", data);
            map.put(WorkflowVariableEnum.editTaskPage.name(), "/WEB-INF/manage/workflow/amountApply/amountApply_task_edit.jsp");
            // 判断是否可以修改绑定数据用于重新提交申请
            boolean readonly = true; 
            if ("modifyApply".equals(taskInfo.getTaskDefinitionKey())) {// 申请的流程ID，可以修改数据
                readonly = false;
            }
            map.put("readonly", readonly);
            
            // 判断是否是风控人员
            String taskDefinitionKey = taskInfo.getTaskDefinitionKey();// task定义ID
            if (StringUtils.isNotEmpty(taskDefinitionKey)) {
                String index = taskDefinitionKey.substring(taskDefinitionKey.length() - 1);
                Object randomUserId = taskInfo.getProcessVariables().get(AmountApplyEnum.randomMember + index);
                if (randomUserId != null) {
                    // 风控人员
                    map.put("memberFlg", true);
                }
            }
        }
        return map;
    }
    
    @Override
    public void beforeComplete(HttpServletRequest request, Map<String, Object> variables) {
        String primaryAmount = request.getParameter("primaryAmount");
        if (StringUtils.isNotEmpty(primaryAmount)) {
            variables.put(AmountApplyEnum.amount.toString(), Double.valueOf(primaryAmount));// 申请金额
            variables.put(AmountApplyEnum.standard.toString(), Double.valueOf(AmountApplyEnum.standardNum.getValue()));// 限制金额
        }
        // 设置风控人员提交金额
        String memberAmountStr = request.getParameter("memberAmount");
        if (StringUtils.isNotEmpty(memberAmountStr)) {
            String taskDefinitionKey = request.getParameter("taskDefinitionKey");
            variables.put(taskDefinitionKey + "_amount", memberAmountStr);
        }
    }
    
    @Override
    public void afterComplete(HttpServletRequest request, Map<String, Object> variables) {
        String processDefinitionKey = request.getParameter("processDefinitionKey");// 流程定义KEY
        String readonly = request.getParameter("readonly");// 是否只读
        if (AmountApply.processDefinitionKey.equals(processDefinitionKey)) {
            // 保存数据
            String amountApplyId = request.getParameter("dataId");
            AmountApply amountApply = (AmountApply) baseService.get(AmountApply.class, amountApplyId);
            if ("false".equalsIgnoreCase(readonly)) {
                try {
                    amountApply.setUserId(java.lang.Long.valueOf((request.getParameter("userId"))));
                    amountApply.setUserName(DataFormater.noNullValue((request.getParameter("userName"))));
                    amountApply.setPrimaryAmount(java.lang.Double.valueOf((request.getParameter("primaryAmount"))));
                    amountApply.setApplyRemark(DataFormater.noNullValue((request.getParameter("applyRemark"))));
                    String applyTimeString = request.getParameter("applyTimeString");
                    amountApply.setApplyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applyTimeString));
                    amountApply.setUpdateTime(new Date());
                    amountApply.setUpdateUser(SysUserAuthentication.getSysUser().getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            ProcessTaskInfo task = workflowService.getCurrentTask(request.getParameter("processInstanceId"));
            String taskName = "";
            if (task != null) taskName = task.getName();
            amountApply.setProcessNodeName(taskName);
            baseService.edit(amountApply);
        }
    }
}
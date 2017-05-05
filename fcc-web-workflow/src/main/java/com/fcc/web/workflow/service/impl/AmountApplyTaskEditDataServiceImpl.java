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

import com.fcc.web.workflow.model.AmountApply;
/**
 * <p>Description:AmountApply</p>
 */

@Service
public class AmountApplyTaskEditDataServiceImpl implements WorkflowTaskEditDataFilter {
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
<#include "/custom.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

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

import ${basepackage}.model.${className};
/**
 * <p>Description:${table.tableAlias}</p>
 */

@Service
public class ${className}TaskListener implements TaskListener {
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
        if (${className}.processDefinitionKey.equals(taskInfo.getProcessDefinitionKey())) {
            map = new HashMap<String, Object>(3);
            ${className} data = (${className}) baseService.get(${className}.class, businessKey);
            map.put("requestUserName", ((SysUser)baseService.get(SysUser.class, data.getCreateUser())).getUserName());
            map.put("data", data);
            map.put(WorkflowVariableEnum.editTaskPage.name(), "/WEB-INF${actionBasePath}/${classNameLower}_task_edit.jsp");
            // 判断是否可以修改绑定数据用于重新提交申请
            boolean readonly = true; 
            if ("${classNameLower}_audit".equals(taskInfo.getTaskDefinitionKey())) {// 申请的流程ID，可以修改数据
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
        if (${className}.processDefinitionKey.equals(processDefinitionKey)) {
            // 保存数据
            <#list table.pkColumns as column>
            String ${column.columnNameLower} = request.getParameter("dataId");
            ${className} ${classNameLower} = (${className}) baseService.get(${className}.class, ${column.columnNameLower});
            </#list>
            if ("false".equalsIgnoreCase(readonly)) {
                try {
                    <#list table.columns as column>
                    <#if !column.pk>
                    <#if column.columnNameLower != "createTime" && column.columnNameLower != "createUser" && column.columnNameLower != "updateTime" && column.columnNameLower != "updateUser" && column.columnNameLower != "status" && column.columnNameLower != "processInstanceId" && column.columnNameLower != "processDefinitionId" && column.columnNameLower != "processNodeName">  
                        <#if column.isDateTimeColumn>
                    String ${column.columnNameLower}String = request.getParameter("${column.columnNameLower}String");
                    ${classNameLower}.set${column.columnName}(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(${column.columnNameLower}String));
                        </#if>
                        <#if column.isStringColumn>
                    ${classNameLower}.set${column.columnName}(DataFormater.noNullValue((request.getParameter("${column.columnNameLower}"))));
                        </#if>
                        <#if column.isNumberColumn>
                    ${classNameLower}.set${column.columnName}(${column.javaType}.valueOf((request.getParameter("${column.columnNameLower}"))));
                        </#if>
                    </#if>
                    </#if>
                    </#list>
                    ${classNameLower}.setUpdateTime(new Date());
                    ${classNameLower}.setUpdateUser(SysUserAuthentication.getSysUser().getUserId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            ProcessTaskInfo task = workflowService.getCurrentTask(request.getParameter("processInstanceId"));
            String taskName = "";
            if (task != null) taskName = task.getName();
            ${classNameLower}.setProcessNodeName(taskName);
            baseService.edit(${classNameLower});
        }
    }
}
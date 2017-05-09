<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.workflow.common.WorkflowStatus;
import com.fcc.commons.workflow.service.WorkflowService;
import com.fcc.commons.workflow.view.ProcessTaskInfo;
import com.fcc.web.sys.model.SysUser;

import ${basepackage}.model.${className};
import ${basepackage}.service.${className}WorkflowService;
/**
 * <p>Description:${table.tableAlias}</p>
 */

@Service
public class ${className}WorkflowServiceImpl implements ${className}WorkflowService {
    @Resource
    private BaseService baseService;
    @Resource
    private WorkflowService workflowService;
    
    /**
     * @see com.fcc.web.workflow.service.LeaveApplyWorkflowService#startWorkflow(java.lang.Long[])
     **/
    @Transactional(rollbackFor = Exception.class)//事务申明
    @Override
    public void startWorkflow(String dataId) {
        ${className} data = (${className}) baseService.get(${className}.class, dataId);
        // 判断状态
        String status = data.getStatus();
        if (!WorkflowStatus.unstart.name().equals(status)) { // 未启动状态才可以启动
            return;
        }
        
        SysUser sysUser = (SysUser) baseService.get(SysUser.class, data.getCreateUser());
        data.setStatus(WorkflowStatus.start.name());
        
        Map<String, Object> variables = new HashMap<String, Object>(1);
        variables.put("applyUserId", sysUser.getUserId());// 设置启动用户ID
        String processInstanceId = workflowService.startWorkflow(data, sysUser.getUserId(), sysUser.getUserName(), variables);
        ProcessTaskInfo task = workflowService.getCurrentTask(processInstanceId);
        // 开始下个流程
        variables.clear();
        variables.put("action", "apply");// 设置下个流转
        workflowService.taskComplete(sysUser.getUserId(), task.getId(), processInstanceId, variables, null);
        task = workflowService.getCurrentTask(processInstanceId);
        if (task != null) data.setProcessNodeName(task.getName());
        baseService.edit(data);
    }
}
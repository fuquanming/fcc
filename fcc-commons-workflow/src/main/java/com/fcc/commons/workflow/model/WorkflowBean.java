package com.fcc.commons.workflow.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fcc.commons.workflow.view.ProcessHistoryInfo;
import com.fcc.commons.workflow.view.ProcessInstanceInfo;
import com.fcc.commons.workflow.view.ProcessTaskInfo;

/**
 * 
 * <p>Description: MappedSuperclass：注解可以被继承</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@MappedSuperclass
@SuppressWarnings("unused")
public abstract class WorkflowBean {

	/** 流程定义的id,请假流程leave */
	private String definitionKey;
	/** 业务数据ID */
	private String businessKey;
	
	/**
     * 流程实例ID    db_column: PROCESS_INSTANCE_ID
     */
    private String processInstanceId;
    /**
     * 流程定义ID    db_column: PROCESS_DEFINITION_ID
     */
    private String processDefinitionId;
    /**
     * 流程当前节点        db_column: PROCESS_NODE_NAME
     */
    private String processNodeName;
	/**
     * 申请状态 (数据字典）       db_column: STATUS 
     */ 	
	private String status;
	
	private ProcessInstanceInfo processInstanceInfo;
    private ProcessHistoryInfo processHistoryInfo;
    private ProcessTaskInfo processTaskInfo;
	
	/** 业务数据ID */
	@Transient
	public abstract String getBusinessKey();

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	/** 流程定义的id,请假流程leave */
	@Transient
	public abstract String getDefinitionKey();

	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}
	
	@Column(name = "PROCESS_INSTANCE_ID", unique = false, nullable = true, insertable = true, updatable = true)//, length = 255
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@Column(name = "PROCESS_DEFINITION_ID", unique = false, nullable = true, insertable = true, updatable = true)//, length = 255
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Column(name = "PROCESS_NODE_NAME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 255
    public String getProcessNodeName() {
        return processNodeName;
    }

    public void setProcessNodeName(String processNodeName) {
        this.processNodeName = processNodeName;
    }

	@Column(name = "STATUS", unique = false, nullable = true, insertable = true, updatable = true)//, length = 16
	public java.lang.String getStatus() {
		return this.status;
	}
	
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	
	@Transient
    public ProcessInstanceInfo getProcessInstanceInfo() {
        return processInstanceInfo;
    }

    public void setProcessInstanceInfo(ProcessInstanceInfo processInstanceInfo) {
        this.processInstanceInfo = processInstanceInfo;
    }
    @Transient
    public ProcessHistoryInfo getProcessHistoryInfo() {
        return processHistoryInfo;
    }

    public void setProcessHistoryInfo(ProcessHistoryInfo processHistoryInfo) {
        this.processHistoryInfo = processHistoryInfo;
    }
    @Transient
    public ProcessTaskInfo getProcessTaskInfo() {
        return processTaskInfo;
    }

    public void setProcessTaskInfo(ProcessTaskInfo processTaskInfo) {
        this.processTaskInfo = processTaskInfo;
    }
	
}

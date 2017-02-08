package com.fcc.commons.workflow.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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

	/**
	 * 流程实例ID	 db_column: PROCESS_INSTANCE_ID
	 */
	private String processInstanceId;
	/** 流程定义的id,请假流程leave */
	private String definitionKey;
	/** 业务数据ID */
	private String businessKey;
	
	/**
     * 申请状态 (数据字典）       db_column: STATUS 
     */ 	
	private String status;
	
	/** 流程定义的id,请假流程leave */
	@Transient
	public abstract String getBusinessKey();

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	/** 业务数据ID */
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

	@Column(name = "STATUS", unique = false, nullable = true, insertable = true, updatable = true)//, length = 16
	public java.lang.String getStatus() {
		return this.status;
	}
	
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	
}

package com.fcc.commons.workflow.view;

import java.util.Date;

/**
 * <p>Description:流程定义列表数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ProcessDefinitionInfo {

	/** 流程定义ID */
	private String processDefinitionId;
	/** 流程部署ID */
	private String processDeploymentId;
	/** 流程名称 */
	private String processName;
	/** 流程KEY */
	private String processKey;
	/** 流程版本 */
	private int processVersion;
	/** 流程资源名称-XML */
	private String processResourceName;
	/** 流程资源图片 */
	private String processDiagramResourceName;
	/** 流程部署时间 */
	private Date deploymentTime;
	/** 流程是否挂起 */
	private boolean processSuspended;
	
	
	public ProcessDefinitionInfo(String processDefinitionId,
			String processDeploymentId, String processName, String processKey,
			Integer processVersion, String processResourceName,
			String processDiagramResourceName, Date deploymentTime,
			boolean processSuspended) {
		super();
		this.processDefinitionId = processDefinitionId;
		this.processDeploymentId = processDeploymentId;
		this.processName = processName;
		this.processKey = processKey;
		this.processVersion = processVersion;
		this.processResourceName = processResourceName;
		this.processDiagramResourceName = processDiagramResourceName;
		this.deploymentTime = deploymentTime;
		this.processSuspended = processSuspended;
	}
	public ProcessDefinitionInfo() {
		super();
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessDeploymentId() {
		return processDeploymentId;
	}
	public void setProcessDeploymentId(String processDeploymentId) {
		this.processDeploymentId = processDeploymentId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public Integer getProcessVersion() {
		return processVersion;
	}
	public void setProcessVersion(Integer processVersion) {
		this.processVersion = processVersion;
	}
	public String getProcessResourceName() {
		return processResourceName;
	}
	public void setProcessResourceName(String processResourceName) {
		this.processResourceName = processResourceName;
	}
	public String getProcessDiagramResourceName() {
		return processDiagramResourceName;
	}
	public void setProcessDiagramResourceName(String processDiagramResourceName) {
		this.processDiagramResourceName = processDiagramResourceName;
	}
	public Date getDeploymentTime() {
		return deploymentTime;
	}
	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	public boolean isProcessSuspended() {
		return processSuspended;
	}
	public void setProcessSuspended(boolean processSuspended) {
		this.processSuspended = processSuspended;
	}
	
	
}

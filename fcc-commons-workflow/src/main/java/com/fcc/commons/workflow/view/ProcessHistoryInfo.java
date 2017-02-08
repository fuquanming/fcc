package com.fcc.commons.workflow.view;

import java.util.Date;

/**
 * <p>Description:流程实例列表数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ProcessHistoryInfo {
	// ID
	private String id;
	// 流程定义ID
	private String processDefinitionId;
	// 流程业务ID
	private String businessKey;
	// 开始时间
	private Date startTime;
	// 结束时间
	private Date endTime;
	// 持续时间
	private Long durationInMillis;
	// 流程结束原因
	private String deleteReason;
	// 流程定义版本号
	private Integer processDefinitionVersion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getDurationInMillis() {
		return durationInMillis;
	}
	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}
	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}
	

}

package com.fcc.commons.workflow.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Description:流程任务列表数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ProcessTaskInfo {
	/** 任务ID */
	private String Id;
	/** 任务定义ID-流程图里的ID */
	private String taskDefinitionKey;
	/** 任务名称 */
	private String name;
	/** 流程定义ID */
	private String processDefinitionId;
	/** 流程实例ID */
	private String processInstanceId;
	/** 执行ID */
	private String executionId;
	/** 优先级 */
	private Integer priority;
	/** 任务创建日期 */
	private Date createTime;
	/** 任务逾期日 */
	private Date dueDate;
	/** 任务描述 */
	private String description;
	/** 任务所属人 */
	private String owner;
	/** 当前处理人 */
	private String assignee;
	/** 任务存储变量 */
	private Map<String, Object> processVariables;
	
	/** 流程定义版本号 */
	private Integer processDefinitionVersion;
	/** 流程定义名称 */
	private String processDefinitionName;
	/** 流程定义KEY */
	private String processDefinitionKey;
	
	// 历史任务使用字段 begin
	/** 任务开始的时间. */
	private Date startTime;
	/** 任务被删除或完成的时间。*/
	private Date endTime;
	/** {@link #getEndTime（）}与{@link #getStartTime（）}之间的差异（以毫秒为单位）。  */
    private Long durationInMillis;
    // 历史任务使用字段 end
    
    /** 超时时间 */
	private Date claimTime;
	/** 评论内容 */
	private String comment;
	/** 评论时间 */
	private Date commentTime;
	
	/** 任务附件 */
	private List<ProcessTaskAttachmentInfo> attachmentList;
	
    public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}
	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}
	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}
	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}
	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}
	public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
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
	public Date getClaimTime() {
		return claimTime;
	}
	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public List<ProcessTaskAttachmentInfo> getAttachmentList() {
        return attachmentList;
    }
    public void setAttachmentList(List<ProcessTaskAttachmentInfo> attachmentList) {
        this.attachmentList = attachmentList;
    }
}

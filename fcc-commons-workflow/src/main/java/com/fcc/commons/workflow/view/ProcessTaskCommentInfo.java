/*
 * @(#)ProcessTaskCommentInfo.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月28日
 * 修改历史 : 
 *     1. [2017年3月28日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.view;

import java.util.Date;

/**
 * 流程任务-评论
 * @version 
 * @author 傅泉明
 */
public class ProcessTaskCommentInfo {
    /** 评论ID */
    private String id;
    /** 评论人 */ 
    private String userId;
    /** 评论时间 */ 
    private Date time;
    /** 任务ID */ 
    private String taskId;
    /** 流程实例ID */ 
    private String processInstanceId;
    /** 评论内容 */ 
    private String comment;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
}

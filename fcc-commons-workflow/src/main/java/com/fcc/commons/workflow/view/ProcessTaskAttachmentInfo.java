/*
 * @(#)TaskAttachment.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年4月27日
 * 修改历史 : 
 *     1. [2017年4月27日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.view;

import java.io.InputStream;

/**
 * 任务附件
 * @version 
 * @author 傅泉明
 */
public class ProcessTaskAttachmentInfo {
    
    private String taskId;
    private String processInstanceId;
    private String attachmentType;
    private String attachmentName;
    private String attachmentDescription;
    private InputStream content;
    private String url;
    
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
    public String getAttachmentType() {
        return attachmentType;
    }
    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
    public String getAttachmentName() {
        return attachmentName;
    }
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
    public String getAttachmentDescription() {
        return attachmentDescription;
    }
    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }
    public InputStream getContent() {
        return content;
    }
    public void setContent(InputStream content) {
        this.content = content;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    
}

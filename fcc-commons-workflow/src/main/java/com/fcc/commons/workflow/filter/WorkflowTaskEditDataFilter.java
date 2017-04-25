/*
 * @(#)WorkflowTaskEditDataFilter.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月28日
 * 修改历史 : 
 *     1. [2017年3月28日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 办理任务-修改业务数据
 * @version 
 * @author 傅泉明
 */
public interface WorkflowTaskEditDataFilter {

    public void edit(HttpServletRequest request, Map<String, Object> variables);
    
}

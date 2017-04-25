/*
 * @(#)StatusCode.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年4月7日
 * 修改历史 : 
 *     1. [2017年4月7日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.common;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface StatusCode extends com.fcc.commons.web.common.StatusCode {

    public interface Workflow {
        /** 不是“未启动”状态 workflow_000 */
        String errorStatus = "workflow_000";
    }
    
}

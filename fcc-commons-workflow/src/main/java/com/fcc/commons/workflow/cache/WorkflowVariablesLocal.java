/*
 * @(#)WorkflowLocal.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年6月27日
 * 修改历史 : 
 *     1. [2017年6月27日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.cache;

import java.util.Map;

/**
 * 存储工作流线程变量
 * @version 
 * @author 傅泉明
 */
public class WorkflowVariablesLocal {

    static ThreadLocal<Map<String, Object>> variablesThreadLocal = new ThreadLocal<Map<String,Object>>();
    
    public static void setVariables(Map<String, Object> variables) {
        variablesThreadLocal.set(variables);
    }
    
    public static Map<String, Object> getVariables() {
        return variablesThreadLocal.get();
    }
}

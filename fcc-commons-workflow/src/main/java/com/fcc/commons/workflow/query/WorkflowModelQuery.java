/*
 * @(#)WorkflowModelQuery.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-workflow
 * 创建日期 : 2017年3月24日
 * 修改历史 : 
 *     1. [2017年3月24日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.query;

/**
 * 流程模型查询参数
 * @version 
 * @author 傅泉明
 */
public interface WorkflowModelQuery {
    /**
     * 模型名称
     * @param modelNameLike
     * @return
     */
    String modelNameLike(String modelNameLike);
    /**
     * 模型KEY
     * @param modelKey
     * @return
     */
    String modelKey(String modelKey);
    
}

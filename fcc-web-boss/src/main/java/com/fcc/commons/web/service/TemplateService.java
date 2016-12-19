/*
 * @(#)TemplateService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月16日
 * 修改历史 : 
 *     1. [2016年12月16日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.util.Map;

/**
 * 模板
 * @version 
 * @author 傅泉明
 */
public interface TemplateService {
    /**
     * 获取模板内容
     * @param templateName      模板名称
     * @param contentMap        替换的内容
     * @return
     * @throws Exception
     */
    String getMailContent(String mailTemplateName, Map<String, String> contentMap) throws Exception;
    
}

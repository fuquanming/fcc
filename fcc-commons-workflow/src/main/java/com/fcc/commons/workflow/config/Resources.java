/*
 * @(#)Resources.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月19日
 * 修改历史 : 
 *     1. [2016年10月19日]创建文件 by 傅泉明
 */
package com.fcc.commons.workflow.config;

import java.util.ResourceBundle;

/**
 * 加载配置文件
 * @version 
 * @author 傅泉明
 */
//@PropertySource(value = { "classpath:config/config.properties", "classpath:config/email.properties"})
public class Resources {
    /** activiti */
    public static final ResourceBundle ACTIVITI = ResourceBundle.getBundle("config/activiti");
    
}

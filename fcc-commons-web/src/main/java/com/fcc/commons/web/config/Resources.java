/*
 * @(#)Resources.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月19日
 * 修改历史 : 
 *     1. [2016年10月19日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.config;

import java.util.ResourceBundle;

import org.springframework.context.annotation.PropertySource;

/**
 * 加载配置文件
 * @version 
 * @author 傅泉明
 */
@PropertySource(value = { "classpath:config/config.properties", "classpath:config/email.properties"})
public final class Resources {

    /** 系统配置 */
    public static final ResourceBundle DB = ResourceBundle.getBundle("config/db");
    /** 邮箱服务器配置 */
    public static final ResourceBundle EMAIL = ResourceBundle.getBundle("config/email");
    /** 系统配置 */
    public static final ResourceBundle CONFIG = ResourceBundle.getBundle("config/config");
    
    
}

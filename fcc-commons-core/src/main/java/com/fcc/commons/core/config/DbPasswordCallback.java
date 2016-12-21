/*
 * @(#)DBPasswordCallback.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年12月20日
 * 修改历史 : 
 *     1. [2016年12月20日]创建文件 by 傅泉明
 */
package com.fcc.commons.core.config;

import java.security.Key;
import java.util.Properties;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.fcc.commons.utils.EncryptionUtil;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class DbPasswordCallback extends DruidPasswordCallback {

    public static final String DB_KEY_STRING = "fcc-db";
    
    public static final Key DB_KEY = EncryptionUtil.getKeyDES(DB_KEY_STRING, null);
    
    /**
     * 
     */
    private static final long serialVersionUID = 7741631530036929316L;
    
    private boolean isLoad = false;

    public void setProperties(Properties properties) {
//        super.setProperties(properties);
        if (isLoad) return;
        String pwd = properties.getProperty("password");
        if (pwd != null && !"".equals(pwd)) {
            try {
                String password = EncryptionUtil.decryptDES(DB_KEY, pwd, null);
                setPassword(password.toCharArray());
                isLoad = true;
            } catch (Exception e) {
                setPassword(pwd.toCharArray());
            }
        } else {
            setPassword("".toCharArray());
        }
    }
}

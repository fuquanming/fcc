/*
 * @(#)DynamicDataSourceHolder.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年12月21日
 * 修改历史 : 
 *     1. [2016年12月21日]创建文件 by 傅泉明
 */
package com.fcc.commons.core.datasource;

/**
 * 使用ThreadLocal来记录当前线程中的数据源的key
 * @version 
 * @author 傅泉明
 */
public class DynamicDataSourceHolder {
    /** 使用ThreadLocal记录当前线程的数据源key */
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();
    
    /** 
     * 设置数据源key 
     * @param key 
     */  
    public static void setDataSourceKey(String key) {  
        holder.set(key);  
    }  
  
    /** 
     * 获取数据源key 
     * @return 
     */  
    public static String getDataSourceKey() {  
        return holder.get();  
    }  
}

/*
 * @(#)FileThreadVariable.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-file
 * 创建日期 : 2018年1月23日
 * 修改历史 : 
 *     1. [2018年1月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.file;

/**
 * 文件上传时使用的文件变量，可以配合FileService附带其他参数使用
 * @version 
 * @author 傅泉明
 */
public class FileThreadVariable {
    
    private static ThreadLocal<Object> sysUserThreadLocal = new ThreadLocal<Object>();

    public static void setObject(Object obj) {
        sysUserThreadLocal.set(obj);
    }

    public static Object getObject() {
        return sysUserThreadLocal.get();
    }
}

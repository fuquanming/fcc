/*
 * @(#)BaseFileService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-file
 * 创建日期 : 2018年1月23日
 * 修改历史 : 
 *     1. [2018年1月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.file;

/**
 * 抽象的基类
 * @version 
 * @author 傅泉明
 */
public abstract class BaseFileService implements FileService {

    public Object getObject() {
        return FileThreadVariable.getObject();
    }
    
    public void setObject(Object obj) {
        FileThreadVariable.setObject(obj);
    }

}

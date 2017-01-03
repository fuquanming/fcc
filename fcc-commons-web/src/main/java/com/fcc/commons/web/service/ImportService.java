/*
 * @(#)ImportService.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月28日
 * 修改历史 : 
 *     1. [2016年10月28日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.util.List;

/**
 * 数据导入
 * @version 
 * @author 傅泉明
 */
public interface ImportService {
    /**
     * 转化一行数据为一个对象
     * @param src        需要转化的对象
     * @return           需要持久化的对象
     */
    public Object converObject(Object src);
    /**
     * 保存数据
     * @param dataList
     */
    public void addData(List<Object> dataList);
    
}

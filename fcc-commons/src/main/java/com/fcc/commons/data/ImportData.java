/*
 * @(#)ImportData.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2018年1月9日
 * 修改历史 : 
 *     1. [2018年1月9日]创建文件 by 傅泉明
 */
package com.fcc.commons.data;

import java.util.List;

/**
 * 导入数据
 * @version 
 * @author 傅泉明
 */
public interface ImportData {
    /**
     * 转化一行数据为一个对象
     * @param src        需要转化的对象
     * @return           需要持久化的对象
     */
    public Object importDataConver(Object src);
    /**
     * 保存数据
     * @param dataList
     */
    public void addData(List<Object> dataList);
}

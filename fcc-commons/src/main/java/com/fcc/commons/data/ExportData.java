/*
 * @(#)ExportData.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2018年1月9日
 * 修改历史 : 
 *     1. [2018年1月9日]创建文件 by 傅泉明
 */
package com.fcc.commons.data;

/**
 * 导出数据
 * @version 
 * @author 傅泉明
 */
public interface ExportData {
    /**
     * 将数据转换为导出的对象
     * @return
     */
    public Object exportDataConver(Object obj);
}

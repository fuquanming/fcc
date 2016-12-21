/*
 * @(#)DynamicDataSourceEnum.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年12月21日
 * 修改历史 : 
 *     1. [2016年12月21日]创建文件 by 傅泉明
 */
package com.fcc.commons.core.datasource;

/**
 * 动态数据源类型
 * @version 
 * @author 傅泉明
 */
public enum DynamicDataSourceEnum {
    
    master("master"), slave("slave");

    private final String info;

    private DynamicDataSourceEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}

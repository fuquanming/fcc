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
     * 转化数据为对象
     * @return
     */
    public Object dataConver(List<String> dataList);
    /**
     * 保存数据 
     * @param dataList
     */
    public void saveData(List<Object> dataList);
    
}

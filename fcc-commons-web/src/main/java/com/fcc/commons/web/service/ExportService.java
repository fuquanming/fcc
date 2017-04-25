/*
 * @(#)ExportService.java
 * 
 * Copyright (c) 2015 , All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年10月27日
 * 修改历史 : 
 *     1. [2016年10月27日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.util.List;

/**
 * 数据导出
 * @version 
 * @author 傅泉明
 */
public interface ExportService {

    /**
     * 转化数据为字符串
     * @return
     */
    public List<String> dataConver(Object converObj);
    
}

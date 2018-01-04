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

import java.io.IOException;

import org.dom4j.Document;

/**
 * 数据导出
 * @version 
 * @author 傅泉明
 */
public interface ExportXmlService {

    /**
     * 转化xml格式
     * @param productView   产品
     * @param document      dom4j
     */
    public void converXml(Object obj, Document document) throws IOException;
}

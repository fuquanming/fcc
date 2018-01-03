/*
 * @(#)HttpRequest.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http;

import java.net.URL;
import java.util.List;

/**
 * Http请求
 * @version 
 * @author 傅泉明
 */
public interface HttpRequest {
    /**
     * 请求资源
     * @return
     */
    URL getURL();
    /**
     * 添加请求头信息
     * @param key
     * @param value
     */
    void addHeader(String key, String value);
    /**
     * 添加请求参数
     * @param key
     * @param value
     */
    void addParam(String key, String value);
    /**
     * 添加请求参数
     * @param key
     * @param value
     */
    void addParam(String key, List<String> value);
    /**
     * 添加文件
     * @param fileInfo
     */
    void addFile(FileInfo fileInfo);
    /**
     * 设置请求Body内容
     * @param body
     */
    void setBody(String body);
    /**
     * 执行请求
     */
    HttpResponse execute();
    /**
     * 关闭链接
     */
    void close();
}

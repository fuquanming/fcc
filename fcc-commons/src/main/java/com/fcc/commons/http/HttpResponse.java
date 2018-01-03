/*
 * @(#)HttpResponse.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Http返回
 * @version 
 * @author 傅泉明
 */
public interface HttpResponse {
    /**
     * 返回的状态码
     * @return
     */
    int getResponseCode();
    /**
     * 返回头信息
     * @return
     */
    Map<String, List<String>> getHeader();
    /**
     * 返回数据流
     * @return
     */
    InputStream getInputStream() throws IOException;
    
}

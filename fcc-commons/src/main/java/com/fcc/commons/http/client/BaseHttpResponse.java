/*
 * @(#)BaseHttpResponse.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import com.fcc.commons.http.HttpResponse;

/**
 * Http返回基类
 * @version 
 * @author 傅泉明
 */
public abstract class BaseHttpResponse implements HttpResponse {

    protected HttpURLConnection httpURLConnection;
    
    public BaseHttpResponse(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }
    
    /**
     * @see com.fcc.commons.http.HttpResponse#getResponseCode()
     **/
    @Override
    public int getResponseCode() {
        try {
            return httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @see com.fcc.commons.http.HttpResponse#getHeader()
     **/
    @Override
    public Map<String, List<String>> getHeader() {
        return httpURLConnection.getHeaderFields();
    }

    /**
     * @see com.fcc.commons.http.HttpResponse#getInputStream()
     **/
    @Override
    public InputStream getInputStream() throws IOException {
        return httpURLConnection.getInputStream();
    }

}

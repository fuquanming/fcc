/*
 * @(#)HttpGet.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http.client;

import java.io.IOException;

import com.fcc.commons.http.HttpResponse;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class HttpGet extends BaseHttpRequest {

    private HttpResponse httpResponse;
    
    public HttpGet(String urlStr) {
        super(urlStr);
    }
    
    public HttpGet(String urlStr, String requestCharset) {
        super(urlStr, requestCharset);
    }
    
    @Override
    public HttpResponse execute() {
        try {
            // 请求的参数
            String paramsStr = getParamsStr();
            String tempPath = urlStr;
            if (tempPath.contains("?")) {
                tempPath = tempPath + ("".equals(paramsStr) ? "" : "&" + paramsStr);
            } else {
                tempPath = tempPath + "?" + paramsStr;
            }
            urlStr = tempPath;
            // 设定请求的方法，默认是GET
            requestMethod = "GET";
            connection();
            writeBody();
            
            dos.flush();
            httpResponse = new BaseHttpResponse(httpURLConnection) {};
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.httpResponse;
    }
    
}

/*
 * @(#)HttpPost.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http.client;

import com.fcc.commons.http.HttpResponse;

/**
 * HttpPost提交数据
 * @version 
 * @author 傅泉明
 */
public class HttpPost extends BaseHttpRequest {

    private HttpResponse httpResponse;
    
    public HttpPost(String urlStr) {
        super(urlStr);
    }
    
    public HttpPost(String urlStr, String requestCharset) {
        super(urlStr, requestCharset);
    }
    
    @Override
    public HttpResponse execute() {
        try {
            // 设定请求的方法，默认是GET
            requestMethod = "POST";
            connection();
            if (fileInfos != null && fileInfos.size() > 0) {// 附件上传
                writeMultipart();
            } else {// Post请求
                writeParam();
                writeBody();
            }
            
            dos.flush();
            httpResponse = new BaseHttpResponse(httpURLConnection) {};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.httpResponse;
    }
    
}

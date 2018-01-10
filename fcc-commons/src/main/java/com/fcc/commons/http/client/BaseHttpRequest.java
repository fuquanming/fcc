/*
 * @(#)BaseHttpRequest.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : wangji-file-client
 * 创建日期 : 2017年12月26日
 * 修改历史 : 
 *     1. [2017年12月26日]创建文件 by 傅泉明
 */
package com.fcc.commons.http.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fcc.commons.http.FileInfo;
import com.fcc.commons.http.HttpRequest;

/**
 * Http请求基类
 * @version 
 * @author 傅泉明
 */
public abstract class BaseHttpRequest implements HttpRequest {
    /** 请求的地址 */
    protected String urlStr;
    /** 请求资源 */
    protected URL url;
    /** 请求Http链接 */
    protected HttpURLConnection httpURLConnection;
    
    protected String requestMethod;
    /** 输入流 */
    protected DataOutputStream dos = null; 
    /** 请求字符集 */
    protected String requestCharset = "UTF-8";
    /** 请求头信息 */
    protected Map<String, String> headers;
    /** 请求的body */
    protected String body;
    /** 请求的参数 */
    protected Map<String, Object> params;
    /** 请求的文件 */
    protected List<FileInfo> fileInfos;
    
    /** 文件上传分隔符 */
    private String boundary = "uuid:fccMultipart";
    
    public BaseHttpRequest(String urlStr) {
        this.urlStr = urlStr;
    }
    
    public BaseHttpRequest(String urlStr, String requestCharset) {
        if (requestCharset != null) this.requestCharset = requestCharset;
        this.urlStr = urlStr;
    }
    
    public void connection() throws IOException {
        url = new URL(urlStr);
        // 连接类的父类，抽象类
        URLConnection urlConnection = url.openConnection();
        // http的连接类
        httpURLConnection = (HttpURLConnection) urlConnection;
        // 设置是否从httpUrlConnection读入，默认情况下是true;
        httpURLConnection.setDoInput(true);
        // 设置是否向httpUrlConnection输出
        httpURLConnection.setDoOutput(true);
        // Post 请求不能使用缓存
        httpURLConnection.setUseCaches(false);
        // 设定请求的方法，默认是GET
        httpURLConnection.setRequestMethod(requestMethod);
        // 设置字符编码
        addHeader("Charset", requestCharset);
        // 设置连接方式参数
        addHeader("Connection", "Keep-Alive");
        // 设置请求内容类型
        if (fileInfos != null && fileInfos.size() > 0) {// 附件上传
            addHeader("Content-Type", "multipart/form-data;boundary=" + boundary);
        } else {
            addHeader("Content-type", "application/x-www-form-urlencoded");
        }
        if (headers != null) {
            for (String key : headers.keySet()) {
                addHeader(key, headers.get(key));
            }
        }
        // 设置DataOutputStream
        dos = new DataOutputStream(httpURLConnection.getOutputStream());
    }
    
    public void writeParam() throws IOException {
        String str = this.getParamsStr();
        if (str != null && !"".equals(str)) {
            dos.write(str.getBytes(requestCharset));
        }
    }
    
    public void writeBody() throws IOException {
        if (body != null && !"".equals(body)) {
            dos.write(this.body.getBytes(requestCharset));
        }
    }
    
    @SuppressWarnings("unchecked")
    public void writeMultipart() throws IOException {
        String requestCharset = "UTF-8";
        
        String split = "--" + boundary;
        byte[] splitByte = split.getBytes();
        String rn = "\r\n";
        byte[] rnByte = "\r\n".getBytes();
        String end =  "\r\n--" + boundary + "--\r\n";
        
        dos.write(rnByte);
        // 请求参数
        if (params != null) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof List) {
                    List<String> values = (List<String>) value;
                    for (String v : values) {
                        dos.write(splitByte);
                        dos.write(rnByte);
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
                        dos.write(rnByte);
                        dos.write(rnByte);
                        dos.write(v.getBytes(requestCharset));
                        dos.write(rnByte);
                    }
                } else {
                    dos.write(splitByte);
                    dos.write(rnByte);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
                    dos.write(rnByte);
                    dos.write(rnByte);
                    dos.write(value.toString().getBytes(requestCharset));
                    dos.write(rnByte);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        // 上传文件
        if (fileInfos != null) {
            for (FileInfo info : fileInfos) {
                String key = info.getFormName();
                String fileName = info.getFileName();
                dos.write(splitByte);
                dos.write(rnByte);
                sb.append("Content-Disposition: form-data; name=\"").append(key)
                .append("\"; filename=\"").append(fileName).append("\"").append(rn);
                dos.write(sb.toString().getBytes(requestCharset));
                dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");  
                byte[] fileByte = null;
                if (info.getFileByte() != null) {
                    fileByte = info.getFileByte();
                } else {
                    fileByte = getFileByte(info.getFile());
                }
                dos.write(fileByte);
                dos.write(rnByte);
                
                sb.delete(0, sb.length());
            }
        }
        dos.writeBytes(end);
        dos.flush();
    }
    
    /**
     * @see com.fcc.commons.http.HttpRequest#getURL()
     **/
    @Override
    public URL getURL() {
        return url;
    }

    /**
     * @see com.fcc.commons.http.HttpRequest#addHeader(java.lang.String, java.lang.String)
     **/
    @Override
    public void addHeader(String key, String value) {
        if (httpURLConnection == null) {
            if (headers == null) headers = new HashMap<String, String>();
            headers.put(key, value);
        } else {
            httpURLConnection.setRequestProperty(key, value);
        }
    }

    /**
     * @see com.fcc.commons.http.HttpRequest#addParam(java.lang.String, java.lang.String)
     **/
    @Override
    public void addParam(String key, String value) {
        initParams();
        params.put(key, value);
    }
    
    @Override
    public void addParam(String key, List<String> value) {
        initParams();
        params.put(key, value);
    }

    @Override
    public void addFile(FileInfo fileInfo) {
        if (fileInfos == null) fileInfos = new ArrayList<FileInfo>();
        fileInfos.add(fileInfo);
    }
    
    /**
     * @see com.fcc.commons.http.HttpRequest#setBody(java.lang.String)
     **/
    @Override
    public void setBody(String body) {
        this.body = body;
    }
    
    /**
     * @see com.fcc.commons.http.HttpRequest#close()
     **/
    @Override
    public void close() {
        IOUtils.closeQuietly(dos);
    }
    
    @SuppressWarnings("unchecked")
    public String getParamsStr() {
        if (params == null || params.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = params.get(key);
                if (value != null) {
                    if (value instanceof List) {
                        List<String> values = (List<String>) value;
                        for (String v : values) {
                            sb.append(key).append("=").append(URLEncoder.encode(v, requestCharset)).append("&");
                        }
                    } else {
                        sb.append(key).append("=").append(URLEncoder.encode(value.toString(), requestCharset)).append("&");
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    private void initParams() {
        if (this.params == null) this.params = new HashMap<String, Object>();
    }
    
    private byte[] getFileByte(File file) {
        FileInputStream fis = null;
        byte[] fileByte = null;
        try {
            fis = new FileInputStream(file);
            fileByte = IOUtils.toByteArray(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return fileByte;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}

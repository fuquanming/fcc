/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:HttpParams.java</p>
 * <p>Package:com.fcc.commons.http</p>
 * <p>Description:http请求参数</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
public class HttpParams {
	/** 参数编码 */
	private String charset = "UTF-8";
	/** 参数 两种类型：string或string[] */
	private Map<String, Object> params;
	
	public HttpParams() {
		this.params = new HashMap<String, Object>();
	}
	/**
	 * 
	 * @param charset
	 * @param params value:string或List<String>
	 */
	public HttpParams(String charset, Map<String, Object> params) {
		this.charset = charset;
		this.params = params;
		if (this.charset == null) this.charset = "UTF-8";
	}
	
	public void addParam(String key, Object value) {
		params.put(key, value);
	}
	
	/**
	 * 将Map里的参数拼接成string
	 * @return a=1&b=2
	 */
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
							sb.append(key).append("=").append(URLEncoder.encode(v, charset)).append("&");
						}
					} else {
						sb.append(key).append("=").append(URLEncoder.encode(value.toString(), charset)).append("&");
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}

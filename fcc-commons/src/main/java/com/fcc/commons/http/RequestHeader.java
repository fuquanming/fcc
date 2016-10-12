/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Title:RequestHeader.java</p>
 * <p>Package:com.fcc.commons.http</p>
 * <p>Description:http请求头</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class RequestHeader {
	
	private Map<String, String> headers = new HashMap<String, String>();
	/**
	 * @param key	Accept-Language
	 * @param value	zh-cn,zh;q=0.5
	 */
	public void setHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public String getHeader(String key) {
		return headers.get(key);
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	/**
	 * 将header 构建成字符串
	 * @return HOST:111
	 */
	public String getHeaderStr() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = headers.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = headers.get(key);
			sb.append(key).append(":").append(value).append("\r\n");
		}
		return sb.toString();
	}
}

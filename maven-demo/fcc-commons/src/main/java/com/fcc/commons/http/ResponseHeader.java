/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title:ResponseHeader.java</p>
 * <p>Package:com.fcc.commons.http</p>
 * <p>Description:http请求返回头</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class ResponseHeader {
	
	public static final String HEADER_STATUS_LINE = "Stauts_Line";
	
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, Object> heads = new HashMap<String, Object>();
	/**
	 * @param key	Accept-Language
	 * @param value	zh-cn,zh;q=0.5
	 */
	@Deprecated
	public void setHeader(String key, String value) {
		headers.put(key, value);
	}
	@Deprecated
	public String getHeader(String key) {
		return headers.get(key);
	}
	@Deprecated
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * @param key	Accept-Language
	 * @param value	zh-cn,zh;q=0.5
	 */
	@SuppressWarnings("unchecked")
	public void setHeads(String key, String value) {
		if (heads.containsKey(key)) {
			Object val = heads.get(key);
			if (val instanceof String) {
				Collection<String> list = new ArrayList<String>();
				list.add(val.toString());
				list.add(value);
				val = list;
			} else if (val instanceof List) {
				((List<String>)val).add(value);
			}
			heads.put(key, val);
		} else {
			heads.put(key, value);
		}
		headers.put(key, value);
	}
	
	public Object getHeads(String key) {
		return heads.get(key);
	}
	/**
	 * Object 为String或Collection
	 * @return
	 */
	public Map<String, Object> getHeads() {
		return heads;
	}
	
}

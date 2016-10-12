/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods;

import java.io.InputStream;

import com.fcc.commons.http.ResponseHeader;

/**
 * <p>Title:Response.java</p>
 * <p>Package:com.fcc.commons.http.client.methods</p>
 * <p>Description:Http请求返回</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public interface Response {
	/**
	 * 返回状态码 Stauts_Line:
	 * @return
	 */
	String getStatusLine();
	/**
	 * 返回请求头
	 * @return
	 */
	ResponseHeader getResponseHeader();
	/**
	 * 返回数据流
	 * @return
	 */
	InputStream getInputStream();
	
}

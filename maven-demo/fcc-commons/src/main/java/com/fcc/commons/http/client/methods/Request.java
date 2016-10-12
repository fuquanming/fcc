/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods;

import java.net.URL;

import com.fcc.commons.http.RequestBody;
import com.fcc.commons.http.RequestHeader;

/**
 * <p>Title:Request.java</p>
 * <p>Package:com.fcc.commons.http.client.methods</p>
 * <p>Description:请求</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public interface Request {

	URL getURL();
	/**
	 * Http请求内容
	 * @return
	 */
	RequestBody getRequestBody();
	/**
	 * Http请求头
	 * @return
	 */
	RequestHeader getRequestHeader();
	/**
	 * 执行请求
	 * @return Response
	 */
	Response execute();
	/**
	 * 关闭Http请求
	 */
	void close();
	
}

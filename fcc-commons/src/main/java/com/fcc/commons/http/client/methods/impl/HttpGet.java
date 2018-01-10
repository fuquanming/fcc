/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.fcc.commons.http.client.methods.Response;
/**
 * <p>Title:Request.java</p>
 * <p>Package:com.fcc.commons.http.client.methods</p>
 * <p>Description:Http,Get请求</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class HttpGet extends RequestBase {

	public final static String METHOD_NAME = "GET";
	
//	private Logger logger = Logger.getLogger(HttpGet.class);
	
	private HttpGetResponse response = new HttpGetResponse();
	
	public HttpGet(String urlStr) {
		super(urlStr);
		requestMethod = METHOD_NAME;
	}
	
	public HttpGet(String urlStr, int requestTimeout) {
		super(urlStr, requestTimeout);
		requestMethod = METHOD_NAME;
	}
	
	public URL getURL() {
		return url;
	}
	
	public Response execute() {
		try {
			if (socket == null) {
				connection();
			}
			if (socket == null) return response;
			if (headers == null) headers = requestHeader.getHeaderStr();
			String paramsStr = requestBody.getHttpParams().getParamsStr();
			String tempPath = getPath();
			if (tempPath.contains("?")) {
				tempPath = tempPath + ("".equals(paramsStr) ? "" : "&" + paramsStr);
			} else {
				tempPath = tempPath + "?" + paramsStr;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(requestMethod).append(" ").append(tempPath).append(" HTTP/").append(httpAgreement).append("\r\n")
			.append("HOST:").append(host).append("\r\n")
			.append("User-Agent: FuQuanming\r\n");
			if (!"".equals(headers)) {
				sb.append(headers);
			}
			sb.append("Accept:*/*\r\n")
			.append("\r\n");
			
			dos.write(sb.toString().getBytes());
			String body = requestBody.getBody();
			if (body != null && !"".equals(body)) dos.write(body.getBytes(requestCharset));
			
			dos.flush();
			
			response.buildHeader(socket.getInputStream());
		} catch (UnsupportedEncodingException e) {
//			logger.error(e);
			response.statusLine = null;
		} catch (IOException e) {
//			logger.error(e);
			response.statusLine = null;
		} catch (Exception e) {
//			logger.error(e);
			response.statusLine = null;
		}
		return response;
	}
	
	public void close() {
		IOUtils.closeQuietly(dos);
		IOUtils.closeQuietly(response.bis);
		IOUtils.closeQuietly(response.is);
		IOUtils.closeQuietly(socket);
		dos = null;
		response.bis = null;
		response.is = null;
		socket = null;
		response.readByte = null;
	}
	
	class HttpGetResponse extends ResponseBase {
		
	}
	
}

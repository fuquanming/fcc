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
 * <p>Description:Http，POST请求</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class HttpPost extends RequestBase {

//	private Logger logger = Logger.getLogger(HttpPost.class);
	public final static String METHOD_NAME = "POST";
	
	private HttpPostResponse response = new HttpPostResponse();
	
	public HttpPost(String urlStr) {
		super(urlStr);
	}
	
	public HttpPost(String urlStr, int requestTimeout) {
		super(urlStr, requestTimeout);
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
			String params = requestBody.getHttpParams().getParamsStr();
			String body = requestBody.getBody();
			if (body == null) body = "";
			byte[] bodyByte = body.getBytes(requestCharset);
			byte[] paramByte = params.getBytes();
			
			StringBuilder sb = new StringBuilder();
			sb.append("POST ").append(getPath()).append(" HTTP/").append(httpAgreement).append("\r\n")
			.append("HOST:").append(host).append("\r\n")
			.append("User-Agent: FuQuanming\r\n")
			.append("Content-Length: ").append(paramByte.length + bodyByte.length).append("\r\n");
			
			if (!"".equals(headers)) {
				sb.append(headers);
				if (!headers.contains("Content-Type")) {
					sb.append("Content-Type: application/x-www-form-urlencoded\r\n");
				}
			} else {
				sb.append("Content-Type: application/x-www-form-urlencoded\r\n");
			}
			sb.append("Accept:*/*\r\n")
			.append("\r\n");
			dos.write(sb.toString().getBytes());
			
			if (!"".equals(params)) dos.write(paramByte);
			if (!"".equals(body)) dos.write(bodyByte);
			
			dos.flush();
			response.buildHeader(socket.getInputStream());
		} catch (UnsupportedEncodingException e) {
			response.statusLine = null;
//			logger.error(e);
		} catch (IOException e) {
			response.statusLine = null;
//			logger.error(e);
		} catch (Exception e) {
			response.statusLine = null;
			e.printStackTrace();
//			logger.error(e);
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
	
	class HttpPostResponse extends ResponseBase {
		
	}
	
}

/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods.impl;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.fcc.commons.http.RequestBody;
import com.fcc.commons.http.RequestHeader;
import com.fcc.commons.http.client.methods.Request;
import com.fcc.commons.socket.SocketUtil;
/**
 * <p>Title:RequestBase.java</p>
 * <p>Package:com.fcc.commons.http.client.methods.impl</p>
 * <p>Description:处理请求数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public abstract class RequestBase implements Request {
	
//	private Logger logger = Logger.getLogger(RequestBase.class);
	protected URL url;
	
	protected Map<String, String> urlParams;
	
	protected String requestCharset = "UTF-8";
	protected String responseCharset = "UTF-8";
	
	protected RequestBody requestBody = new RequestBody();
	protected RequestHeader requestHeader = new RequestHeader();
	
	protected Socket socket;
	// socket取得输入流
	protected DataOutputStream dos = null;
	protected String host;
	protected String headers;
	
	protected String httpAgreement = "1.0";
	// 请求超时时间
	protected int requestTimeout = 0;
	// 读取超时时间
	protected int readTimeout = 0;
	
	public RequestBase() {
	}
	
	public RequestBase(String urlStr) {
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
//			logger.error(e);
			e.printStackTrace();
		}
		urlParams = SocketUtil.getParams(urlStr);
		connection();
	}
	
	public RequestBase(String urlStr, int requestTimeout) {
		this.requestTimeout = requestTimeout;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
//			logger.error(e);
			e.printStackTrace();
		}
		urlParams = SocketUtil.getParams(urlStr);
		connection();
	}
	
	public boolean connection() {
		try {
			socket = getSocket();
			dos = new DataOutputStream(socket.getOutputStream());
			host = getHost();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Socket getSocket() throws Exception {
		if (urlParams != null) {
			String ip = urlParams.get(SocketUtil.URL_IP);
			String domainName = urlParams.get(SocketUtil.URL_DOMAIN_NAME);
			if (ip != null) {
				return SocketUtil.getSocketByIp(ip, Integer.parseInt(urlParams.get(SocketUtil.URL_PORT)), requestTimeout, readTimeout);
			}
			if (domainName != null) {
				return SocketUtil.getSocketByDomainName(domainName, Integer.parseInt(urlParams.get(SocketUtil.URL_PORT)), requestTimeout, readTimeout);
			}
		}
		return null;
	}
	
	public String getHost() {
		String host = urlParams.get(SocketUtil.URL_HOST);
		host = host + ":" + urlParams.get(SocketUtil.URL_PORT);
		return host;
	}
	
	public String getPath() {
		String path = urlParams.get(SocketUtil.URL_PATH);
		String query = urlParams.get(SocketUtil.URL_QUERY);
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		if (!"".equals(query)) {
			try {
				String[] params = query.split("&");
				sb.append("?");
				for (String param : params) {
					String[] p = param.split("=");
					if (p.length == 2) {
						sb.append(p[0]).append("=").append(URLEncoder.encode(p[1], requestCharset)).append("&");
					} else {
						sb.append(p[0]).append("=").append("&");
					}
				}
			} catch (UnsupportedEncodingException e) {
//				logger.error(e);
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public RequestBody getRequestBody() {
		return requestBody;
	}

	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	public String getRequestCharset() {
		return requestCharset;
	}

	public void setRequestCharset(String requestCharset) {
		this.requestCharset = requestCharset;
		requestBody.getHttpParams().setCharset(requestCharset);
	}

	public String getResponseCharset() {
		return responseCharset;
	}

	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

	public String getHttpAgreement() {
		return httpAgreement;
	}

	public void setHttpAgreement(String httpAgreement) {
		this.httpAgreement = httpAgreement;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
}

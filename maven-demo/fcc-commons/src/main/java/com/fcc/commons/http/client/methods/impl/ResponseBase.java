/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;

import com.fcc.commons.http.ResponseHeader;
import com.fcc.commons.http.client.methods.Response;
/**
 * <p>Title:ResponseBase.java</p>
 * <p>Package:com.fcc.commons.http.client.methods.impl</p>
 * <p>Description:处理返回数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public abstract class ResponseBase implements Response {

//	private Logger logger = Logger.getLogger(ResponseBase.class);
	protected Pattern statusPattern = Pattern.compile("HTTP/\\d{1}.\\d{1} (\\d{1,5})(.*)");
	protected String statusLine;
	
	protected ResponseHeader responseHeader = new ResponseHeader();
	protected BufferedInputStream bis = null;
	protected InputStream is = null;
	// 默认读取字节总数
	protected int defaultReadByte = 2048;
	// 默认读取字节数组
	protected byte[] readByte = new byte[defaultReadByte];
	
	public String getStatusLine() {
		return statusLine;
	}
	
	public boolean getResponseStatus() {
		return (statusLine.contains(String.valueOf(HttpURLConnection.HTTP_OK)));
	}
	
	private int readHeader() throws IOException {
		bis.mark(defaultReadByte);
		int size = bis.read(readByte);
		int markHeader = -1;// 记录数据开始位置
		int rIndex = 0;
		boolean firstFlag = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			char c = (char) readByte[i];
			sb.append(c);
			if (c == '\r') {
				rIndex = i;
			} else if (c == '\n') {
				if ((rIndex + 1 == i)) {
					String sbStr = sb.toString().substring(0, sb.length() - 2);
					if (sbStr.equals("")) {
						markHeader = i + 1;
						break;
					}
					if (firstFlag == false) {// 记录第一行的返回状态
						responseHeader.setHeads(ResponseHeader.HEADER_STATUS_LINE, sbStr);
						statusLine = sbStr;
						firstFlag = true;
					} else {
						int flag = sbStr.indexOf(":");
						String key = sbStr.substring(0, flag);
						String value = sbStr.substring(flag + 1).replaceFirst(" ", "");
						responseHeader.setHeads(key, value);
					}
					sb.delete(0, sb.length());
				}
			}
		}
		return markHeader;
	}
	
	public void buildHeader(InputStream is) {
		this.is = is;
		bis = new BufferedInputStream(is);
		try {
			int markHeader = readHeader();
			bis.reset();
			bis.skip(markHeader);
		} catch (IOException e) {
//			logger.error(e);
			e.printStackTrace();
		}
		
//		this.is = is;
//		String line = null;
//		try {
//			boolean firstFlag = false;
////			while ((line = IOUtil.readLine(is, "utf-8")) != null) {
//			while ((line = IOUtil.readLineDefault(is)) != null) {
//				if ("".equals(line)) {
//					break;
//				} else {
//					if (firstFlag == false) {// 记录第一行的返回状态
//						responseHeader.setHeader(ResponseHeader.HEADER_STATUS_LINE, line);
//						statusLine = line;
//						firstFlag = true;
//					} else {
//						int flag = line.indexOf(":");
//						String key = line.substring(0, flag);
//						String value = line.substring(flag + 1).replaceFirst(" ", "");
//						responseHeader.setHeader(key, value);
//					}
//				}
//			}
//		} catch (IOException e) {
//			logger.error(e);
//		}
	}
	
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	
	public InputStream getInputStream() {
		return bis;
	}

}

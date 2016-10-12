/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:RequestBody.java</p>
 * <p>Package:com.fcc.commons.http</p>
 * <p>Description:http请求内容</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class RequestBody {

	private HttpParams httpParams = new HttpParams();
	
	private String body;
	/**
	 * key:表单域name
	 */
	private Map<String, File> fileMap;
	/**
	 * key:表单域name, 
	 */
	private Map<String, FileByte> fileByteMap;
	/**
	 * 文件排序 对应fileMap 的key
	 */
	private List<String> fileSortList;
	
	public HttpParams getHttpParams() {
		return httpParams;
	}

	public void setHttpParams(HttpParams httpParams) {
		this.httpParams = httpParams;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, File> getFileMap() {
		return fileMap;
	}

	public void setFileMap(Map<String, File> fileMap) {
		this.fileMap = fileMap;
	}

	public List<String> getFileSortList() {
		return fileSortList;
	}

	public void setFileSortList(List<String> fileSortList) {
		this.fileSortList = fileSortList;
	}

	public Map<String, FileByte> getFileByteMap() {
		return fileByteMap;
	}

	public void setFileByteMap(Map<String, FileByte> fileByteMap) {
		this.fileByteMap = fileByteMap;
	}

}
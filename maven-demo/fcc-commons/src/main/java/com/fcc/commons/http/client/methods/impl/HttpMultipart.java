/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.http.client.methods.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fcc.commons.http.FileByte;
import com.fcc.commons.http.client.methods.Response;
import com.fcc.commons.utils.FileUtil;

/**
 * <p>Title:Request.java</p>
 * <p>Package:com.fcc.commons.http.client.methods</p>
 * <p>Description:Http，表单请求</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
public class HttpMultipart extends RequestBase {

//	private Logger logger = Logger.getLogger(HttpMultipart.class);
	public final static String METHOD_NAME = "POST";
	/** 表单数据 */
	public final static String MULTIPART_FORM_DATA = "multipart/form-data";
	/** 邮件附件 */
	public final static String MULTIPART_RELATED = "multipart/related";
	
	// 分隔符
	private String split = "uuid:---7d" + (int)(Math.random() * 100000000);
	// 开始字符分隔
	private String splitBoundary = split;
	// 中间字符分隔
	private String splitParam =    "--" + split;
	// 结束字符分隔
	private String splitEnd =  "\r\n--" + split + "--\r\n";
	// 数据总数
	private long totalSize = 0;

    private String multipartType = MULTIPART_FORM_DATA;
	
	private String rn = "\r\n";
	private byte[] rnByte = "\r\n".getBytes();
	
	private HttpPostResponse response = new HttpPostResponse();
	
	public HttpMultipart(String urlStr) {
		super(urlStr);
	}
	
	public HttpMultipart(String urlStr, int requestTimeout) {
		super(urlStr, requestTimeout);
	}
	
	public void setMultipart(String multipartType) {
		this.multipartType = multipartType;
	}
	
	public URL getURL() {
		return url;
	}
	   
    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

	public Response execute() {
		try {
			if (socket == null) {
				connection();
			}
			if (socket == null) return response;
			if (headers == null) headers = requestHeader.getHeaderStr();
			// 记录数据大小
			byte[] paramByte = buildHttpParams();
			// 记录文件大小
			byte[] fileByte = buildFiles();
			// 记录数据大小 结束
			// 开始写头信息
			StringBuilder sb = new StringBuilder();
			sb.append("POST ").append(getPath()).append(" HTTP/").append(httpAgreement).append(rn)
			.append("HOST:").append(host).append(rn)
			.append("User-Agent: FuQuanming").append(rn)
			.append("Content-Type: ").append(multipartType).append("; boundary=").append(splitBoundary).append(rn)
			.append("Content-Length: ").append(fileByte.length + splitEnd.getBytes().length + paramByte.length).append(rn)
			;
			dos.write(sb.toString().getBytes());
			if (!"".equals(headers)) {
				dos.write(headers.getBytes());
			}
			dos.write("Accept:*/*\r\n".getBytes());
			dos.write("\r\n".getBytes());
			// 记录数据
			dos.write(paramByte);
			// 记录文件
			dos.write(fileByte);
			
			dos.write(splitEnd.getBytes());
			dos.flush();
			
			response.buildHeader(socket.getInputStream());
		} catch (UnknownHostException e) {
//			logger.error(e);
			response.statusLine = null;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
//			logger.error(e);
			response.statusLine = null;
			e.printStackTrace();
		} catch (IOException e) {
//			logger.error(e);
			response.statusLine = null;
			e.printStackTrace();
		} catch (Exception e) {
//			logger.error(e);
			response.statusLine = null;
			e.printStackTrace();
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
	
	private byte[] buildHttpParams() {
		Map<String, Object> params = requestBody.getHttpParams().getParams();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (params != null) {
			if (params != null && params.size() > 0) {
				// 字段
				Iterator<String> it = params.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object obj = params.get(key);
					List<String> values = null;
					if (obj instanceof String) {
						values = new ArrayList<String>();
						values.add(obj.toString());
					} else if (obj instanceof List) {
						values = (ArrayList<String>)obj;
					}
					if (values == null) continue;
					try {
						for (String value : values) {
							byte[] temp = (splitParam + "\r\nContent-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes();
							baos.write(temp);
							baos.write(value.getBytes(requestCharset));
							temp = "\r\n".getBytes();
							baos.write(temp);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					totalSize += baos.toByteArray().length;
				}
			}
		}
		return baos.toByteArray();
	}
	
	private byte[] buildFiles() {
		Map<String, File> fileMap = requestBody.getFileMap();
		Map<String, FileByte> fileByteMap = requestBody.getFileByteMap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			StringBuilder sb = new StringBuilder();
			List<String> fileSortList = requestBody.getFileSortList();
			if (fileSortList != null && fileSortList.size() > 0) {
				for (String key : fileSortList) {
					if (fileMap != null) {
						File file = fileMap.get(key);
						if (file != null && file.exists() == true) {
							String fileName = file.getName();
							FileInputStream input = new FileInputStream(file);
							byte[] data = IOUtils.toByteArray(input);
							baos.write(data);
							IOUtils.closeQuietly(input);
							buildFile(baos, key, fileName, data, sb);
						}
					}
					if (fileByteMap != null) {
						FileByte fileByte = fileByteMap.get(key);
						if (fileByte != null) {
							buildFile(baos, key, fileByte.getFileName(), fileByte.getFileByte(), sb);
						}
					}
				}
			} else {
				if (fileMap != null) {
					Iterator<String> it = fileMap.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						File file = fileMap.get(key);
						if (file == null || file.exists() == false) continue;
						String fileName = file.getName();
						FileInputStream input = new FileInputStream(file);
						byte[] data = IOUtils.toByteArray(input);
						IOUtils.closeQuietly(input);
						buildFile(baos, key, fileName, data, sb);
					}
				}
				if (fileByteMap != null) {
					Iterator<String> it = fileByteMap.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						FileByte fileByte = fileByteMap.get(key);
						buildFile(baos, key, fileByte.getFileName(), fileByte.getFileByte(), sb);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
//			logger.error(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error(e);
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		}
		return baos.toByteArray();
	}
	
	private void buildFile(ByteArrayOutputStream baos, String key, String fileName, byte[] fileByte, StringBuilder sb) throws Exception {
		// 文件名后缀
		String fileFix = fileName.substring(fileName.lastIndexOf(".") + 1);
		// 文件类型
		String fileContentType = FileUtil.getFileContentType(fileFix);
		if (fileContentType == null) fileContentType = "text/plain";
		int sbLen = sb.length();
		if (sbLen > 0) sb.delete(0, sbLen);
		sb.append(splitParam).append(rn);
		if (multipartType.equals(MULTIPART_FORM_DATA)) {
			sb.append("Content-Disposition: ").append(multipartType).append("; name=\"").append(key).append("\"; filename=\"")
			.append(fileName).append("\"").append(rn)
			.append("Content-Type: ").append(fileContentType).append(rn);
		} else if (multipartType.equals(MULTIPART_RELATED)) {
			sb.append("Content-Type: ").append(fileContentType);
			if (fileContentType.contains("text")) {
				sb.append("; charset=").append(requestCharset).append(";");
			}
			sb.append(rn)
			.append("Content-Transfer-Encoding: binary").append(rn)
			.append("Content-ID: <").append(fileName).append(">").append(rn);
		}
		sb.append(rn)
		;
		baos.write(sb.toString().getBytes(requestCharset));
		// 读出文件字节
		baos.write(fileByte);
		baos.write(rnByte);
	}
	
	class HttpPostResponse extends ResponseBase {
		
	}
	
}

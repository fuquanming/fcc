/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * <p>Title:FileUtils.java</p>
 * <p>Package:com.fcc.commons.file</p>
 * <p>Description:文件处理工具类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("rawtypes")
public class FileUtil {
	// 文件类型
	private static String FILE_TYPE = "file-type.xml";
	private static Map<String, String> fileTypeMap = new HashMap<String, String>();
	private static Map<String, String> contentTypeMap = new HashMap<String, String>();
	
	static {
		String path = ClassUtil.getClassRootPath();
		// 读取文件
		InputStream is = null;
		try {
			is = new FileInputStream(path + FILE_TYPE);
			SAXReader saxReader = new SAXReader();
			Document xmlDoc = saxReader.read(is);
			Element rootEle = xmlDoc.getRootElement();
			// 加载数据
//			<mime-mapping>
//		        <extension>abs</extension>
//		        <mime-type>audio/x-mpeg</mime-type>
//		    </mime-mapping>
			Iterator mimeIt = rootEle.elementIterator("mime-mapping");
			while (mimeIt.hasNext()) {
				Element mimeEle = (Element) mimeIt.next();
				String fileFix = mimeEle.elementTextTrim("extension");
				String type = mimeEle.elementTextTrim("mime-type");
				fileTypeMap.put(fileFix, type);
				contentTypeMap.put(type, fileFix);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * 删除该目录下的所有文件
	 * @param filePath
	 */
	public static boolean delFile(String filePath) {
		if (filePath != null) {
			File file = new File(filePath);
			if (!file.exists()) return false;
			if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				for (File data : childFiles) {
					delFile(data.getPath());
				}
			}
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 取得文件的描述类型 例如：Content-Type:text/plain
	 * @param fileFix	文件后缀 txt
	 * @return
	 */
	public static String getFileContentType(String fileFix) {
		return fileTypeMap.get(fileFix);
	}
	/**
	 * 取得文件后缀 例如：Content-Type:text/plain
	 * @param contentType	
	 * @return
	 */
	public static String getFileFix(String contentType) {
		return contentTypeMap.get(contentType);
	}
	
}

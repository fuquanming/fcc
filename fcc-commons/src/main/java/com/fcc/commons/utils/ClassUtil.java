/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * <p>Title:ClassUtils.java</p>
 * <p>Package:com.fcc.commons.file</p>
 * <p>Description:类工具类：路径</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class ClassUtil {
	/**
	 * 取得classpath
	 * @return
	 */
	public static String getClassRootPath() {
		URL url = ClassUtil.class.getResource("/");
		String path = url.getPath();
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	}
	/**
	 * 取得该类的 classpath
	 * @param c
	 * @return
	 */
	public static String getClassPath(Class<?> c) {
		String path = getClassRootPath();
		String name = c.getName().replace(".", "/");
		name = name.substring(0, name.lastIndexOf("/") + 1);
		return path + name;
	}
}

/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.web.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title:CookieUtil.java</p>
 * <p>Package:com.fcc.commons.web</p>
 * <p>Description:cookie帮助类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class CookieUtil {
	/**
	 * 添加cookie
	 * @param response
	 * @param name		名称
	 * @param value		值
	 * @param path		什么路径下可以使用
	 * @param domain	域名
	 * @param time		失效时间
	 * @param secure TODO
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, String path,
			String domain, int time, boolean secure) {
		Cookie cookie;
		try {
			cookie = new Cookie(name, java.net.URLEncoder.encode(value, "utf-8"));
			cookie.setSecure(false);
			if (path != null) cookie.setPath(path);//这个要设置 表示当前同个服务器下的cookie共用
			if (domain != null) cookie.setDomain(domain);//这个也要设置才能实现上面的两个网站共用   (设置对方使用 域名)
			if (time > 0) {
				cookie.setMaxAge(time);
			} else {
				cookie.setMaxAge(24 * 60 * 60);//不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 * @param name		名称
	 */
	public static void delCookie(HttpServletRequest request, HttpServletResponse response, 
			String name) {
//		Cookie[] cookies = request.getCookies();
//		Cookie cookie = null;
//	    if (cookies != null) {
//	   	    int length = cookies.length;
//	        for (int i = 0; i < length; i++) {
//	        	cookie = cookies[i];
//	            if (cookie.getName().equals(name)) {
////	                Cookie cookie_new = new Cookie(name,"");//这边得用"",不能用null
////	                cookie_new.setMaxAge(0);
////	                if (cookie.getPath() != null) cookie_new.setPath(cookie.getPath());//设置成跟写入cookies一样的
////	                if (cookie.getDomain() != null) cookie_new.setDomain(cookie.getDomain());//设置成跟写入cookies一样的
////	                response.addCookie(cookie_new);
//	                
//	            	cookie.setMaxAge(0);
//	            	response.addCookie(cookie);
//	            	System.out.println("CookieUtil.delCookie() " + cookie.getName() + "\t" + cookie.getMaxAge());
////	                break;
//	            }
//	        }
//	    }
		 Cookie cookie_new = new Cookie(name,"");//这边得用"",不能用null
         cookie_new.setMaxAge(0);
         cookie_new.setSecure(false);
         cookie_new.setPath("/");//设置成跟写入cookies一样的
         response.addCookie(cookie_new);
	}
	/**
	 * 取得cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		Cookie cookie = null;
	    if (cookies != null) {
	        int length = cookies.length;
	        boolean findFlag = false;
	        for (int i = 0; i < length; i++) {
	        	cookie = cookies[i];
	            if (cookie.getName().equals(name)) {
	            	findFlag = true;
	            	break;
	            }
	        }
	        if (!findFlag) cookie = null; 
	    }
	    return cookie;
	}
	/**
	 * 取得cookie 的值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		String val = "";
		try {
			val = cookie != null ? java.net.URLDecoder.decode(cookie.getValue(),"UTF-8") : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public static String getCookieValue(Cookie cookie) {
		String val = "";
		try {
			val = cookie != null ? java.net.URLDecoder.decode(cookie.getValue(),"UTF-8") : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}
}

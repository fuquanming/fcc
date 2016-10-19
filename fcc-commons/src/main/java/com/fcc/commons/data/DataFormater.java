/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.data;

import java.lang.reflect.Constructor;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * <p>Title:DataFormater.java</p>
 * <p>Package:com.fcc.commons.data</p>
 * <p>Description:页面显示辅助工具类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class DataFormater {
	/**
	 * 
	 */
	public DataFormater() {
		super();
		// TODO 自动生成构造函数存根
	}

	/**
	 * 修正WEB页显示中的NULL
	 * 
	 * @param arg
	 *            原始参数值
	 * @return 修正后的显示值
	 */
	public static String noNullValue(String arg) {
		String result = "";
		if (arg != null) {
			result = arg;
		}
		return result;
	}

	/**
	 * 修正WEB页显示中的NULL
	 * 
	 * @param arg
	 *            原始参数值
	 * @return 修正后的显示值
	 */
	public static String noNullValue(Object arg) {
		String result = "";
		if (arg != null) {
			result = arg.toString();
		}
		return result;
	}

	/**
	 * 转化WEB页显示的日期
	 * 
	 * @param arg
	 *            原始参数值
	 * @return 修正后的显示值
	 */
	public static String noNullValue(Date arg) {
		return noNullValue(arg, "yyyy年MM月dd日");
	}

	/**
	 * 转化WEB页显示的日期
	 * 
	 * @param arg
	 * @param format
	 *            修正后的日期格式
	 * @return 修正后的结果
	 */
	public static String noNullValue(Date arg, String format) {
		String result = "";
		if (arg != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(arg);
		}
		return result;
	}
	
	public static String noNullValue(java.util.Date arg, String format) {
		String result = "";
		if (arg != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(arg);
		}
		return result;
	}

	/**
	 * 转化WEB页显示的日期时间
	 * 
	 * @param arg
	 *            原始参数值
	 * @return 修正后的显示值
	 */
	public static String noNullValue(Timestamp arg) {
		return noNullValue(arg, "yyyy年MM月dd日 HH时mm分ss秒");
	}

	/**
	 * 转化WEB页显示的日期时间
	 * 
	 * @param arg
	 * @param format
	 *            修正后的日期时间格式
	 * @return 修正后的结果
	 */
	public static String noNullValue(Timestamp arg, String format) {
		String result = "";
		if (arg != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(arg);
		}
		return result;
	}

	/**
	 * 值为0时不显示
	 * 
	 * @param arg
	 *            原始参数值
	 * @return 修正后的显示值
	 */
	public static String noNullValue(double arg) {
		String result = "";
		if (arg != 0) {
			result = Double.toString(arg);
		}
		return result;
	}

	public static String noNullValue(int arg) {
		String result = "";
		if (arg != 0) {
			result = Integer.toString(arg);
		}
		return result;
	}

	public static String noNullValue(long arg) {
		String result = "";
		if (arg != 0) {
			result = Long.toString(arg);
		}
		return result;
	}
	
	public static String noNullValue(Double arg) {
		String result = "";
		if (arg != null) {
			result = noNullValue(arg.doubleValue());
		}
		return result;
	}

	public static String noNullValue(Integer arg) {
		String result = "";
		if (arg != null) {
			result = noNullValue(arg.intValue());
		}
		return result;
	}

	public static String noNullValue(Long arg) {
		String result = "";
		if (arg != null) {
			result = noNullValue(arg.longValue());
		}
		return result;
	}

	/**
     * 
     * Object o = t.getNumber(Long.getClass(), "11111");
     * 给出一个String 型数据 a = 100;
     * 将这个String型转 a 换为Integer
     * @param type
     * @param str
     * @return
     * @throws Exception
     */
    public static Object getNumber(Class type, String str) throws Exception {
        if (str == null || "".equals(str)) return -1;
        Class[] paramsClasses = { str.getClass() };
        Object[] params = { str };
        Constructor c = type.getConstructor(paramsClasses);
        Object o = c.newInstance(params);
        return o;
    }
    
    public static Long[] getLong(String[] str) throws Exception {
        int size = str.length;
        Long[] obj = new Long[size];
        for (int i = 0; i < size; i++) {
            obj[i] = (Long) getNumber(Long.class, str[i]);
        }
        return obj;
    }
    
    public static Integer[] getInteger(String[] str) throws Exception {
        int size = str.length;
        Integer[] obj = new Integer[size];
        for (int i = 0; i < size; i++) {
            obj[i] = (Integer) getNumber(Integer.class, str[i]);
        }
        return obj;
    }
}

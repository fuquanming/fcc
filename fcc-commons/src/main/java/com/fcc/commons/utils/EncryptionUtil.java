/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * <p>Title:EncryptionUtil.java</p>
 * <p>Package:com.fcc.commons.data</p>
 * <p>Description:加密、解密工具类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class EncryptionUtil {

	/**
	 * 对字窜进新MD5摘要,并将摘要转化成16进制编码，大写
	 * 
	 * @param src 原始字符串
	 * @return 摘要编码字窜
	 */
	public static String encodeMD5(String src) {
//		String resultString = null;
//		try {
//			resultString = byte2hex(md5Digest(src.getBytes()));
//		} catch (Exception ex) {
//		}
//		return resultString;
	    return DigestUtils.md5Hex(src);
	}
	
	/**
	 * BASE64 解码. 
     * @param src      待编码的字符串的字节数组
     * @param charset  返回字符串编码
	 * @return
	 */
	public static String decodeBase64(byte[] src, String charset) { 
//        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
//        try { 
//            return new String(decoder.decodeBuffer(src), charset); 
//        } catch (Exception ex) { 
//            return null; 
//        } 
	    String str = null;
	    try {
            if (charset == null) {
                str = new String(Base64.decodeBase64(src));
            } else {
                str = new String(Base64.decodeBase64(src), charset);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	    return str;
    }
	/**
	 * BASE64 编码
	 * @param src      待编码的字符串的字节数组
	 * @param charset  返回字符串编码
	 * @return
	 */
	public static String encodeBase64(byte[] src, String charset) {
//		return (new sun.misc.BASE64Encoder()).encode(src);
	    String str = null;
	    try {
	        if (charset == null) {// UTF-8
	            str = Base64.encodeBase64String(src);
	        } else {
	            str = new String(Base64.encodeBase64(src), charset);
	        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	    return str;
	}
}

/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.fcc.commons.coder.Coder;
import com.fcc.commons.coder.CoderEnum;
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

    public static final String CHARSET = "UTF-8";
    /**
     * 数据解码，BASE64
     * @param data      解码数据
     * @return
     */
    public static byte[] decodeBase64(String data) { 
//        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
//        try { 
//            return new String(decoder.decodeBuffer(src), charset); 
//        } catch (Exception ex) { 
//            return null; 
//        } 
        return Base64.decodeBase64(data);
    }
    /**
     * 数据解码，DES
     * @param key       秘钥
     * @param data      解码数据
     * @param charset   字符集
     * @return
     */
    public static String decryptDES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return new String(Coder.decrypt(CoderEnum.DES, key, decodeBase64(data)), charset);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据解码，3DES
     * @param key       秘钥
     * @param data      解码数据
     * @param charset   字符集
     * @return
     */
    public static String decrypt3DES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return new String(Coder.decrypt(CoderEnum.DES3, key, decodeBase64(data)), charset);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据解码，AES
     * @param key       秘钥
     * @param data      解码数据
     * @param charset   字符集
     * @return
     */
    public static String decryptAES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return new String(Coder.decrypt(CoderEnum.AES, key, decodeBase64(data)), charset);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据解码（公钥、私钥）   RSA
     * @param key       秘钥
     * @param data      解码数据
     * @param charset   字符集
     * @return
     */
    public static String decryptRSA(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return new String(Coder.decrypt(CoderEnum.RSA, key, decodeBase64(data)), charset);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    
	/**
	 * 对字窜进新MD5摘要,并将摘要转化成16进制编码，大写
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
	 * 数据编码，BASE64
	 * @param data     解码数据
	 * @return
	 */
    public static String encodeBase64(byte[] data) {
//      return (new sun.misc.BASE64Encoder()).encode(src);
        return Base64.encodeBase64String(data);
    }
    
    /**
     * 数据编码，BASE64
     * @param data     解码数据
     * @return
     */
    public static String encodeBase64(String data, String charset) {
        if (charset == null || "".equals(charset)) charset = CHARSET;
        try {
            return Base64.encodeBase64String(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据编码，DES
     * @param key       秘钥
     * @param data      编码数据
     * @param charset   字符集
     * @return
     */
    public static String encryptDES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return encodeBase64(Coder.encrypt(CoderEnum.DES, key, data.getBytes(charset)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据编码，3DES
     * @param key       秘钥
     * @param data      编码数据
     * @param charset   字符集
     * @return
     */
    public static String encrypt3DES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return encodeBase64(Coder.encrypt(CoderEnum.DES3, key, data.getBytes(charset)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据编码，AES
     * @param key       秘钥
     * @param data      编码数据
     * @param charset   字符集
     * @return
     */
    public static String encryptAES(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return encodeBase64(Coder.encrypt(CoderEnum.AES, key, data.getBytes(charset)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 数据编码（公钥、私钥）   RSA
     * @param key       秘钥
     * @param data      编码数据
     * @param charset   字符集
     * @return
     */
    public static String encryptRSA(Key key, String data, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return encodeBase64(Coder.encrypt(CoderEnum.RSA, key, data.getBytes(charset)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 生成DES秘钥
     * @param strKey    指定字符生成秘钥    
     * @param charset   指定字符的编码
     * @return
     */
	public static Key getKeyDES(String strKey, String charset) {
	    try {
	        if (charset == null || "".equals(charset)) charset = CHARSET;
            return Coder.getKey(CoderEnum.DES, strKey.getBytes(charset));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	    return null;
	}
	/**
     * 生成3DES秘钥
     * @param strKey    指定字符生成秘钥    
     * @param charset   指定字符的编码
     * @return
     */
	public static Key getKey3DES(String strKey, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return Coder.getKey(CoderEnum.DES3, strKey.getBytes(charset));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
	/**
     * 生成AES秘钥
     * @param strKey    指定字符生成秘钥    
     * @param charset   指定字符的编码
     * @return
     */
	public static Key getKeyAES(String strKey, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return Coder.getKey(CoderEnum.AES, strKey.getBytes(charset));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
	/**
     * 生成RSA公钥、私钥
     * @param strKey    指定字符生成秘钥    
     * @param charset   指定字符的编码
     * @return
     */
	public static KeyPair getKeyRSA(String strKey, String charset) {
        try {
            if (charset == null || "".equals(charset)) charset = CHARSET;
            return Coder.getKeyPair(CoderEnum.RSA, strKey.getBytes(charset));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) throws Exception {
        String str = "fqm";
        System.out.println(DigestUtils.sha1Hex(str));
        
        String strKey = "fcc-db";
        Key key = getKeyDES(strKey, null);
        key = Coder.byteToKey(CoderEnum.DES, key.getEncoded());
        String encrypt = encryptDES(key, str, null);
        System.out.println(strKey);
        System.out.println(encrypt);
        System.out.println("value=" + decryptDES(key, encrypt, null));
        
        key = getKey3DES(strKey, null);
        encrypt = encrypt3DES(key, str, null);
        key = Coder.byteToKey(CoderEnum.DES3, key.getEncoded());
        System.out.println(strKey);
        System.out.println(encrypt);
        System.out.println("value=" + decrypt3DES(key, encrypt, null));
        
        key = getKeyAES(strKey, null);
        encrypt = encryptAES(key, str, null);
        key = Coder.byteToKey(CoderEnum.AES, key.getEncoded());
        System.out.println(strKey);
        System.out.println(encrypt);
        System.out.println("value=" + decryptAES(key, encrypt, null));
        
        KeyPair keyPair = getKeyRSA(str, null);
        System.out.println(encodeBase64(keyPair.getPublic().getEncoded()));
        System.out.println(encodeBase64(keyPair.getPrivate().getEncoded()));
        
        key = keyPair.getPublic();
        key = Coder.byteToPublicKey(CoderEnum.RSA, key.getEncoded());
        encrypt = encryptRSA(key, strKey, null);
        System.out.println(strKey);
        System.out.println(encrypt);
        key = keyPair.getPrivate();
        
        key = Coder.byteToPrivateKey(CoderEnum.RSA, key.getEncoded());
        System.out.println("value=" + decryptRSA(keyPair.getPrivate(), encrypt, null));
        
    }
}

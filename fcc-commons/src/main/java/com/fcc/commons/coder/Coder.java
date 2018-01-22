/*
 * @(#)Coder.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年12月20日
 * 修改历史 : 
 *     1. [2016年12月20日]创建文件 by 傅泉明
 */
package com.fcc.commons.coder;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

/**
 * 编码器
 * @version 
 * @author 傅泉明
 */
public class Coder {
    
    /**
     * 生成公钥和私钥对
     * @param coderEnum 类型
     * @param strKey    参数生成秘钥
     * @param charset   字符集
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static KeyPair getKeyPair(CoderEnum coderEnum, byte[] strKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // KeyPairGenerator类用于生成公钥和私钥对
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(coderEnum.getInfo());
        //生成一个密钥对，保存在keyPair中
        if (CoderEnum.RSA == coderEnum || CoderEnum.DSA == coderEnum) {
            kpg.initialize(512, new SecureRandom(strKey));
        }
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }
    /**
     * 生成密钥
     * @param coderEnum 类型
     * @param strKey    参数生成秘钥
     * @param charset   字符集
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static Key getKey(CoderEnum coderEnum, byte[] strKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator kg = KeyGenerator.getInstance(coderEnum.getInfo());
//        kg.init(new SecureRandom(strKey)); Window 
        
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");// linux 
        random.setSeed(strKey);
        kg.init(random);
        
        // 生成秘密密钥
        SecretKey secretKey = kg.generateKey();
        return secretKey;
    }
    /**
     * byte转化Key，对称加密，适用 DES,3DES(DESede),AES
     * @param coderEnum
     * @param bytes
     * @return
     */
    public static Key byteToKey(CoderEnum coderEnum, byte[] bytes) {
        SecretKey secretKey = null;  
        try {  
//            DESKeySpec dks = new DESKeySpec(bytes);  
//            SecretKeyFactory factory = SecretKeyFactory.getInstance(coderEnum.getInfo());  
//            secretKey = factory.generateSecret(dks);  
            // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码  
            secretKey = new SecretKeySpec(bytes, coderEnum.getInfo());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return secretKey; 
    }
    /**
     * byte转化Key，非对称加密，适用 RSA
     * @param coderEnum
     * @param bytes
     * @return
     */
    public static PublicKey byteToPublicKey(CoderEnum coderEnum, byte[] bytes) {
        try {
            if (CoderEnum.RSA == coderEnum) {
                RSAPublicKeyImpl sepc = new RSAPublicKeyImpl(bytes);
                return sepc;
            } else if (CoderEnum.DSA == coderEnum) {
                
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * byte转化Key，非对称加密，适用 RSA
     * @param coderEnum
     * @param bytes
     * @return
     */
    public static PrivateKey byteToPrivateKey(CoderEnum coderEnum, byte[] bytes) {
        try {
            if (CoderEnum.RSA == coderEnum) {
                return RSAPrivateCrtKeyImpl.newKey(bytes);
            } else if (CoderEnum.DSA == coderEnum) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 加密
     * @param coderEnum 类型
     * @param key       秘钥
     * @param data      加密的数据
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static byte[] encrypt(CoderEnum coderEnum, Key key, byte[] data) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        // 实例化
        Cipher cipher = Cipher.getInstance(coderEnum.getInfo());
        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }
    /**
     * 解密
     * @param coderEnum 类型
     * @param key       秘钥        
     * @param data      解密的数据
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static byte[] decrypt(CoderEnum coderEnum, Key key, byte[] data) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        // 实例化
        Cipher cipher = Cipher.getInstance(coderEnum.getInfo());
        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }
    
}
/*
 * @(#)MemoryUtil.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2018年3月14日
 * 修改历史 : 
 *     1. [2018年3月14日]创建文件 by 傅泉明
 */
package com.fcc.commons.utils;

/**
 * Memory判断
 * @version 
 * @author 傅泉明
 */
public class MemoryUtil {
    /**
     * 检查内存
     * @param scale 剩余内存和最大内存百分比
     * @return  true 调用gc
     */
    public static boolean checkMemory(float scale) {
        /** 是否出现堆内存不足 */
        boolean heapSpace = false;
//        //可使用内存
//        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        // 剩余内存
        long freeMemory = Runtime.getRuntime().freeMemory();
        // 最大可使用内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        // 可用内存百分比
        if (scale <= 0 || scale >= 1) scale = 0.1f;// gc 使用0.03
        if (((float)freeMemory / (float)maxMemory) < scale) {
            heapSpace = true;
            Runtime.getRuntime().gc();
        }
        return heapSpace;
    }
    
}

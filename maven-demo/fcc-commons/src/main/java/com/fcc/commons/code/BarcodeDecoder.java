/*
 * @(#)BarcodeDecoder.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月11日
 * 修改历史 : 
 *     1. [2016年10月11日]创建文件 by fqm
 */
package com.fcc.commons.code;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/**
 * 条形码解码
 * @version v1.0
 * @author 傅泉明
 */
public class BarcodeDecoder {
    
    /**
     * 解析读取条形码
     * @param barcodePath 条形码文件路径
     * @return 
     */
    public String decode(String barcodePath) {
        BufferedImage image;
        Result result = null;
        try {
            image = ImageIO.read(new File(barcodePath));
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                result = new MultiFormatReader().decode(bitmap, null);
            }
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

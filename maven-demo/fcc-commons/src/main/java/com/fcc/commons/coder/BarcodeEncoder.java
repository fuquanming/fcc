/*
 * @(#)BarcodeEncoder.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons
 * 创建日期 : 2016年10月11日
 * 修改历史 : 
 *     1. [2016年10月11日]创建文件 by fqm
 */
package com.fcc.commons.coder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 条形码编码
 * @version v1.0
 * @author 傅泉明
 */
public class BarcodeEncoder {
    
    /** 图片类型 jpg */
    private String fileType = "jpg";
    
    public BarcodeEncoder() {
    }
    
    public BarcodeEncoder(String fileType) {
        this.fileType = fileType;
    }
    
    /**
     * 生成条形码
     * @param contents 条形码内容
     * @param width 条形码宽度
     * @param height 条形码高度
     * @return 
     */
    public BufferedImage encode(String contents, int width, int height) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        BufferedImage barcode = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, height, null);
            barcode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barcode;
    }
    
    /**
     * 生成条形码
     * @param contents 条形码内容
     * @param width 条形码宽度
     * @param height 条形码高度
     * @return 
     */
    public boolean encode(String contents, int width, int height, OutputStream outputStream) {
        BufferedImage barcode = encode(contents, width, height);
        if (barcode != null) {
            try {
                ImageIO.write(barcode, fileType, outputStream);//把二维码写到response的输出流
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
        return false;
    }
    
}

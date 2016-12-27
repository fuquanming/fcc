package com.fcc.commons.coder;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 
 * <p>Description:二维码编码 使用google ZXing-code-3.3.0.jar</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class QRCodeEncoder {

    private String charset = "UTF-8";

    /** 字符颜色 黑色 */
    private int charColor = 0xFF000000;

    /** 背景颜色 白色 */
    private int backColor = 0xFFFFFFFF;

    /** 图片类型 jpg */
    private String fileType = "jpg";

    public QRCodeEncoder() {
    }

    /**
     * 
     * @param charset       字符编码
     * @param fileType      文件类型jpg、png
     * @param backColor     图片背景色
     * @param charColor     图片字符颜色
     */
    public QRCodeEncoder(String charset, String fileType, int backColor, int charColor) {
        this.charset = charset;
        this.fileType = fileType;
        this.charColor = charColor;
        this.backColor = backColor;
    }

    private BufferedImage toImage(BitMatrix bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bm.get(x, y) ? charColor : backColor);
            }
        }
        return image;
    }

    /**
     * 二维码编码
     * @param contents          编码的内容
     * @param width             图片的宽度
     * @param height            图片的高度
     * @return
     */
    public BufferedImage encode(String contents, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BufferedImage image = null;
        try {
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>(1);
            hints.put(EncodeHintType.CHARACTER_SET, charset);
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            image = toImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    /**
     * 二维码编码
     * @param contents          编码的内容
     * @param width             图片的宽度
     * @param height            图片的高度
     * @param outputStream
     * @return
     */
    public boolean encode(String contents, int width, int height, OutputStream outputStream) {
        BufferedImage image = null;
        try {
            image = encode(contents, width, height);
            ImageIO.write(image, fileType, outputStream); //把二维码写到response的输出流
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成带有logo标志的二维码
     * @param context 二维码存储内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logoPath  二维码logo路径
     * @return 
     */
    public BufferedImage encodeLogo(String context, int width, int height, String logoPath) {
        BufferedImage logoQRcode = null;
        try {
            // 20%的比例
            int imageWidth = (int)(width * 0.2);
            int imageHeight = (int)(height * 0.2);
            int imageHalfWidth = imageWidth / 2;
            int frameWidth = 2;
            // 读取Logo图像  
            BufferedImage logoImage = Thumbnails.of(logoPath).size(imageWidth, imageHeight).asBufferedImage();

            int[][] srcPixels = new int[imageWidth][imageHeight];
            for (int i = 0; i < logoImage.getWidth(); i++) {
                for (int j = 0; j < logoImage.getHeight(); j++) {
                    srcPixels[i][j] = logoImage.getRGB(i, j);
                }
            }

            Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
            hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(context, BarcodeFormat.QR_CODE, width, height, hint);

            // 二维矩阵转为一维像素数组  
            int halfW = bitMatrix.getWidth() / 2;
            int halfH = bitMatrix.getHeight() / 2;

            int[] pixels = new int[width * height];

            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    // 读取图片  
                    if (x > halfW - imageHalfWidth && x < halfW + imageHalfWidth && y > halfH - imageHalfWidth && y < halfH + imageHalfWidth) {
                        pixels[y * width + x] = srcPixels[x - halfW + imageHalfWidth][y - halfH + imageHalfWidth];
                    }
                    // 在图片四周形成边框  
                    else if ((x > halfW - imageHalfWidth - frameWidth && x < halfW - imageHalfWidth + frameWidth
                            && y > halfH - imageHalfWidth - frameWidth && y < halfH + imageHalfWidth + frameWidth)
                            || (x > halfW + imageHalfWidth - frameWidth && x < halfW + imageHalfWidth + frameWidth
                                    && y > halfH - imageHalfWidth - frameWidth && y < halfH + imageHalfWidth + frameWidth)
                            || (x > halfW - imageHalfWidth - frameWidth && x < halfW + imageHalfWidth + frameWidth
                                    && y > halfH - imageHalfWidth - frameWidth && y < halfH - imageHalfWidth + frameWidth)
                            || (x > halfW - imageHalfWidth - frameWidth && x < halfW + imageHalfWidth + frameWidth
                                    && y > halfH + imageHalfWidth - frameWidth && y < halfH + imageHalfWidth + frameWidth)) {
                        pixels[y * width + x] = 0xfffffff;
                    } else {
                        // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；  
                        pixels[y * width + x] = bitMatrix.get(x, y) ? charColor : backColor;
                    }
                }
            }
            logoQRcode = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            logoQRcode.getRaster().setDataElements(0, 0, width, height, pixels);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logoQRcode;
    }
    
    /**
     * 生成带有logo标志的二维码
     * @param context 二维码存储内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param logoPath  二维码logo路径
     * @return 
     */
    public boolean encodeLogo(String context, int width, int height, String logoPath, OutputStream outputStream) {
        BufferedImage logoQRcode = null;
        try {
            logoQRcode = encodeLogo(context, width, height, logoPath);
            ImageIO.write(logoQRcode, fileType, outputStream); //把二维码写到response的输出流
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCharset() {
        return charset;
    }

    public QRCodeEncoder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getCharColor() {
        return charColor;
    }

    public QRCodeEncoder setCharColor(int charColor) {
        this.charColor = charColor;
        return this;
    }

    public int getBackColor() {
        return backColor;
    }

    public QRCodeEncoder setBackColor(int backColor) {
        this.backColor = backColor;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public QRCodeEncoder setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

}

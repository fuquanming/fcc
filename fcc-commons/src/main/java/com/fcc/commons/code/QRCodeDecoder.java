package com.fcc.commons.code;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * 
 * <p>Description:二维码解码 使用google ZXing-code-2.2.jar</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class QRCodeDecoder {

    private String charset = "UTF-8";

    public QRCodeDecoder() {
    }

    public QRCodeDecoder(String charset) {
        this.charset = charset;
    }

    /**
     * 二维码解码
     * @param file	需要解析的文件
     * @return
     */
    public String decode(File file) {
        String msg = null;
        try {
            if (file.exists() == false) return null;
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader qrCodeReader = new QRCodeReader();
            Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>(1);
            hints.put(DecodeHintType.CHARACTER_SET, charset);
            msg = qrCodeReader.decode(bitmap, hints).getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    public static void main(String[] args) {
        String str = new QRCodeDecoder().decode(new File("F:\\work_temp\\1.jpg"));
        System.out.println(str);
    }
}

package com.fcc.commons.fcc_commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fcc.commons.code.BarcodeDecoder;
import com.fcc.commons.code.BarcodeEncoder;
import com.fcc.commons.code.QRCodeDecoder;
import com.fcc.commons.code.QRCodeEncoder;
import com.fcc.commons.utils.EncryptionUtil;

public class TestMain {

    public static String base64Encode(String src) {
        return (new sun.misc.BASE64Encoder()).encode(src.getBytes()); 
    }
    /**
     * BASE64 编码
     * @param src
     * @return
     */
    public static String base64Encode(String src, String charset) {
        String str = "";
        try {
            str = (new sun.misc.BASE64Encoder()).encode(src.getBytes(charset));
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return str;
    }
    /**
     * BASE64 编码
     * @param src
     * @return
     */
    public static String base64Encode(byte[] src) {
        return (new sun.misc.BASE64Encoder()).encode(src); 
    }
    
    /**
     * BASE64 解码. 
     * @param src
     * @return
     */
    public static String base64Decode(String src) { 
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
        try { 
            return new String(decoder.decodeBuffer(src)); 
        } catch (Exception ex) { 
            return null; 
        } 
    }
    
    /**
     * BASE64 解码. 
     * @param src
     * @return
     */
    public static String base64Decode(String src, String charset) { 
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
        try { 
            return new String(decoder.decodeBuffer(src), charset); 
        } catch (Exception ex) { 
            return null; 
        } 
    }
    /**
     * BASE64 解码(to byte[]). 
     * @param src
     * @return
     */
    public static byte[] base64DecodeToBytes(String src) { 
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
        try { 
            return decoder.decodeBuffer(src); 
        } catch (Exception ex) { 
            return null; 
        } 
    }
    
    @org.junit.Test
    public void testBase64Encode() throws Exception {
//        fail("Not yet implemented");
        String str = "123你好";
        String encode1 = base64Encode(str);
        System.out.println(encode1);
        System.out.println(base64Decode(encode1));
        
        Base64 base64 = new Base64();
        String val = null;
        val = new String(Base64.encodeBase64(str.getBytes("UTF-8")));
        System.out.println(val);
        System.out.println(new String(Base64.decodeBase64(encode1.getBytes())));
        System.out.println(encode1.equals(val));
        
        
        val = base64.encodeToString(str.getBytes());
        System.out.println(val);
        val = new String(base64.encode(str.getBytes("utf-8")));
        System.out.println(val);
        
    }
    
    @Test
    public void testMd5() {
        String str = "123你好";
        String val = DigestUtils.md5Hex(str);
        System.out.println(val);
        
        String val1 = EncryptionUtil.encodeMD5(str).toLowerCase();
        System.out.println(val1);
        System.out.println(val.equals(val1));
    }
    
    @Test
    public void testQRCodeEncode() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File("F:\\work_temp\\1.jpg"));
            new QRCodeEncoder().setBackColor(0xFFFFFFFF).setCharColor(0xFF0066FF)
//                        .encode("http://127.7.7.1水电费", 300, 300, fileOutputStream);
                    .encodeLogo("http://127.7.7.1水电费", 300, 300, "F:\\work_temp\\barcode-1.jpg", fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
    
    @Test
    public void testQRCodeDecoder() {
        System.out.println(new QRCodeDecoder().decode(new File("F:\\work_temp\\1.jpg")));
    }
    
    @Test
    public void testBarcodeEncoder() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File("F:\\work_temp\\barcode-1.jpg"));
            new BarcodeEncoder().encode("123", 300, 300, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
    
    @Test
    public void testBarcodeDecoder() {
        System.out.println(new BarcodeDecoder().decode("F:\\work_temp\\barcode-1.jpg"));
    }

}

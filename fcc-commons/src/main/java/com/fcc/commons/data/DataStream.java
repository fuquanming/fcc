/*
 * Copyright(2009-****) of FuQuanming Inc.. All rights reserved.
 * FuQuanming PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.fcc.commons.data;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;


/**
 * <p>Description:对数据类型 int，short，long 转化 ，参见DataOutputStream</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * <p>Company:</p>
 * @author 傅泉明
 * @version v1.0
 */
public class DataStream {
	/**
	 * 将字符转换为字节数组
	 * @param str
	 * @param charset
	 * @param size		字节总数，不足的右补0X00, 0 表示不补充
	 * @return
	 */
	public static byte[] readStr(String str, String charset, int size) {
		byte[] strB = null;
		if (charset != null) {
			try {
				strB = str.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			strB = str.getBytes();
		}
		if (size > 0) {
			int len = strB.length;
			if (size > len) {// 补充
				ByteBuffer bb = ByteBuffer.allocate(size);
				bb.put(strB);
				strB = bb.array();
			}
		}
		return strB;
	}

	/**
	 * 将一个 int 值以 4-byte 值形式输出，先写入高字节，参见 DataOutputStream
	 * @param v
	 * @return
	 */
	public static byte[] readInt(int v) {
		byte[] b = new byte[4];
		b[0] = (byte)((v >>> 24) & 0xFF);
		b[1] = (byte)((v >>> 16) & 0xFF);
		b[2] = (byte)((v >>>  8) & 0xFF);
		b[3] = (byte)((v >>>  0) & 0xFF);
		return b;
	}
	
	/**
	 * 将一个 short 值以 2-byte 值形式输出，先写入高字节，参见 DataOutputStream
	 * @param v
	 * @return
	 */
	public static byte[] readShort(int v) {
		byte[] b = new byte[2];
		b[0] = (byte)((v >>>  8) & 0xFF);
		b[1] = (byte)((v >>>  0) & 0xFF);
		return b;
	}
	
	/**
	 * 将一个 long 值以 8-byte 值形式输出，先写入高字节，参见 DataOutputStream
	 * @param v
	 * @return
	 */
	public static byte[] readLong(long v) {
		byte[] writeBuffer = new byte[8];
		writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
		return writeBuffer;
	}
	
	/**
	 * 此输入四个字节，将它们解释为一个 int 参考 DataInputStream 
	 * @param buf
	 * @return
	 */
	public static int buildInt(byte[] buf) {
		int ch1 = buf[0] & 0xff;
        int ch2 = buf[1] & 0xff;
        int ch3 = buf[2] & 0xff;
        int ch4 = buf[3] & 0xff;
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	/**
	 * 此输入2个字节，将它们解释为一个 有符号的 16 位数字 参考 DataInputStream 
	 * @param buf
	 * @return
	 */
	public static int buildShort(byte[] buf) {
		int ch1 = buf[0] & 0xff;
        int ch2 = buf[1] & 0xff;
        return (short)((ch1 << 8) + (ch2 << 0));
	}
	
	/**
	 * 此输入8个字节，将它们解释为一个 long 参考 DataInputStream 
	 * @param buf
	 * @return
	 */
	public static long buildLong(byte[] buf) {
		byte[] readBuffer = new byte[8];
		System.arraycopy(buf, 0, readBuffer, 0, 8);
		return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
	}
	
	/**
	 * 此输入4个字节，将它们解释为一个 int 通过DataInputStream 
	 * @param v
	 * @return
	 */
	public static int buildIntByInputStream(byte[] buf) {
		if (buf.length != 4) return 0;
		ByteArrayInputStream is = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(is);
		try {
			return dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(is);
		}
		return 0;
	}
	
	/**
	 * 此输入2个字节，将它们解释为一个 有符号的 16 位数字 通过DataInputStream 
	 * @param v
	 * @return
	 */
	public static int buildShortByInputStream(byte[] buf) {
		ByteArrayInputStream is = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(is);
		try {
			return dis.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(is);
		}
		return 0;
	}
	
	/**
	 * 此输入8个字节，将它们解释为一个 long 通过DataInputStream 
	 * @param v
	 * @return
	 */
	public static long buildLongByInputStream(byte[] buf) {
		ByteArrayInputStream is = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(is);
		try {
			return dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(is);
		}
		return 0;
	}
	
}

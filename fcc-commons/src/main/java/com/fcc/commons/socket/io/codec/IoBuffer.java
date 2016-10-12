package com.fcc.commons.socket.io.codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * <p>Description:获取输入、输出流</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface IoBuffer {
	/**
     * 获取读取缓存字节流
     */
    public ByteBuffer getByteBuffer();
    /**
     * 获取输入流
     * @return
     */
    public InputStream getInputStream();
    /**
     * 获取输出流
     * @return
     */
    public OutputStream getOutputStream();
	
}

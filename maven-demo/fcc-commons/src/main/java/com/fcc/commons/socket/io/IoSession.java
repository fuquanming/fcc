package com.fcc.commons.socket.io;

import java.net.SocketAddress;
import java.util.Set;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface IoSession {
	/**
	 * I/O 处理 事件
	 * @return
	 */
    IoHandler getHandler();
    /**
     * 取得读取缓冲字节大小
     * @return
     */
    int getReadBufferSize();
    /**
     * 设置读取缓存字节大小
     * @param readBufferSize
     */
    void setReadBufferSize(int readBufferSize);
    /**
     * 读取信息
     * @return
     */
    Object read();
    /**
     * 写信息
     * @param message
     * @return TODO
     */
    boolean write(Object message);
    /**
     * 返回本次会议的用户定义的属性值
     * @param key
     * @return
     */
    Object getAttribute(Object key);
    /**
     * 设置一个用户定义的属性
     * @param key
     * @param value
     * @return
     */
    Object setAttribute(Object key, Object value);
    /**
     * 删除用户定义的属性与指定键
     * @param key
     * @return
     */
    Object removeAttribute(Object key);
    /**
     * 返回 true 表示该会话中包含该属性
     * @param key
     * @return
     */
    boolean containsAttribute(Object key);
    /**
     * 返回的所有用户定义的属性的键的设置
     * @return
     */
    Set<Object> getAttributeKeys();
    /**
     * 返回的 true 表示该会话已经关闭
     * @return
     */
    boolean isClosing();
    /**
     * 返回远程对等套接字地址
     * @return
     */
    SocketAddress getRemoteAddress();
    /**
     * 返回本地机器上的套接字地址
     * @return
     */
    SocketAddress getLocalAddress();
    
}


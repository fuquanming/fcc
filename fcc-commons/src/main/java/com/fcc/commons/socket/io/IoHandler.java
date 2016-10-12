package com.fcc.commons.socket.io;

/**
 * <p>Description:处理所有的 I/O 事件</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface IoHandler {
	/**
	 * 当 I / O 处理器已经创建了一个新的连接时调用
	 * @param session
	 * @throws Exception
	 */
    void sessionCreated(IoSession session) throws Exception;
    /**
     * 当 打开 I/O 数据流时调用
     * @param session
     * @throws Exception
     */
    void sessionOpened(IoSession session) throws Exception;
    /**
     * 当连接关闭时调用
     * @param session
     * @throws Exception
     */
    void sessionClosed(IoSession session) throws Exception;
    /**
     * 连接变为空闲时调用
     * @param session
     * @param status
     * @throws Exception
     */
    void sessionIdle(IoSession session, IdleStatus status) throws Exception;
    /**
     * 用户抛出任何异常时调用的
     * @param session
     * @param cause
     * @throws Exception
     */
    void exceptionCaught(IoSession session, Throwable cause) throws Exception;
    /**
     * 当消息收到时调用
     * @param session
     * @param message 接收到数据
     * @throws Exception
     */
    void messageReceived(IoSession session, Object message) throws Exception;
    /**
     * 当消息发送到时调用 {@link IoSession#write(Object)}
     * @param session
     * @param message
     * @throws Exception
     */
    void messageSent(IoSession session, Object message) throws Exception;
    /**
     * 当消息发送失败时调用 {@link IoSession#write(Object)}
     * @param session
     * @param message
     * @throws Exception
     */
    void messageSentFail(IoSession session, Object message) throws Exception;
}

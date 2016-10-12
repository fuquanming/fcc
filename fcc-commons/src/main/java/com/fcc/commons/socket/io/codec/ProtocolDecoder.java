package com.fcc.commons.socket.io.codec;


import com.fcc.commons.socket.io.IoSession;
/**
 * <p>Description:解码二进制或协议的具体数据到更高级别的消息对象</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProtocolDecoder {
	/**
	 * 解码二进制或协议的具体内容到更高级别的消息对象
	 * @param session	
	 * @param io		读取到的缓存字节，通道
	 * @param out		
	 * @throws Exception
	 */
    void decode(IoSession session, IoBuffer io, ProtocolDecoderOutput out)
            throws Exception;
}

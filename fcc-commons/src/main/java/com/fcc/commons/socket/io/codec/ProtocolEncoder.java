package com.fcc.commons.socket.io.codec;

import com.fcc.commons.socket.io.IoSession;

/**
 * <p>Description:编码二进制或协议的具体数据到更高级别的消息对象</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProtocolEncoder {
	/**
	 * 编码二进制或协议的具体数据到更高级别的消息对象
	 * @param session
	 * @param message	发送的数据
	 * @param out
	 * @throws Exception
	 */
	void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception;

}

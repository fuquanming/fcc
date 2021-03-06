package com.fcc.commons.socket.io.codec;

/**
 * <p>Description:回调{@ link ProtocolDecoder}生成解码消息</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public interface ProtocolDecoderOutput {
	/**
     * 回调的{@ link ProtocolDecoder}生成解码消息
     * @param message the decoded message
     */
    void write(Object message);
}

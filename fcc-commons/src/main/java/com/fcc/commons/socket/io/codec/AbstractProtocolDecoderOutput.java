package com.fcc.commons.socket.io.codec;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>Description:适配器 回调{@ link ProtocolDecoder}生成解码消息 </p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public abstract class AbstractProtocolDecoderOutput implements
		ProtocolDecoderOutput {
	private final Queue<Object> messageQueue = new ConcurrentLinkedQueue<Object>();

	public Queue<Object> getMessageQueue() {
		return messageQueue;
	}

	public void write(Object message) {
		messageQueue.offer(message);
	}

}

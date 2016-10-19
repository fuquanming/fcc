package com.fcc.web.sys.cache;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class QueueUtil {
	
	private static Queue<Object> queue = new ConcurrentLinkedQueue<Object>();

	public static Queue<Object> getQueue() {
		return queue;
	}

}

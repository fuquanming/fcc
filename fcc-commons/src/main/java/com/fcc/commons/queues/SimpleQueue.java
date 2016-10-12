package com.fcc.commons.queues;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
/**
 * <p>Description:提供单机队列，存储在本机内存</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class SimpleQueue implements Queue {
	
	private Map<String, java.util.Queue<Object>> queueMap = new HashMap<String, java.util.Queue<Object>>();
	
	private ReentrantLock lock = new ReentrantLock();
	
	public boolean offer(String key, Object value) {
	    lock.lock();
	    try {
    	    java.util.Queue<Object> queue = queueMap.get(key);
    		if (queue == null) {
    			queue = new ConcurrentLinkedQueue<Object>();
    			queueMap.put(key, queue);
    		}
    		return queueMap.get(key).offer(value);
	    } finally {
            lock.unlock();
        }
	}
	
	public Object poll(String key) {
		java.util.Queue<Object> queue = queueMap.get(key);
		if (queue == null) return null;
		return queue.poll();
	}
	
	public Object peek(String key) {
	    java.util.Queue<Object> queue = queueMap.get(key);
	    if (queue == null) return null;
		return queue.peek();
	}
	
	public long size(String key) {
	    java.util.Queue<Object> queue = queueMap.get(key);
        if (queue == null) return 0;
		return queue.size();
	}

	public Iterator<Object> iterator(String key) {
	    java.util.Queue<Object> queue = queueMap.get(key);
        if (queue == null) return null;
		return queue.iterator();
	}
	
}

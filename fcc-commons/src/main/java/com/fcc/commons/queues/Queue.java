package com.fcc.commons.queues;

import java.util.Iterator;

/**
 * <p>Description:队列</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @param <E>
 */
public interface Queue {
	/**
	 * 如果可能，将指定的元素插入此队列
	 * @param obj
	 * @return
	 */
	public abstract boolean offer(String key, Object value);
	/**
	 * 检索并移除此队列的头，如果队列为空，返回null
	 * @return
	 */
	public abstract Object poll(String key);
	/**
	 * 检索，但是不移除此队列的头，如果此队列为空，则返回 null
	 * @return
	 */
	public abstract Object peek(String key);
	/**
	 * 返回此队列中的元素数量
	 * @return
	 */
	public abstract long size(String key);
	/**
	 * 返回在此队列元素上进行迭代的迭代器
	 * @return
	 */
	public abstract Iterator<Object> iterator(String key);
	
}

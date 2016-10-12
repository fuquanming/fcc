package com.fcc.commons.socket.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public abstract class IoSessionAbstract implements IoSession {
	
	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();
	private int readBufferSize = 100;
	
	public boolean containsAttribute(Object key) {
		return attributeMap.containsKey(key);
	}

	public Object getAttribute(Object key) {
		return attributeMap.get(key);
	}

	public Set<Object> getAttributeKeys() {
		return attributeMap.keySet();
	}

	public Object removeAttribute(Object key) {
		return attributeMap.remove(key);
	}
	
	public void removeAllAttribute() {
		attributeMap.clear();
	}

	public Object setAttribute(Object key, Object value) {
		return attributeMap.put(key, value);
	}
	
	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}
	
	public int getReadBufferSize() {
		return this.readBufferSize;
	}

}

package com.fcc.commons.workflow.common;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description:工作流-业务KEY</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class WorkflowBusinessKey {
	
	private static Map<String, String> BUSINESS_KEY_MAP = new HashMap<String, String>();
	
	public static void setBusinessKey(String key, String value) {
		BUSINESS_KEY_MAP.put(key, value);
	}
	
	public static String getBusinessKey(String key) {
		return BUSINESS_KEY_MAP.get(key);
	}
	
}

package com.fcc.commons.workflow.common;
/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public enum WorkflowStatus {
	
	unstart("草稿"), start("待审核"), success("成功"), fail("失败"), cannel("取消");
	
	private String value;

	private WorkflowStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}

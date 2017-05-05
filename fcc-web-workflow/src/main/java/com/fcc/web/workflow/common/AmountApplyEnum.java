package com.fcc.web.workflow.common;
/**
 * 额度申请
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public enum AmountApplyEnum {
	/** 申请额度 */
	amount("amount"),
	/** 限制额度 */
	standard("standard"),
	/** 随机分配风控人员 */
	randomMember("randomMember"),
	/** 风控人员设置金额 */
	memberAmountList("memberAmountList");
	
	private String value;

	private AmountApplyEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}

package com.fcc.commons.workflow.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description:任务连接线</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ProcessTaskSequenceFlowInfo {
	
	/** 连接线名称 */
	private String name;
	/** 条件信息 */
	private String conditionText;
	/** 条件KEY */
	private String conditionKey;
	/** 条件VALUE */
	private String conditionValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConditionText() {
		return conditionText;
	}
	public void setConditionText(String conditionText) {
		this.conditionText = conditionText;
	}
	public String getConditionKey() {
		return conditionKey;
	}
	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	
	/**
	 * 解析条件
	 * @param conditionText
	 * @return
	 */
	public String[] analyzeConditionText(String conditionText) {
		Pattern patternConditionStr = null;
		if (conditionText.contains("'")) {
			patternConditionStr = Pattern.compile("\\$\\{(.*)==.*'(.*)'.*\\}", Pattern.CASE_INSENSITIVE);
		} else {
			patternConditionStr = Pattern.compile("\\$\\{(.*)==(.*)\\}", Pattern.CASE_INSENSITIVE);
		}
		Matcher matcher = patternConditionStr.matcher(conditionText);
		if (matcher.find()) {
			String key = matcher.group(1).trim();
			String value = matcher.group(2).trim();
			return new String[]{key, value};
		}
		return null;
	}
}

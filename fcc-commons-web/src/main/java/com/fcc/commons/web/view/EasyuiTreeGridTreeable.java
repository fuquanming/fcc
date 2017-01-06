package com.fcc.commons.web.view;

/**
 * <p>Description:easyui使用的treeGrid模型，树形结构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class EasyuiTreeGridTreeable extends EasyuiTreeNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6804600865764178730L;

	private String nodeCode;
	private Integer nodeLevel;
	private Integer nodeSort;
	private Boolean nodeStatus;
	
}

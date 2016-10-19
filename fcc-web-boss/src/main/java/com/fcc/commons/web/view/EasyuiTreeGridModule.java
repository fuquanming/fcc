package com.fcc.commons.web.view;

/**
 * <p>Description:easyui使用的treeGrid模型，模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class EasyuiTreeGridModule extends EasyuiTreeNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6804600865764178730L;

	private String moduleDesc;
	private Integer moduleSort;
	private String operateIds;
	private String operateNames;
	public String getModuleDesc() {
		return moduleDesc;
	}
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
	public Integer getModuleSort() {
		return moduleSort;
	}
	public void setModuleSort(Integer moduleSort) {
		this.moduleSort = moduleSort;
	}
	public String getOperateIds() {
		return operateIds;
	}
	public void setOperateIds(String operateIds) {
		this.operateIds = operateIds;
	}
	public String getOperateNames() {
		return operateNames;
	}
	public void setOperateNames(String operateNames) {
		this.operateNames = operateNames;
	}
	
}

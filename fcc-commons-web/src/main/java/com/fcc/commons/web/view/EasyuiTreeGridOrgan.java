package com.fcc.commons.web.view;

/**
 * <p>Description:easyui使用的treeGrid模型，组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class EasyuiTreeGridOrgan extends EasyuiTreeNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6804600865764178730L;

	private String organDesc;
	private Integer organSort;
	private Integer organLevel;
	public String getOrganDesc() {
		return organDesc;
	}
	public void setOrganDesc(String organDesc) {
		this.organDesc = organDesc;
	}
	public Integer getOrganSort() {
		return organSort;
	}
	public void setOrganSort(Integer organSort) {
		this.organSort = organSort;
	}
	public Integer getOrganLevel() {
		return organLevel;
	}
	public void setOrganLevel(Integer organLevel) {
		this.organLevel = organLevel;
	}
	
}

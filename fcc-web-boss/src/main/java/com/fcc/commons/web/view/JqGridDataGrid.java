package com.fcc.commons.web.view;
/**
 * <p>Description:jqGrid的datagrid向后台传递参数使用的model</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class JqGridDataGrid implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6123957834774972268L;

	private int page = 1;// 当前页
	private int rows = 10;// 每页显示记录数
	private String sidx = null;// 排序字段名
	private String sord = "asc";// 按什么排序(asc,desc)
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	
}

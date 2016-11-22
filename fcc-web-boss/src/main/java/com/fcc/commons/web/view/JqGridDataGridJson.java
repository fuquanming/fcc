package com.fcc.commons.web.view;

import java.util.List;

/**
 * <p>Description:后台向前台返回JSON，用于jqGrid的datagrid</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class JqGridDataGridJson implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3859118647387392258L;
	private Long totalPage;// 总记录数
	private List rows;// 每行记录
	private String msg = "";// 提示信息
	private List footer;

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List getFooter() {
		return footer;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

}

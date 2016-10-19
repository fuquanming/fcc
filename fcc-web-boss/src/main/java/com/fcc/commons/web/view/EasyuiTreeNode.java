package com.fcc.commons.web.view;

import java.util.List;
import java.util.Map;
/**
 * <p>Description:easyui使用的tree模型</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class EasyuiTreeNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4231687117854049863L;
	private String id;
	private String text;// 树节点名称
	private String iconCls;// 前面的小图标样式
	private Boolean checked = false;// 是否勾选状态
	private Map<String, Object> attributes;// 其他参数
	private List<EasyuiTreeNode> children;// 子节点
	private String state = "open";// 是否展开(open,closed)
	
	// 非必需
	private String msg = "";// 提示信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<EasyuiTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EasyuiTreeNode> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

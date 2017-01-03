package com.fcc.commons.web.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description:报表信息</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class ReportInfo {
	
	private Long count;			// 总数
	private String name;		// 名称
	private String groupName;
	public ReportInfo(Long count, String name, String groupName) {
		super();
		this.count = count;
		this.name = name;
		this.groupName = groupName;
	}
	public ReportInfo(Long count, String name, Date groupName) {
		super();
		this.count = count;
		this.name = name;
		if (groupName != null) this.groupName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(groupName);;
	}
	public ReportInfo() {
		super();
	}
	public ReportInfo(Long count, String groupName) {
		super();
		this.count = count;
		this.groupName = groupName;
	}
	public ReportInfo(Long count, Integer groupName) {
		super();
		this.count = count;
		if (groupName != null) this.groupName = groupName.toString();
	}
	public ReportInfo(Long count, Long groupName) {
		super();
		this.count = count;
		if (groupName != null) this.groupName = groupName.toString();
	}
	public ReportInfo(Long count, BigDecimal groupName) {
		super();
		this.count = count;
		if (groupName != null) this.groupName = groupName.toString();
	}
	public ReportInfo(Long count, Date groupName) {
		super();
		this.count = count;
		if (groupName != null) this.groupName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(groupName);
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}

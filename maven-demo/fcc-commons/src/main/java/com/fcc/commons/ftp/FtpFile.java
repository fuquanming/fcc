package com.fcc.commons.ftp;

import java.util.Date;

/**
 * <p>Description:FTP客户端-文件类型</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class FtpFile {
	public static String PROPERTY_DIRECTORY = "drw-rw-rw-";
	public static String PROPERTY_FILE = "-rw-rw-rw-";
	/** 显示的文件名 */
	private String fileName;
	/** 文件大小 字节 */
	private long fileSize = 0;
	/** 更新时间 */
	private Date modifyDate;
	
	private String modifyDateStr;
	/** 属性 ： drw-rw-rw- ，-rw-rw-rw- */
	private String property = PROPERTY_FILE;
	/** 是否是目录 */
	public boolean isDirectory() {
		return property.contains("d");
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getModifyDateStr() {
		return modifyDateStr;
	}
	public void setModifyDateStr(String modifyDateStr) {
		this.modifyDateStr = modifyDateStr;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("fileName=").append(fileName)
		.append(",fileSize=").append(fileSize)
		.append(",modifyDate=").append(modifyDate)
		.append(",property=").append(property);
		return sb.toString();
	}
	
}

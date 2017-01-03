package com.fcc.commons.web.view;

/**
 * <p>Description:easyui使用的treeGrid模型，模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class EasyuiTreeGridFile extends EasyuiTreeNode implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6804600865764178730L;

	private String filePath;
	private String fileType;
	private String fileName;
	private Long fileSize;
	private Long modifyTime;
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}

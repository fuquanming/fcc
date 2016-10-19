package com.fcc.commons.web.view;

import java.io.Serializable;

/**
 * <p>Description:导出返回数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class ExportMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean empty = false;// 是否空数据
	private Integer currentSize = 0;// 当前导出数据总数
	private String fileName;// 下载的文件名
	private boolean destroy = false;// 系统是否停止
	private boolean error = false;// 统计数据时候是否出现异常
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public Integer getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public boolean isDestroy() {
		return destroy;
	}
	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
	
}

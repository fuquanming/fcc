package com.fcc.commons.web.view;

import java.io.Serializable;

/**
 * <p>Description:导入返回数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class ImportMessage extends Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean uploadFlag = false;// 是否上次成功
	private Integer currentSize = 0;// 当前导入数据总数
	private boolean importFlag;// 是否导入完成
	private boolean destroy;// 是否系统停止
	public boolean isUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(boolean uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public Integer getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}
	public boolean isImportFlag() {
		return importFlag;
	}
	public void setImportFlag(boolean importFlag) {
		this.importFlag = importFlag;
	}
	public boolean isDestroy() {
		return destroy;
	}
	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
	
}

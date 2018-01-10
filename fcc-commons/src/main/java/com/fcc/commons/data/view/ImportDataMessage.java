package com.fcc.commons.data.view;

import java.io.Serializable;

/**
 * <p>Description:导入返回数据</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class ImportDataMessage extends Message implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 文件是否正常 */
	private boolean fileFlag = false;// 
	/** 当前导入数据总数 */
	private int currentSize = 0;// 
	/** 是否导入完成 */
	private boolean importFlag;// 
	/** 是否系统停止 */
	private boolean destroy;// 
	public boolean isFileFlag() {
		return fileFlag;
	}
	public void setFileFlag(boolean fileFlag) {
		this.fileFlag = fileFlag;
	}
	public int getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(int currentSize) {
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

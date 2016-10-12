package com.fcc.commons.http;
/**
 * <p>Description: 记录文件名，文件字节</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class FileByte {

	private String fileName;
	
	private byte[] fileByte;
	/** 内容编码 base64,quoted-printable,(8bit,binary 二进制文件)  */
	private String encoding;
	/** 字符编码 utf-8, gbk */
	private String charset;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileByte() {
		return fileByte;
	}

	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}

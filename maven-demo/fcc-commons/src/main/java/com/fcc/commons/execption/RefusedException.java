package com.fcc.commons.execption;

/**
 * 用户非法操作
 * @author fuquanming
 *
 * Jul 21, 2009
 */
public class RefusedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefusedException() {
		super("拒绝操作");
	}
	
	public RefusedException(String message) {
		super(message);
	}
}

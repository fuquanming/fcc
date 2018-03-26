package com.fcc.commons.execption;

/**
 * 用户非法操作
 * @author fuquanming
 *
 * Jul 21, 2009
 */
public class RefusedException extends Exception {

    private static final long serialVersionUID = 1L;
	/**
	 * 异常信息
	 */
	private String msg;
	/**
	 * 异常代码
	 */
	private String code;
	
    public RefusedException() {
		super("拒绝操作");
	}
	
	public RefusedException(String message) {
		super(message);
		this.code = message;
		this.msg = message;
	}
	
	public RefusedException(String code, String msgFormat) {
        super(msgFormat);
        this.code = code;
        this.msg = msgFormat;
    }
	
	public RefusedException(String code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }
	
	public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
    
    /**
     * 移除父类同步方法
     * @see java.lang.Throwable#fillInStackTrace()
     *
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}

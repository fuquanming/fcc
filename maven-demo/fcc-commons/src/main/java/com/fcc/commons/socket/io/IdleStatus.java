package com.fcc.commons.socket.io;
/**
 * 表示空闲的类型
 * @author 傅泉明
 *
 * Dec 31, 2011
 */
public class IdleStatus {
	/**
	 * 当前没有读数据
	 */
    public static final IdleStatus READER_IDLE = new IdleStatus("reader idle");
    /**
     * 当前没有写数据
     */
    public static final IdleStatus WRITER_IDLE = new IdleStatus("writer idle");
    /**
     * 没有读、写数据
     */
    public static final IdleStatus BOTH_IDLE = new IdleStatus("both idle");

    private final String strValue;

    private IdleStatus(String strValue) {
        this.strValue = strValue;
    }

    /**
     * 返回此状态的字符串表示形式
     * <ul>
     *   <li>{@link #READER_IDLE} - <tt>"reader idle"</tt></li>
     *   <li>{@link #WRITER_IDLE} - <tt>"writer idle"</tt></li>
     *   <li>{@link #BOTH_IDLE} - <tt>"both idle"</tt></li>
     * </ul>
     */
    @Override
    public String toString() {
        return strValue;
    }
}
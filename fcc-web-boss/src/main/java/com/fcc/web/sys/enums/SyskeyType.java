package com.fcc.web.sys.enums;
/**
 * 系统用户KEY类型
 * 
 * @version 
 * @author 傅泉明
 */
public enum SyskeyType {
    /** 用户密码 */
    userPassword("用户密码");

    private final String info;

    private SyskeyType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    
}

package com.fcc.web.sys.enums;
/**
 * 系统字典KEY类型
 * 
 * @version 
 * @author 傅泉明
 */
public enum SysDictType {
    /** 系统 */
    sys("系统"),
    /** 用户密码 */
    userPassword("用户密码"),
    /** 用户错误登录次数 */
    userLoginCount("用户错误登录次数");
    
    
    private final String info;

    private SysDictType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public static void main(String[] args) {
        for (SysDictType type : SysDictType.values()) {
            System.out.println(type);
        }
    }
}

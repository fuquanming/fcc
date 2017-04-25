package com.fcc.web.sys.enums;

public enum UserStatus {
    /** 未激活 */
    inactive("未激活"),
    /** 未激活 */
    normal("正常状态"), 
    /** 未激活 */
    locked("锁定状态"), 
    /** 未激活 */
    off("注销");

    private final String info;

    private UserStatus(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    
    public static void main(String[] args) {
        System.out.println(UserStatus.normal.name().equals("normal"));
    }
}

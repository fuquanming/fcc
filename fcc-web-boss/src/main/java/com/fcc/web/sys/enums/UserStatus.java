package com.fcc.web.sys.enums;

import org.apache.log4j.chainsaw.Main;

public enum UserStatus {

    inactive("未激活"), normal("正常状态"), locked("锁定状态"), off("注销");

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

/*
 * @(#)BaseModel.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年10月13日
 * 修改历史 : 
 *     1. [2016年10月13日]创建文件 by fqm
 */
package com.fcc.commons.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 基础实体类，包含各实体公用属性 .
 * @version v1.0
 * @author 傅泉明
 */
@MappedSuperclass
public class BaseModel {
    /** 创建者 */
    private String createUser;
    /** 创建时间 */
    private Date createTime;
    /** 修改者 */
    private String updateUser;
    /** 修改时间 */
    private Date updateTime;
    
    @Column(name = "CREATE_USER")
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_USER")
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
}

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

/**
 * 基础实体类，包含各实体公用属性 .
 * @version v1.0
 * @author 傅泉明
 */
public class BaseModel {

    private String id;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
}

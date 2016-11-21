/*
 * @(#)Treeable.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年11月12日
 * 修改历史 : 
 *     1. [2016年11月12日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.model;

import org.springframework.util.StringUtils;

/**
 * tree结构
 * @version 
 * @author 傅泉明
 */
public abstract class Treeable {
    
    /** 父ID */
    protected String parentId;
    /** 所有父路径 如1-2-3 */
    protected String parentIds;
    /** 父路径分隔符 */
    private String separator = "-";
    /**
     * 
     * @param parentIds     父节点ID集合
     * @param id            自己的ID
     * @return
     */
    public String buildParendIds(Treeable parent, String id) {
        String parent_parentIds = parent.getParentIds();
        if (StringUtils.isEmpty(parent_parentIds)) {
            parent_parentIds = id;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(parent_parentIds).append(parent.getSeparator()).append(id);
            parent_parentIds = sb.toString();
        }
        return parent_parentIds;
    }
    
//    /**
//     * 获取父机构ID编号
//     * @param organId 子机构ID
//     * @return
//     */
//    public String getParentTreeId(){
//        String parentTreeId = "";
//        int lastSplitPos = parentIds.lastIndexOf(separator);
//        if (lastSplitPos > 0) {
//            parentTreeId = parentIds.substring(0, lastSplitPos);
//        } else {
//            parentTreeId = "ROOT";
//        }
//        return parentTreeId;
//    }
    
    public String getSeparator() {
        return separator;
    }
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    public String getParentId() {
        return parentId;
    };
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    
}

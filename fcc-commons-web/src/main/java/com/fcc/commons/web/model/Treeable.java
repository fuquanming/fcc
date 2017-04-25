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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import com.fcc.commons.core.model.BaseModel;

/**
 * tree结构
 * @version 
 * @author 傅泉明
 */
@MappedSuperclass
public class Treeable extends BaseModel implements Comparable<Treeable> {
    public static final Treeable ROOT = new Treeable();
    static {
        ROOT.setNodeId("ROOT");
        ROOT.setNodeLevel(0);
        ROOT.setNodeSort(0);
    }
    /** 主键 */
    private String nodeId;
    /** 节点名称 */
    private String nodeName;
    /** 节点编码 */
    private String nodeCode;
    /** 节点等级 */
    private int nodeLevel;
    /** 节点排序 */
    private int nodeSort;
    /** 节点描述 */
    private String nodeDesc;
    /** 节点状态：true启用，false不启用 */
    private boolean nodeStatus = Boolean.TRUE;
    /** 父ID */
    private String parentId;
    /** 所有父路径 如1-2-3 */
    private String parentIds;
    /** 非数据库字段,父路径分隔符 */
    private String separator = "-";
    /** 非数据库字段,多少个子节点 */
    private int childSize = 0;

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
    @Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "assigned")
    @Column(name = "NODE_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 6)
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    @Column(name = "NODE_NAME")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    @Column(name = "NODE_CODE")
    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }
    @Column(name = "NODE_LEVEL")
    public int getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(int nodeLevel) {
        this.nodeLevel = nodeLevel;
    }
    @Column(name = "NODE_SORT")
    public int getNodeSort() {
        return nodeSort;
    }

    public void setNodeSort(int nodeSort) {
        this.nodeSort = nodeSort;
    }
    @Column(name = "NODE_DESC")
    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }
    @Column(name = "NODE_STATUS")
    public boolean getNodeStatus() {
        return nodeStatus;
    }
    public void setNodeStatus(boolean nodeStatus) {
        this.nodeStatus = nodeStatus;
    }
    
    @Transient
    public String getSeparator() {
        return separator;
    }
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    @Transient
    public int getChildSize() {
        return childSize;
    }
    public void setChildSize(int childSize) {
        this.childSize = childSize;
    }
    
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    };
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "PARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("NodeId",getNodeId())
            .append("NodeName",getNodeName())
            .append("NodeCode",getNodeCode())
            .append("NodeLevel",getNodeLevel())
            .append("NodeSort",getNodeSort())
            .append("NodeDesc",getNodeDesc())
            .append("NodeStatus",getNodeStatus())
            .append("ParentId",getParentId())
            .append("ParentIds",getParentIds())
            .toString();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getNodeId())
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof Treeable == false) return false;
        if(this == obj) return true;
        Treeable other = (Treeable)obj;
        return new EqualsBuilder()
            .append(getNodeId(),other.getNodeId())
            .isEquals();
    }
    
    public int compareTo(Treeable o) {
        if(equals(o)){
            return 0;
        }
        int nodeLevel = o.getNodeLevel();
        int nodeSort = o.getNodeSort();
        if (this.nodeLevel > nodeLevel) {
            return 1;
        } else if (this.nodeLevel < nodeLevel) {
            return -1;
        } else if (this.nodeSort > nodeSort){
            return 1;
        } else if (this.nodeSort < nodeSort){
            return -1;
        } else {
            return this.nodeId.compareTo(o.getNodeId());
        }
    }
}

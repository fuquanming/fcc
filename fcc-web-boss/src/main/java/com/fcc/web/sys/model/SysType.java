package com.fcc.web.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_type")
public class SysType implements Comparable<SysType>, java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	public static final SysType ROOT = new SysType();
	static{
		ROOT.setTypeId("SYSTYPE_ROOT");
		ROOT.setTypeCode("");
		ROOT.setTypeDesc("");
		ROOT.setTypeLevel(0);
		ROOT.setTypeName("数据类型");
		ROOT.setTypeSort(0);
	}
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 主键       db_column: TYPE_ID 
     */ 	
	private java.lang.String typeId;
    /**
     * 名称       db_column: TYPE_NAME 
     */ 	
	private java.lang.String typeName;
    /**
     * 代码       db_column: TYPE_CODE 
     */ 	
	private java.lang.String typeCode;
    /**
     * 级别       db_column: TYPE_LEVEL 
     */ 	
	private java.lang.Integer typeLevel;
    /**
     * 排序       db_column: TYPE_SORT 
     */ 	
	private java.lang.Integer typeSort;
    /**
     * 描述       db_column: TYPE_DESC 
     */ 	
	private java.lang.String typeDesc;
	//columns END


	public SysType(){
	}

	public SysType(
		java.lang.String typeId
	){
		this.typeId = typeId;
	}

	

	public void setTypeId(java.lang.String value) {
		this.typeId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "TYPE_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getTypeId() {
		return this.typeId;
	}
	
			
	@Column(name = "TYPE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getTypeName() {
		return this.typeName;
	}
	
	public void setTypeName(java.lang.String value) {
		this.typeName = value;
	}
	
			
	@Column(name = "TYPE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getTypeCode() {
		return this.typeCode;
	}
	
	public void setTypeCode(java.lang.String value) {
		this.typeCode = value;
	}
	
			
	@Column(name = "TYPE_LEVEL", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getTypeLevel() {
		return this.typeLevel;
	}
	
	public void setTypeLevel(java.lang.Integer value) {
		this.typeLevel = value;
	}
	
			
	@Column(name = "TYPE_SORT", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getTypeSort() {
		return this.typeSort;
	}
	
	public void setTypeSort(java.lang.Integer value) {
		this.typeSort = value;
	}
	
			
	@Column(name = "TYPE_DESC", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getTypeDesc() {
		return this.typeDesc;
	}
	
	public void setTypeDesc(java.lang.String value) {
		this.typeDesc = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("TypeId",getTypeId())
			.append("TypeName",getTypeName())
			.append("TypeCode",getTypeCode())
			.append("TypeLevel",getTypeLevel())
			.append("TypeSort",getTypeSort())
			.append("TypeDesc",getTypeDesc())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getTypeId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SysType == false) return false;
		if(this == obj) return true;
		SysType other = (SysType)obj;
		return new EqualsBuilder()
			.append(getTypeId(),other.getTypeId())
			.isEquals();
	}
	
	public int compareTo(SysType o) {
		if(equals(o)){
			return 0;
		}
		Integer tempLevel = o.getTypeLevel();
		Integer tempSort = o.getTypeSort();
		if (this.typeLevel > tempLevel) {
			return 1;
		} else if (this.typeLevel < tempLevel) {
			return -1;
		} else if (this.typeSort > tempSort){
			return 1;
		} else if (this.typeSort < tempSort){
			return -1;
		} else {
			return this.typeId.compareTo(o.getTypeId());
		}
	}
	
	/**
	 * 获取父ID编号
	 * @param id 子ID
	 * @return
	 */
	@Transient
	public static String getParentId(String id){
		String parentId = "";
		int lastSplitPos = id.lastIndexOf('-');
		if (lastSplitPos > 0) {
			parentId = id.substring(0,lastSplitPos);
		} else {
			parentId = SysType.ROOT.getTypeId();
		}
		return parentId;
	}
	
	/**
	 * 获取父节点ID编号
	 */
	@Transient
	public String getParentId(){
		return SysType.getParentId(this.typeId);
	}
}


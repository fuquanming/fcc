package com.fcc.web.sys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>Description:系统操作</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_rbac_operate")
public class Operate implements Comparable<Operate>, Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * operateId       db_column: OPERATE_ID 
     */ 	
	private java.lang.String operateId;
    /**
     * operateName       db_column: OPERATE_NAME 
     */ 	
	private java.lang.String operateName;
    /**
     * operateValue       db_column: OPERATE_VALUE 
     */ 	
	
	private java.lang.Long operateValue;
	//columns END


	public Operate(){
	}

	public Operate(
		java.lang.String operateId
	){
		this.operateId = operateId;
	}

	

	public void setOperateId(java.lang.String value) {
		this.operateId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "OPERATE_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getOperateId() {
		return this.operateId;
	}
	
			
	@Column(name = "OPERATE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getOperateName() {
		return this.operateName;
	}
	
	public void setOperateName(java.lang.String value) {
		this.operateName = value;
	}
	
			
	@Column(name = "OPERATE_VALUE", unique = false, nullable = true, insertable = true, updatable = true, length = 19)
	public java.lang.Long getOperateValue() {
		return this.operateValue;
	}
	
	public void setOperateValue(java.lang.Long value) {
		this.operateValue = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("OperateId",getOperateId())
			.append("OperateName",getOperateName())
			.append("OperateValue",getOperateValue())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getOperateId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Operate == false) return false;
		if(this == obj) return true;
		Operate other = (Operate)obj;
		return new EqualsBuilder()
			.append(getOperateId(),other.getOperateId())
			.isEquals();
	}
	
	public int compareTo(Operate o) {
		if(equals(o)){
			return 0;
		}
		Long operateValue = o.getOperateValue();
		if (this.operateValue > operateValue) {
			return 1;
		} else if (this.operateValue < operateValue) {
			return -1;
		}
		return 0;
	}
}


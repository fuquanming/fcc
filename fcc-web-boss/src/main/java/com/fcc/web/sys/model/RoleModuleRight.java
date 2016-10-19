package com.fcc.web.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>Description:系统模块权限</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_rbac_modright")
public class RoleModuleRight implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 模块ID       db_column: MODULE_ID 
     */ 	
	private java.lang.String moduleId;
    /**
     * 角色ID       db_column: ROLE_ID 
     */ 	
	private java.lang.String roleId;
    /**
     * 色在该模块的总权值       db_column: RIGHT_VALUE 
     */ 	
	
	private java.lang.Long rightValue;
	//columns END


	public RoleModuleRight(){
	}
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "MODULE_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getModuleId() {
		return this.moduleId;
	}
	
	public void setModuleId(java.lang.String value) {
		this.moduleId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")		
	@Column(name = "ROLE_ID", unique = false, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getRoleId() {
		return this.roleId;
	}
	
	public void setRoleId(java.lang.String value) {
		this.roleId = value;
	}
	
			
	@Column(name = "RIGHT_VALUE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public java.lang.Long getRightValue() {
		return this.rightValue;
	}
	
	public void setRightValue(java.lang.Long value) {
		this.rightValue = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("ModuleId",getModuleId())
			.append("RoleId",getRoleId())
			.append("RightValue",getRightValue())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.toHashCode();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof RoleModuleRight){
			RoleModuleRight another = (RoleModuleRight)obj;
			if(another.getModuleId().equals(this.moduleId)
					&& another.getRoleId().equals(this.roleId)){
				return true;
			}
		}
		return false;
	}
}


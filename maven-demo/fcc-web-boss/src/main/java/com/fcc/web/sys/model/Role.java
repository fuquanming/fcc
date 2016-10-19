package com.fcc.web.sys.model;

import java.util.Date;
import java.util.Set;

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
 * <p>Description:角色</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_rbac_role")
public class Role implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	public static final Role ROOT = new Role();//数据库中没有对映记录
	static{
		ROOT.setRoleId("ROLE_ROOT");
		ROOT.setRoleName("系统角色配置");
		ROOT.setRoleOrderNo(-1);
		ROOT.setRoleDesc("");
	}
	
	//用户基础角色
	public static final Role SYS_BASE_ROLE = new Role();//数据库中有对映记录
	static{
		SYS_BASE_ROLE.setRoleId("BASE_ROLE");
		SYS_BASE_ROLE.setRoleName("基础用户");
		SYS_BASE_ROLE.setRoleOrderNo(2);
	}

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * roleId       db_column: ROLE_ID 
     */ 	
	private java.lang.String roleId;
    /**
     * roleName       db_column: ROLE_NAME 
     */ 	
	private java.lang.String roleName;
    /**
     * roleOrderno       db_column: ROLE_ORDERNO 
     */ 	
	
	private java.lang.Integer roleOrderno;
    /**
     * roleDesc       db_column: ROLE_DESC 
     */ 	
	private java.lang.String roleDesc;
	
	private Date createTime;
	
	private String createUser;
	//columns END

	private Set<RoleModuleRight> roleModuleRights; 
	
	public Role(){
	}

	public Role(
		java.lang.String roleId
	){
		this.roleId = roleId;
	}

	

	public void setRoleId(java.lang.String value) {
		this.roleId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "ROLE_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getRoleId() {
		return this.roleId;
	}
	
			
	@Column(name = "ROLE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getRoleName() {
		return this.roleName;
	}
	
	public void setRoleName(java.lang.String value) {
		this.roleName = value;
	}
	
			
	@Column(name = "ROLE_ORDERNO", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getRoleOrderNo() {
		return this.roleOrderno;
	}
	
	public void setRoleOrderNo(java.lang.Integer value) {
		this.roleOrderno = value;
	}
	
			
	@Column(name = "ROLE_DESC")
	public java.lang.String getRoleDesc() {
		return this.roleDesc;
	}
	
	public void setRoleDesc(java.lang.String value) {
		this.roleDesc = value;
	}
	
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Transient
	public Set<RoleModuleRight> getRoleModuleRights() {
		return roleModuleRights;
	}

	public void setRoleModuleRights(Set<RoleModuleRight> roleModuleRights) {
		this.roleModuleRights = roleModuleRights;
	}
	
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("RoleId",getRoleId())
			.append("RoleName",getRoleName())
			.append("RoleOrderNo",getRoleOrderNo())
			.append("RoleDesc",getRoleDesc())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRoleId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Role == false) return false;
		if(this == obj) return true;
		Role other = (Role)obj;
		return new EqualsBuilder()
			.append(getRoleId(),other.getRoleId())
			.isEquals();
	}

	
}


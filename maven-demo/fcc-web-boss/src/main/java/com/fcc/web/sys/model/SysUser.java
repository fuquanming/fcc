package com.fcc.web.sys.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "sys_rbac_user")
public class SysUser implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 用户ID       db_column: USER_ID 
     */ 	
	private java.lang.String userId = "";
    /**
     * 用户名称       db_column: USER_NAME 
     */ 	
	private java.lang.String userName;
    /**
     * 用户密码       db_column: USER_PASS 
     */ 	
	private java.lang.String userPass;
	/**
     * 用户状态       db_column: USER_STATUS
     */ 	
	private java.lang.String userStatus;
    /**
     * 工号       db_column: USER_CODE 
     */ 	
	private java.lang.String userCode;
    /**
     * 所属部门       db_column: USER_DEPT_ID 
     */ 	
	private java.lang.String userDeptId = "";
    /**
     * 职务       db_column: USER_DUTY 
     */ 	
	private java.lang.String userDuty;
    /**
     * 证件类型       db_column: USER_CERT_TYPE 
     */ 	
	private java.lang.String userCertType;
    /**
     * 证件号       db_column: USER_CERT_CODE 
     */ 	
	private java.lang.String userCertCode;
    /**
     * 注册时间       db_column: USER_DATE 
     */ 	
	private java.util.Date userDate;
    /**
     * 国籍       db_column: USER_NATION 
     */ 	
	private java.lang.String userNation;
    /**
     * 性别       db_column: USER_SEX 
     */ 	
	private java.lang.String userSex;
    /**
     * 电话       db_column: USER_PHONE 
     */ 	
	private java.lang.String userPhone;
    /**
     * 手机       db_column: USER_MOBILE 
     */ 	
	private java.lang.String userMobile;
    /**
     * E-mail       db_column: USER_MAIL 
     */ 	
	private java.lang.String userMail;
    /**
     * 地址       db_column: USER_ADDRESS 
     */ 	
	private java.lang.String userAddress;
    /**
     * 邮编       db_column: USER_POST_CODE 
     */ 	
	private java.lang.String userPostCode;
    /**
     * 备注       db_column: USER_REMARK 
     */ 	
	private java.lang.String userRemark;
	
	private Date createTime;
	
	private String createUser;
	
	private Date loginTime;
	//columns END

	private Set<Role> roles;
	// 没有数据库映射
	private String ip;

	public SysUser(){
	}

	public SysUser(
		java.lang.String userId
	){
		this.userId = userId;
	}

	

	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "USER_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserId() {
		return this.userId;
	}
	
			
	@Column(name = "USER_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserName() {
		return this.userName;
	}
	
	public void setUserName(java.lang.String value) {
		this.userName = value;
	}
	
			
	@Column(name = "USER_PASS", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getPassword() {
		return this.userPass;
	}
	
	public void setPassword(java.lang.String value) {
		this.userPass = value;
	}
	
	@Column(name = "USER_STATUS", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public java.lang.String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(java.lang.String userStatus) {
		this.userStatus = userStatus;
	}
			
	@Column(name = "USER_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getCode() {
		return this.userCode;
	}
	
	public void setCode(java.lang.String value) {
		this.userCode = value;
	}
	
			
	@Column(name = "USER_DEPT_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getDept() {
		return this.userDeptId;
	}
	
	public void setDept(java.lang.String value) {
		this.userDeptId = value;
	}
	
			
	@Column(name = "USER_DUTY", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserDuty() {
		return this.userDuty;
	}
	
	public void setUserDuty(java.lang.String value) {
		this.userDuty = value;
	}
	
			
	@Column(name = "USER_CERT_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserCertType() {
		return this.userCertType;
	}
	
	public void setUserCertType(java.lang.String value) {
		this.userCertType = value;
	}
	
			
	@Column(name = "USER_CERT_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserCertCode() {
		return this.userCertCode;
	}
	
	public void setUserCertCode(java.lang.String value) {
		this.userCertCode = value;
	}
	
			
	@Column(name = "USER_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 0)
	public java.util.Date getRegDate() {
		return this.userDate;
	}
	
	public void setRegDate(java.util.Date value) {
		this.userDate = value;
	}
	
			
	@Column(name = "USER_NATION", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserNation() {
		return this.userNation;
	}
	
	public void setUserNation(java.lang.String value) {
		this.userNation = value;
	}
	
			
	@Column(name = "USER_SEX", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserSex() {
		return this.userSex;
	}
	
	public void setUserSex(java.lang.String value) {
		this.userSex = value;
	}
	
			
	@Column(name = "USER_PHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getTel() {
		return this.userPhone;
	}
	
	public void setTel(java.lang.String value) {
		this.userPhone = value;
	}
	
			
	@Column(name = "USER_MOBILE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getMobile() {
		return this.userMobile;
	}
	
	public void setMobile(java.lang.String value) {
		this.userMobile = value;
	}
	
			
	@Column(name = "USER_MAIL", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getEmail() {
		return this.userMail;
	}
	
	public void setEmail(java.lang.String value) {
		this.userMail = value;
	}
	
			
	@Column(name = "USER_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserAddress() {
		return this.userAddress;
	}
	
	public void setUserAddress(java.lang.String value) {
		this.userAddress = value;
	}
	
			
	@Column(name = "USER_POST_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getUserPostCode() {
		return this.userPostCode;
	}
	
	public void setUserPostCode(java.lang.String value) {
		this.userPostCode = value;
	}
	
			
	@Column(name = "USER_REMARK", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getRemark() {
		return this.userRemark;
	}
	
	public void setRemark(java.lang.String value) {
		this.userRemark = value;
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
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("UserId",getUserId())
			.append("UserName",getUserName())
			.append("UserPass",getPassword())
			.append("UserCode",getCode())
			.append("UserDeptId",getDept())
			.append("UserDuty",getUserDuty())
			.append("UserCertType",getUserCertType())
			.append("UserCertCode",getUserCertCode())
			.append("UserDate",getRegDate())
			.append("UserNation",getUserNation())
			.append("UserSex",getUserSex())
			.append("UserPhone",getTel())
			.append("UserMobile",getMobile())
			.append("UserMail",getEmail())
			.append("UserAddress",getUserAddress())
			.append("UserPostCode",getUserPostCode())
			.append("UserRemark",getRemark())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUserId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SysUser == false) return false;
		if(this == obj) return true;
		SysUser other = (SysUser)obj;
		return new EqualsBuilder()
			.append(getUserId(),other.getUserId())
			.isEquals();
	}
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)   
	@JoinTable(name = "SYS_RBAC_USERTOROLE",    
	        joinColumns ={@JoinColumn(name = "user_id") },    
	        inverseJoinColumns = { @JoinColumn(name = "role_id")    
	})
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@Transient
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(name = "LOGIN_TIME")
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	
}


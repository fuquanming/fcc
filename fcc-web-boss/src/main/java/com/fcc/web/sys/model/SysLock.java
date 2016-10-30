package com.fcc.web.sys.model;

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
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_lock")
public class SysLock implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	public static final String STATUS_UNLOCK = "unlock";
	public static final String STATUS_LOCK = "lock";
	
	//alias
//	public static final String TABLE_ALIAS = "系统锁";
//	public static final String ALIAS_LOCK_KEY = "锁的key";
//	public static final String ALIAS_LOCK_STATUS = "当前锁状态";
//	public static final String ALIAS_CREATE_TIME = "创建时间";
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 锁的key       db_column: LOCK_KEY 
     */ 	
	private java.lang.String lockKey;
    /**
     * 当前锁状态，lock，unlock       db_column: LOCK_STATUS 
     */ 	
	private java.lang.String lockStatus;
    /**
     * 创建时间       db_column: CREATE_TIME 
     */ 	
	
	private java.util.Date createTime;
	//columns END


	public SysLock(){
	}

	public SysLock(
		java.lang.String lockKey
	){
		this.lockKey = lockKey;
	}

	

	public void setLockKey(java.lang.String value) {
		this.lockKey = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "LOCK_KEY", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public java.lang.String getLockKey() {
		return this.lockKey;
	}
	
			
	@Column(name = "LOCK_STATUS", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public java.lang.String getLockStatus() {
		return this.lockStatus;
	}
	
	public void setLockStatus(java.lang.String value) {
		this.lockStatus = value;
	}
	
			
	@Column(name = "CREATE_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 0)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("LockKey",getLockKey())
			.append("LockStatus",getLockStatus())
			.append("CreateTime",getCreateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getLockKey())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SysLock == false) return false;
		if(this == obj) return true;
		SysLock other = (SysLock)obj;
		return new EqualsBuilder()
			.append(getLockKey(),other.getLockKey())
			.isEquals();
	}
}


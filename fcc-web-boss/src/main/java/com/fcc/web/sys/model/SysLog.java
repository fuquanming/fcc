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
@Table(name = "sys_log")
public class SysLog implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "系统日志";
	public static final String ALIAS_LOG_ID = "主键";
	public static final String ALIAS_USER_ID = "用户ID";
	public static final String ALIAS_USER_NAME = "用户名称";
	public static final String ALIAS_IP_ADDRESS = "IP地址";
	public static final String ALIAS_LOG_TIME = "日志时间";
	public static final String ALIAS_MODULE_NAME = "模块名称";
	public static final String ALIAS_OPERATE_NAME = "操作名称";
	public static final String ALIAS_EVENT_PARAM = "事件参数";
	public static final String ALIAS_EVENT_OBJECT = "事件对象";
	public static final String ALIAS_EVENT_RESULT = "事件结果";
	/** 事件成功 1 */
	public static final String EVENT_RESULT_OK = "1";
	/** 事件失败 0 */
	public static final String EVENT_RESULT_FAIL = "0";
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 键主       db_column: LOG_ID 
     */ 	
	private java.lang.Long logId;
    /**
     * 用户ID       db_column: USER_ID 
     */ 	
	private java.lang.String userId;
    /**
     * 用户名称       db_column: USER_NAME 
     */ 	
	private java.lang.String userName;
    /**
     * IP地址       db_column: IP_ADDRESS 
     */ 	
	private java.lang.String ipAddress;
    /**
     * 日志时间       db_column: LOG_TIME 
     */ 	
	
	private java.util.Date logTime;
    /**
     * 模块名称       db_column: MODULE_NAME 
     */ 	
	private java.lang.String moduleName;
    /**
     * 操作名称       db_column: OPERATE_NAME 
     */ 	
	private java.lang.String operateName;
    /**
     * 事件关联ID       db_column: EVENT_ID 
     */ 	
	private java.lang.String eventParam;
    /**
     * 事件对象       db_column: EVENT_OBJECT 
     */ 	
	private java.lang.String eventObject;
    /**
     * 事件结果       db_column: EVENT_RESULT 1:成功；0:失败
     */ 	
	private java.lang.String eventResult;
	//columns END


	public SysLog(){
	}

	public SysLog(
		java.lang.Long logId
	){
		this.logId = logId;
	}

	

	public void setLogId(java.lang.Long value) {
		this.logId = value;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "identity")
	@Column(name = "LOG_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 100)
//	@Id
//	@Column(name = "LOG_ID")
//	@SequenceGenerator(name = "SEQ_SYS_LOG", allocationSize = 1, sequenceName = "SEQ_SYS_LOG")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "SEQ_SYS_LOG")
	public java.lang.Long getLogId() {
		return this.logId;
	}
	
			
	@Column(name = "USER_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getUserId() {
		return this.userId;
	}
	
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
			
	@Column(name = "USER_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getUserName() {
		return this.userName;
	}
	
	public void setUserName(java.lang.String value) {
		this.userName = value;
	}
	
			
	@Column(name = "IP_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true, length = 24)
	public java.lang.String getIpAddress() {
		return this.ipAddress;
	}
	
	public void setIpAddress(java.lang.String value) {
		this.ipAddress = value;
	}
	
			
	@Column(name = "LOG_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 0)
	public java.util.Date getLogTime() {
		return this.logTime;
	}
	
	public void setLogTime(java.util.Date value) {
		this.logTime = value;
	}
	
			
	@Column(name = "MODULE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getModuleName() {
		return this.moduleName;
	}
	
	public void setModuleName(java.lang.String value) {
		this.moduleName = value;
	}
	
			
	@Column(name = "OPERATE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getOperateName() {
		return this.operateName;
	}
	
	public void setOperateName(java.lang.String value) {
		this.operateName = value;
	}
	
			
	@Column(name = "EVENT_PARAM", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
	public java.lang.String getEventParam() {
		return this.eventParam;
	}
	
	public void setEventParam(java.lang.String value) {
		this.eventParam = value;
	}
	
			
	@Column(name = "EVENT_OBJECT", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public java.lang.String getEventObject() {
		return this.eventObject;
	}
	
	public void setEventObject(java.lang.String value) {
		this.eventObject = value;
	}
	
			
	@Column(name = "EVENT_RESULT", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getEventResult() {
		return this.eventResult;
	}
	
	public void setEventResult(java.lang.String value) {
		this.eventResult = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("LogId",getLogId())
			.append("UserId",getUserId())
			.append("UserName",getUserName())
			.append("IpAddress",getIpAddress())
			.append("LogTime",getLogTime())
			.append("ModuleName",getModuleName())
			.append("OperateName",getOperateName())
			.append("EventId",getEventParam())
			.append("EventObject",getEventObject())
			.append("EventResult",getEventResult())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getLogId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SysLog == false) return false;
		if(this == obj) return true;
		SysLog other = (SysLog)obj;
		return new EqualsBuilder()
			.append(getLogId(),other.getLogId())
			.isEquals();
	}
}


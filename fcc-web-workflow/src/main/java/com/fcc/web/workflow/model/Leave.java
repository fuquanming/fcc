package com.fcc.web.workflow.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fcc.commons.workflow.model.WorkflowBean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Description:Leave</p>
 */

@Entity
@Table(name = "oa_leave")
public class Leave extends WorkflowBean implements java.io.Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
    
    public static final String processDefinitionKey = "leave";
    
    public static final String processDefinitionName = "请假";
    

    //可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    //columns START
    /**
     * 主键       db_column: LEAVE_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String leaveId;
    /**
     * 开始时间       db_column: START_TIME 
     */     
    // 不再限制长度  
    private java.util.Date startTime;
    /**
     * 结束时间       db_column: END_TIME 
     */     
    // 不再限制长度  
    private java.util.Date endTime;
    /**
     * 内容       db_column: CONTENT 
     */     
    // 不再限制长度  @Length(max=500)
    private java.lang.String content;
    //columns END


	public Leave(){
	}

	public Leave(
		java.lang.String leaveId
	){
		this.leaveId = leaveId;
	}

    

    public void setLeaveId(java.lang.String value) {
        this.leaveId = value;
    }
    @Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "uuid")
    @Column(name = "LEAVE_ID", unique = true, nullable = false, insertable = true, updatable = true)//, length = 32
    public java.lang.String getLeaveId() {
        return this.leaveId;
    }
    
    @Column(name = "START_TIME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 0
    public java.util.Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(java.util.Date value) {
        this.startTime = value;
    }
    @Column(name = "END_TIME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 0
    public java.util.Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(java.util.Date value) {
        this.endTime = value;
    }
    @Column(name = "CONTENT", unique = false, nullable = true, insertable = true, updatable = true)//, length = 500
    public java.lang.String getContent() {
        return this.content;
    }
    
    public void setContent(java.lang.String value) {
        this.content = value;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("LeaveId",getLeaveId())
            .append("StartTime",getStartTime())
            .append("EndTime",getEndTime())
            .append("Content",getContent())
            .append("ProcessInstanceId",getProcessInstanceId())
            .append("ProcessDefinitionId",getProcessDefinitionId())
            .append("ProcessNodeName",getProcessNodeName())
            .append("Status",getStatus())
            .append("CreateUser",getCreateUser())
            .append("CreateTime",getCreateTime())
            .append("UpdateUser",getUpdateUser())
            .append("UpdateTime",getUpdateTime())
            .toString();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLeaveId())
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof Leave == false) return false;
        if(this == obj) return true;
        Leave other = (Leave)obj;
        return new EqualsBuilder()
            .append(getLeaveId(),other.getLeaveId())
            .isEquals();
    }
    
    @Transient
    @Override
    public String getBusinessKey() {
        return this.leaveId;
    }

    @Transient
    @Override
    public String getDefinitionKey() {
        return processDefinitionKey;
    }
}


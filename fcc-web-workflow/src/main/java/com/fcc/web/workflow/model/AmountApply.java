package com.fcc.web.workflow.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fcc.commons.workflow.model.WorkflowBean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Description:AmountApply</p>
 */

@Entity
@Table(name = "sys_amount_apply")
public class AmountApply extends WorkflowBean implements java.io.Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
    
    public static final String processDefinitionKey = "amount_apply";
    
    public static final String processDefinitionName = "申请额度";
    

    //可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    //columns START
    /**
     * 借款额度申请ID       db_column: AMOUNT_APPLY_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String amountApplyId;
    /**
     * 借款人ID（会员）       db_column: USER_ID 
     */     
    // 不再限制长度  
    private java.lang.Long userId;
    /**
     * 会员账号名称       db_column: USER_NAME 
     */     
    // 不再限制长度  @Length(max=20)
    private java.lang.String userName;
    /**
     * 发起人申请的额度       db_column: PRIMARY_AMOUNT 
     */     
    // 不再限制长度  
    private java.lang.Double primaryAmount;
    /**
     * 申请备注       db_column: APPLY_REMARK 
     */     
    // 不再限制长度  @Length(max=4000)
    private java.lang.String applyRemark;
    /**
     * 申请时间       db_column: APPLY_TIME 
     */     
    // 不再限制长度  
    private java.util.Date applyTime;
    //columns END


	public AmountApply(){
	}

	public AmountApply(
		java.lang.String amountApplyId
	){
		this.amountApplyId = amountApplyId;
	}

    

    public void setAmountApplyId(java.lang.String value) {
        this.amountApplyId = value;
    }
    @Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "uuid")
    @Column(name = "AMOUNT_APPLY_ID", unique = true, nullable = false, insertable = true, updatable = true)//, length = 32
    public java.lang.String getAmountApplyId() {
        return this.amountApplyId;
    }
    
    @Column(name = "USER_ID", unique = false, nullable = true, insertable = true, updatable = true)//, length = 19
    public java.lang.Long getUserId() {
        return this.userId;
    }
    
    public void setUserId(java.lang.Long value) {
        this.userId = value;
    }
    @Column(name = "USER_NAME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 20
    public java.lang.String getUserName() {
        return this.userName;
    }
    
    public void setUserName(java.lang.String value) {
        this.userName = value;
    }
    @Column(name = "PRIMARY_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true)//, length = 11
    public java.lang.Double getPrimaryAmount() {
        return this.primaryAmount;
    }
    
    public void setPrimaryAmount(java.lang.Double value) {
        this.primaryAmount = value;
    }
    @Column(name = "APPLY_REMARK", unique = false, nullable = true, insertable = true, updatable = true)//, length = 4000
    public java.lang.String getApplyRemark() {
        return this.applyRemark;
    }
    
    public void setApplyRemark(java.lang.String value) {
        this.applyRemark = value;
    }
    @Column(name = "APPLY_TIME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 0
    public java.util.Date getApplyTime() {
        return this.applyTime;
    }
    
    public void setApplyTime(java.util.Date value) {
        this.applyTime = value;
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("AmountApplyId",getAmountApplyId())
            .append("UserId",getUserId())
            .append("UserName",getUserName())
            .append("PrimaryAmount",getPrimaryAmount())
            .append("ApplyRemark",getApplyRemark())
            .append("ApplyTime",getApplyTime())
            .append("ProcessInstanceId",getProcessInstanceId())
            .append("ProcessDefinitionId",getProcessDefinitionId())
            .append("ProcessNodeName",getProcessNodeName())
            .append("Status",getStatus())
            .append("CreateUser",getCreateUser())
            .append("CreateTime",getCreateTime())
            .toString();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAmountApplyId())
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof AmountApply == false) return false;
        if(this == obj) return true;
        AmountApply other = (AmountApply)obj;
        return new EqualsBuilder()
            .append(getAmountApplyId(),other.getAmountApplyId())
            .isEquals();
    }
    
    @Transient
    @Override
    public String getBusinessKey() {
        return this.amountApplyId;
    }

    @Transient
    @Override
    public String getDefinitionKey() {
        return processDefinitionKey;
    }
}


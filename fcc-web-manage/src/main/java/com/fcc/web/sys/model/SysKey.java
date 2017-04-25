package com.fcc.web.sys.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Description:SysKey</p>
 */

@Entity
@Table(name = "sys_key")
public class SysKey implements java.io.Serializable{
    private static final long serialVersionUID = 5454155825314635342L;
    

    //可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    //columns START
    /**
     * KEY主键       db_column: KYE_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String kyeId;
    /**
     * 关联类型       db_column: LINK_TYPE 
     */     
    // 不再限制长度  @Length(max=10)
    private java.lang.String linkType;
    /**
     * 关联ID       db_column: LINK_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String linkId;
    /**
     * key值       db_column: KEY_VALUE 
     */     
    // 不再限制长度  @Length(max=500)
    private java.lang.String keyValue;
    //columns END


	public SysKey(){
	}

	public SysKey(
		java.lang.String kyeId
	){
		this.kyeId = kyeId;
	}

    

    public void setKyeId(java.lang.String value) {
        this.kyeId = value;
    }
    @Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "uuid")
    @Column(name = "KYE_ID", unique = true, nullable = false, insertable = true, updatable = true)//, length = 32
    public java.lang.String getKyeId() {
        return this.kyeId;
    }
    
            
    @Column(name = "LINK_TYPE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 10
    public java.lang.String getLinkType() {
        return this.linkType;
    }
    
    public void setLinkType(java.lang.String value) {
        this.linkType = value;
    }
    
            
    @Column(name = "LINK_ID", unique = false, nullable = true, insertable = true, updatable = true)//, length = 32
    public java.lang.String getLinkId() {
        return this.linkId;
    }
    
    public void setLinkId(java.lang.String value) {
        this.linkId = value;
    }
    
            
    @Column(name = "KEY_VALUE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 500
    public java.lang.String getKeyValue() {
        return this.keyValue;
    }
    
    public void setKeyValue(java.lang.String value) {
        this.keyValue = value;
    }
    

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("KyeId",getKyeId())
            .append("LinkType",getLinkType())
            .append("LinkId",getLinkId())
            .append("KeyValue",getKeyValue())
            .toString();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getKyeId())
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof SysKey == false) return false;
        if(this == obj) return true;
        SysKey other = (SysKey)obj;
        return new EqualsBuilder()
            .append(getKyeId(),other.getKyeId())
            .isEquals();
    }
}


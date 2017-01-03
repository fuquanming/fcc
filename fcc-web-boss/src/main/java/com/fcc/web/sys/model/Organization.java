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

import com.fcc.commons.web.model.Treeable;

/**
 * <p>Description:组织机构</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @email fuquanming@gmail.com
 */
@Entity
@Table(name = "sys_organ")
public class Organization extends Treeable implements Comparable<Organization>, java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;
	
	public static final Organization ROOT = new Organization();
	static{
		ROOT.setOrganId("ROOT");
		ROOT.setOrganCode("");
		ROOT.setOrganDesc("");
		ROOT.setOrganLevel(0);
		ROOT.setOrganName("机构(部门)");
		ROOT.setOrganSort(0);
	}
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * organId       db_column: ORGAN_ID 
     */ 	
	private java.lang.String organId;
    /**
     * organName       db_column: ORGAN_NAME 
     */ 	
	private java.lang.String organName;
    /**
     * organCode       db_column: ORGAN_CODE 
     */ 	
	private java.lang.String organCode;
    /**
     * organLevel       db_column: ORGAN_LEVEL 
     */ 	
	private java.lang.Integer organLevel;
    /**
     * organSort       db_column: ORGAN_SORT 
     */ 	
	private java.lang.Integer organSort;
    /**
     * organDesc       db_column: ORGAN_DESC 
     */ 	
	private java.lang.String organDesc;
	// 是否可用
    private boolean organStatus = Boolean.TRUE;
    /** 父ID */
    private String parentId;
    /** 所有父路径 如1-2-3 */
    private String parentIds;
	//columns END


	public Organization(){
	}

	public Organization(
		java.lang.String organId
	){
		this.organId = organId;
	}
	
	@Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "assigned")
    @Column(name = "ORGAN_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getOrganId() {
		return this.organId;
	}
	
	public void setOrganId(java.lang.String value) {
        this.organId = value;
    }
			
	@Column(name = "ORGAN_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getOrganName() {
		return this.organName;
	}
	
	public void setOrganName(java.lang.String value) {
		this.organName = value;
	}
	
			
	@Column(name = "ORGAN_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getOrganCode() {
		return this.organCode;
	}
	
	public void setOrganCode(java.lang.String value) {
		this.organCode = value;
	}
	
			
	@Column(name = "ORGAN_LEVEL", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getOrganLevel() {
		return this.organLevel;
	}
	
	public void setOrganLevel(java.lang.Integer value) {
		this.organLevel = value;
	}
	
			
	@Column(name = "ORGAN_SORT", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getOrganSort() {
		return this.organSort;
	}
	
	public void setOrganSort(java.lang.Integer value) {
		this.organSort = value;
	}
	
			
	@Column(name = "ORGAN_DESC", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getOrganDesc() {
		return this.organDesc;
	}
	
	public void setOrganDesc(java.lang.String value) {
		this.organDesc = value;
	}
	@Column(name = "ORGAN_STATUS")
	public boolean getOrganStatus() {
        return organStatus;
    }

    public void setOrganStatus(boolean organStatus) {
        this.organStatus = organStatus;
    }
	
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    };
    public void setParentId(String parentId) {
        super.setParentId(parentId);
        this.parentId = parentId;
    }
    @Column(name = "PARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        super.setParentIds(parentIds);
        this.parentIds = parentIds;
    }

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("OrganId",getOrganId())
			.append("OrganName",getOrganName())
			.append("OrganCode",getOrganCode())
			.append("OrganLevel",getOrganLevel())
			.append("OrganSort",getOrganSort())
			.append("OrganDesc",getOrganDesc())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getOrganId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Organization == false) return false;
		if(this == obj) return true;
		Organization other = (Organization)obj;
		return new EqualsBuilder()
			.append(getOrganId(),other.getOrganId())
			.isEquals();
	}
	
//	/**
//	 * 获取父节点ID编号
//	 */
//	@Transient
//	public String getParentIdTwo(){
//		return Organization.getParentOrganId(this.organId);
//	}
//	
//	/**
//	 * 获取父机构ID编号
//	 * @param organId 子机构ID
//	 * @return
//	 */
//	@Transient
//	public static String getParentOrganId(String organId){
//		String parentOrganID = "";
//		int lastSplitPos = organId.lastIndexOf('-');
//		if(lastSplitPos >0){
//			parentOrganID = organId.substring(0,lastSplitPos);
//		}else{
//			parentOrganID = ROOT.getOrganId();
//		}
//		return parentOrganID;
//	}
	
	public int compareTo(Organization o) {
		if(equals(o)){
			return 0;
		}
		Integer modLevel = o.getOrganLevel();
		Integer modSort = o.getOrganSort();
		if (this.organLevel > modLevel) {
			return 1;
		} else if (this.organLevel < modLevel) {
			return -1;
		} else if (this.organSort > modSort){
			return 1;
		} else if (this.organSort < modSort){
			return -1;
		} else {
			return this.organId.compareTo(o.getOrganId());
		}
	}
}


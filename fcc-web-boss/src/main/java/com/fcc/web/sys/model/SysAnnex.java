package com.fcc.web.sys.model;

import java.io.File;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fcc.web.sys.config.ConfigUtil;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Description:SysAnnex</p>
 */

@Entity
@Table(name = "sys_annex")
public class SysAnnex implements java.io.Serializable{
    private static final long serialVersionUID = 5454155825314635342L;
    

    //可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    //columns START
    /**
     * 附件主键       db_column: ANNEX_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String annexId;
    /**
     * 关联ID       db_column: LINK_ID 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String linkId;
    /**
     * 关联类型       db_column: LINK_TYPE 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String linkType;
    /**
     * 附件名称       db_column: ANNEX_NAME 
     */     
    // 不再限制长度  @Length(max=50)
    private java.lang.String annexName;
    /**
     * 附件类型       db_column: ANNEX_TYPE 
     */     
    // 不再限制长度  @Length(max=50)
    private java.lang.String annexType;
    /**
     * 文件名称       db_column: FILE_NAME 
     */     
    // 不再限制长度  @Length(max=50)
    private java.lang.String fileName;
    /**
     * 文件类型       db_column: FILE_TYPE 
     */     
    // 不再限制长度  @Length(max=50)
    private java.lang.String fileType;
    /**
     * 文件地址       db_column: FILE_URL 
     */     
    // 不再限制长度  @Length(max=100)
    private java.lang.String fileUrl;
    /**
     * 文件大小       db_column: FILE_SIZE 
     */     
    // 不再限制长度  
    private java.lang.Long fileSize;
    /**
     * 文件备注       db_column: REMARK 
     */     
    // 不再限制长度  @Length(max=200)
    private java.lang.String remark;
    /**
     * 创建者       db_column: CREATE_USER 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String createUser;
    /**
     * 创建时间       db_column: CREATE_TIME 
     */     
    // 不再限制长度  
    private java.util.Date createTime;
    /**
     * 更新者       db_column: UPDATE_USER 
     */     
    // 不再限制长度  @Length(max=32)
    private java.lang.String updateUser;
    /**
     * 更新时间       db_column: UPDATE_TIME 
     */     
    // 不再限制长度  
    private java.util.Date updateTime;
    //columns END
    /** web访问的地址 */
    private String url;
    /** 物理访问的地址 */
    private String filePath;

    public SysAnnex(){
	}

	public SysAnnex(
		java.lang.String annexId
	){
		this.annexId = annexId;
	}

    

    public void setAnnexId(java.lang.String value) {
        this.annexId = value;
    }
    @Id @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy = "uuid")
    @Column(name = "ANNEX_ID", unique = true, nullable = false, insertable = true, updatable = true)//, length = 32
    public java.lang.String getAnnexId() {
        return this.annexId;
    }
    
            
    @Column(name = "LINK_ID", unique = false, nullable = true, insertable = true, updatable = true)//, length = 32
    public java.lang.String getLinkId() {
        return this.linkId;
    }
    
    public void setLinkId(java.lang.String value) {
        this.linkId = value;
    }
    
            
    @Column(name = "LINK_TYPE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 32
    public java.lang.String getLinkType() {
        return this.linkType;
    }
    
    public void setLinkType(java.lang.String value) {
        this.linkType = value;
    }
    
            
    @Column(name = "ANNEX_NAME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 50
    public java.lang.String getAnnexName() {
        return this.annexName;
    }
    
    public void setAnnexName(java.lang.String value) {
        this.annexName = value;
    }
    
    @Column(name = "ANNEX_TYPE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 50
    public java.lang.String getAnnexType() {
        return this.annexType;
    }
    
    public void setAnnexType(java.lang.String value) {
        this.annexType = value;
    }
    
            
    @Column(name = "FILE_NAME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 50
    public java.lang.String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(java.lang.String value) {
        this.fileName = value;
    }
    
            
    @Column(name = "FILE_TYPE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 50
    public java.lang.String getFileType() {
        return this.fileType;
    }
    
    public void setFileType(java.lang.String value) {
        this.fileType = value;
    }
    
            
    @Column(name = "FILE_URL", unique = false, nullable = true, insertable = true, updatable = true)//, length = 100
    public java.lang.String getFileUrl() {
        return this.fileUrl;
    }
    
    public void setFileUrl(java.lang.String value) {
        this.fileUrl = value;
    }
    
            
    @Column(name = "FILE_SIZE", unique = false, nullable = true, insertable = true, updatable = true)//, length = 19
    public java.lang.Long getFileSize() {
        return this.fileSize;
    }
    
    public void setFileSize(java.lang.Long value) {
        this.fileSize = value;
    }
    
            
    @Column(name = "REMARK", unique = false, nullable = true, insertable = true, updatable = true)//, length = 200
    public java.lang.String getRemark() {
        return this.remark;
    }
    
    public void setRemark(java.lang.String value) {
        this.remark = value;
    }
    
            
    @Column(name = "CREATE_USER", unique = false, nullable = true, insertable = true, updatable = true)//, length = 32
    public java.lang.String getCreateUser() {
        return this.createUser;
    }
    
    public void setCreateUser(java.lang.String value) {
        this.createUser = value;
    }
    
            
    @Column(name = "CREATE_TIME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 19
    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(java.util.Date value) {
        this.createTime = value;
    }
    
            
    @Column(name = "UPDATE_USER", unique = false, nullable = true, insertable = true, updatable = true)//, length = 32
    public java.lang.String getUpdateUser() {
        return this.updateUser;
    }
    
    public void setUpdateUser(java.lang.String value) {
        this.updateUser = value;
    }
    
            
    @Column(name = "UPDATE_TIME", unique = false, nullable = true, insertable = true, updatable = true)//, length = 19
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(java.util.Date value) {
        this.updateTime = value;
    }
    
    @Transient
    public String getUrl() {
        if (this.url == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ConfigUtil.getFileAccessPath()).append(this.fileUrl).append("/").append(this.fileName);
            this.url = sb.toString();
        }
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Transient
    public String getFilePath() {
        if (this.filePath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ConfigUtil.getFileUploadPath()).append(this.fileUrl).append(File.separatorChar).append(this.fileName);
            this.filePath = sb.toString();
        }
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("AnnexId",getAnnexId())
            .append("LinkId",getLinkId())
            .append("LinkType",getLinkType())
            .append("AnnexName",getAnnexName())
            .append("FileName",getFileName())
            .append("FileType",getFileType())
            .append("FileUrl",getFileUrl())
            .append("FileSize",getFileSize())
            .append("Remark",getRemark())
            .append("CreateUser",getCreateUser())
            .append("CreateTime",getCreateTime())
            .append("UpdateUser",getUpdateUser())
            .append("UpdateTime",getUpdateTime())
            .toString();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAnnexId())
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof SysAnnex == false) return false;
        if(this == obj) return true;
        SysAnnex other = (SysAnnex)obj;
        return new EqualsBuilder()
            .append(getAnnexId(),other.getAnnexId())
            .isEquals();
    }
}


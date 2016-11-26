package com.fcc.web.sys.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fcc.commons.web.model.Treeable;
/**
 * <p>Description:系统模块</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Entity
@Table(name = "sys_rbac_module")
public class Module extends Treeable implements Comparable<Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6965867174909283799L;
	public static final Module ROOT = new Module();//数据库中没有对映记录
	static{
		ROOT.setModuleId("ROOT");
		ROOT.setModuleName("系统模块");
		ROOT.setModuleLevel(0);
		ROOT.setModuleSort(0);
	}
	//  MODULE_ID    VARCHAR2(50) primary key, 
	private String moduleId;
	//  MODULE_NAME  VARCHAR2(100) not null,
	private String moduleName;
	//  MODULE_LEVEL INTEGER not null,
	private Integer moduleLevel;
	//  MODULE_SORT  INTEGER not null,
	private Integer moduleSort;
    //  MODULE_DESC  VARCHAR2(100)
	private String moduleDesc;
	// 是否可用
    private Boolean show = Boolean.TRUE;
    /** 父ID */
    private String parentId;
    /** 所有父路径 如1-2-3 */
    private String parentIds;
	
	private Set<Operate> operates = null;
	
	public Module() {
	}

	/**
	 * @return 返回 moduleDesc。

	 */
	@Column(name = "MODULE_DESC", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public String getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * @param moduleDesc 要设置的 module_desc。

	 */
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	/**
	 * @return 返回 moduleSort。

	 */
	@Column(name = "MODULE_SORT", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public Integer getModuleSort() {
		return moduleSort;
	}

	/**
	 * @param moduleSort 要设置的 moduleSort。

	 */
	public void setModuleSort(Integer moduleSort) {
		this.moduleSort = moduleSort;
	}

	/**
	 * @return 返回 moduleID。

	 */
	@Id @GeneratedValue(generator="paymentableGenerator")
	@GenericGenerator(name="paymentableGenerator", strategy = "assigned")
	@Column(name = "MODULE_ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId 要设置的 moduleID。

	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return 返回 moduleLevel。

	 */
	@Column(name = "MODULE_LEVEL", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public Integer getModuleLevel() {
		return moduleLevel;
	}

	/**
	 * @param moduleLevel 要设置的 moduleLevel。

	 */
	public void setModuleLevel(Integer moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

	/**
	 * @return 返回 moduleName。

	 */
	@Column(name = "MODULE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName 要设置的 moduleName。

	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	@Column(name = "IS_SHOW")
	public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
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
	
	@ManyToMany(fetch = FetchType.LAZY)   
	@JoinTable(name = "SYS_RBAC_MOD2OP",    
	        joinColumns ={@JoinColumn(name = "module_id") },    
	        inverseJoinColumns = { @JoinColumn(name = "operate_id")    
	})
	public Set<Operate> getOperates() {
		return operates;
	}

	public void setOperates(Set<Operate> operates) {
		this.operates = operates;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Module){
			Module module = (Module)obj; 
			if(this.moduleId.equals(module.getModuleId())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		if(this.moduleId != null){
			return this.moduleId.hashCode();
		}else{
			return 0;
		}
	}

	public int compareTo(Object o) {
		if(equals(o)){
			return 0;
		}
		if(o instanceof Module){
			Module mod = (Module)o;
			Integer modLevel = mod.getModuleLevel();
			Integer modSort = mod.getModuleSort();
			if (this.moduleLevel > modLevel) {
				return 1;
			} else if (this.moduleLevel < modLevel) {
				return -1;
			} else if (this.moduleSort > modSort){
				return 1;
			} else if (this.moduleSort < modSort){
				return -1;
			} else {
				return this.parentIds.compareTo(mod.getParentIds());
			}
		}
		return 0;
	}		
	
}

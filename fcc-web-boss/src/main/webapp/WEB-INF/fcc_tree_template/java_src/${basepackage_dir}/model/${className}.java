<#include "/macro.include"/>
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fcc.commons.web.model.Treeable;


/**
 * <p>Description:${table.tableAlias}</p>
 */

@Entity
@Table(name = "${table.sqlName}")
public class ${className} extends Treeable implements java.io.Serializable {
    
    private static final long serialVersionUID = 5454155825314635342L;
    
}
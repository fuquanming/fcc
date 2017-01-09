package com.fcc.web.sys.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fcc.commons.web.model.Treeable;

/**
 * <p>Description:Area</p>
 */

@Entity
@Table(name = "sys_area")
public class Area extends Treeable implements java.io.Serializable{
    
    private static final long serialVersionUID = 5454155825314635342L;
    
}


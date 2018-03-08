/*
 * @(#)CasServvice.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-cas
 * 创建日期 : 2018年3月8日
 * 修改历史 : 
 *     1. [2018年3月8日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service;

import org.jasig.cas.client.validation.Assertion;

import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.model.SysUser;

/**
 * Cas登录
 * @version 
 * @author 傅泉明
 */
public interface CasService {
    /**
     * 通过CAS对象登录
     * @param assertion
     * @return
     */
    public SysUser login(Assertion assertion) throws RefusedException, Exception; 
    
}

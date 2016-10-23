package com.fcc.web.sys.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.web.sys.cache.CacheUtil;
import com.fcc.web.sys.model.SysUser;

public class AppWebController extends BaseController {

    public SysUser getSysUser(HttpServletRequest request) {
        return CacheUtil.getSysUser(request);
    }
    
    /** 是否是Admin用户 */
    public boolean isAdmin(HttpServletRequest request) {
        SysUser sysUser = getSysUser(request);
        if (sysUser == null) {
            return false;
        }
        String organId = sysUser.getDept();
        if (StringUtils.isEmpty(organId)) {// 管理员无组织机构
            return true;
        }
        return false;
    }
    
}

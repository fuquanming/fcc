package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.commons.web.service.ConfigService;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;

@Controller
public class AppWebController extends BaseController {

    @Resource
    ConfigService configService;
    @Resource
    CacheService cacheService;
    
    public void setSysUser(SysUser sysUser, HttpServletRequest request) {
        request.getSession().setAttribute(Constants.SysUserSession.loginUser, sysUser);
    }
    
    public SysUser getSysUser(HttpServletRequest request) {
        return cacheService.getSysUser(request);
    }
    
    public void execute(Runnable runnable) {
        configService.getThreadPool().execute(runnable);
    }
    
    public boolean isGroup() {
        return ConfigUtil.isGroup();
    }
    
    /** 重载模块缓存 */
    public void reloadModuleCache() {
        execute(new Runnable() {
            @Override
            public void run() {
                cacheService.cleanModuleMap();
                cacheService.cleanModuleUrlMap();
                cacheService.getModuleMap();
                cacheService.getModuleUrlMap();
            }
        });
    }
    /** 重载角色模块权限缓存 */
    public void reloadRoleModuleRightCache() {
        execute(new Runnable() {
            @Override
            public void run() {
                cacheService.cleanRoleModuleRightMap();
                cacheService.getRoleModuleRightMap();
            }
        });
    }
    /** 重载操作缓存 */
    public void reloadOperateCache() {
        execute(new Runnable() {
            @Override
            public void run() {
                cacheService.cleanOperateMap();
                cacheService.getOperateMap();
            }
        });
    }
}

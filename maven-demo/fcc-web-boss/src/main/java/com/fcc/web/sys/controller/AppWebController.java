package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;

@Controller
public class AppWebController extends BaseController {

    @Resource
    private CacheService cacheService;
    
    public SysUser getSysUser(HttpServletRequest request) {
        return (SysUser) request.getSession().getAttribute(Constants.SysUserSession.loginUser);
    }
    
    public void execute(Runnable runnable) {
        cacheService.getThreadPool().execute(runnable);
    }
    /** 重载模块缓存 */
    public void reloadModuleCache() {
        execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("cleanModuleMap");
                cacheService.cleanModuleMap();
                System.out.println("reloadModuleCache");
                cacheService.getModuleMap();
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
    
}

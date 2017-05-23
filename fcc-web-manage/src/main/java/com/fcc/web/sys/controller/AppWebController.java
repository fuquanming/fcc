package com.fcc.web.sys.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysAnnex;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.SysAnnexService;
import com.fcc.web.sys.util.SysAnnexUtil;

public class AppWebController extends BaseController {

    @Resource
    CacheService cacheService;
    @Resource
    SysAnnexService sysAnnexService;
    /**
     * 取得文件访问前缀路径,request.setAttribute("filePath")
     * @param request
     */
    public void setFilePath(HttpServletRequest request) {
        request.setAttribute("filePath", ConfigUtil.getFileAccessPath());
    }
    
    public void setSysUser(SysUser sysUser, HttpServletRequest request) {
        request.getSession().setAttribute(Constants.SysUserSession.loginUser, sysUser);
    }
    
    public SysUser getSysUser() {
//        return cacheService.getSysUser(request);
        return SysUserAuthentication.getSysUser();
    }
    
    public boolean isGroup() {
        return ConfigUtil.isGroup();
    }
    /**
     * 保存上传的文件
     * @param linkId    关联ID
     * @param request
     * @return
     */
    public List<SysAnnex> addUploadFile(String linkId, String linkType, String annexType, HttpServletRequest request) {
        Map<String, String[]> fileMap = SysAnnexUtil.getUploadFileName(linkType, annexType, request);
        List<SysAnnex> list = Collections.emptyList();
        if (fileMap != null && fileMap.size() > 0) {
            String[] fileNames = fileMap.get(SysAnnexUtil.fileNameKey);
            String[] fileRealNames = fileMap.get(SysAnnexUtil.fileRealNameKey);
            if (fileNames != null && fileNames.length > 0) {
                list = sysAnnexService.add(linkType, linkId, annexType, fileNames, fileRealNames);
            }
        }
        return list;
    }
    
    /** 重载模块缓存 */
    public void reloadModuleCache() {
//        execute(new Runnable() {
//            @Override
//            public void run() {
//                cacheService.updateModuleMap();
//                cacheService.updateModuleUrlMap();
//            }
//        });
        cacheService.updateModuleMap();
        cacheService.updateModuleUrlMap();
    }
    /** 重载角色模块权限缓存 */
    public void reloadRoleModuleRightCache() {
//        execute(new Runnable() {
//            @Override
//            public void run() {
//                cacheService.updateRoleModuleRightMap();
//            }
//        });
        cacheService.updateRoleModuleRightMap();
    }
    /** 重载操作缓存 */
    public void reloadOperateCache() {
//        execute(new Runnable() {
//            @Override
//            public void run() {
//                cacheService.updateOperateMap();
//            }
//        });
        cacheService.updateOperateMap();
    }
}

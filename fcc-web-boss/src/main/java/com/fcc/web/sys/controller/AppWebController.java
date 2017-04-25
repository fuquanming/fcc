package com.fcc.web.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.fcc.commons.web.controller.BaseController;
import com.fcc.web.sys.cache.SysUserAuthentication;
import com.fcc.web.sys.common.Constants;
import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.CacheService;
import com.fcc.web.sys.service.SysAnnexService;

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
    public boolean addUploadFile(String linkId, HttpServletRequest request) {
        // 附件
        String[] linkType = request.getParameterValues("linkType");// 附件关联类型
        String[] annexType = request.getParameterValues("annexType");// 附件类型
        boolean flag = true;
        if (linkType != null) {
            boolean temp = false;
            int length = linkType.length;
            for (int i = 0; i < length; i++) {
                String link = linkType[i];
                String annex = annexType[i];
                String[] fileName = request.getParameterValues(annex + "-uploadFileName");// 提交的文件名
                String[] fileRealName = request.getParameterValues(annex + "-uploadFileRealName");// 保存的文件名
                String fName = fileName[i];
                String frName = fileRealName[i];
                String[] fileNames = StringUtils.split(fName, ",");
                String[] fileRealNames = StringUtils.split(frName, ",");
                if (fileNames != null && fileNames.length > 0) {
                    temp = sysAnnexService.add(link, linkId, annex, fileNames, fileRealNames);
                }
                if (temp == false) flag = false;
            }
        }
        return flag;
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

/*
 * @(#)SysAnnexUtil.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web-manage
 * 创建日期 : 2017年5月23日
 * 修改历史 : 
 *     1. [2017年5月23日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 附件上传工具，解析上传的文件名
 * @version 
 * @author 傅泉明
 */
public class SysAnnexUtil {
    /** 上传文件名 */
    public static final String fileNameKey = "fileNames";
    /** 上传文件真实名称 */
    public static final String fileRealNameKey = "fileRealNames";
    /**
     * 解析上传的文件名
     * @param linkType  关联类型
     * @param annexType 关联附件类型
     * @param request
     * @return
     */
    public static Map<String, String[]> getUploadFileName(String linkType, String annexType, HttpServletRequest request) {
        Map<String, String[]> map = Collections.emptyMap();
        // 附件
        String[] linkTypes = request.getParameterValues("linkType");// 附件关联类型
        String[] annexTypes = request.getParameterValues("annexType");// 附件类型
        if (linkType != null) {
            int length = linkTypes.length;
            for (int i = 0; i < length; i++) {
                String link = linkTypes[i];
                String annex = annexTypes[i];
                if (linkType.equals(link) && annexType.equals(annex)) {
                    StringBuilder fileNameSb = new StringBuilder();
                    fileNameSb.append(link).append("-").append(annex).append("-uploadFileName");
                    StringBuilder fileRealNameSb = new StringBuilder();
                    fileRealNameSb.append(link).append("-").append(annex).append("-uploadFileRealName");
                    String[] fileName = request.getParameterValues(fileNameSb.toString());// 提交的文件名
                    String[] fileRealName = request.getParameterValues(fileRealNameSb.toString());// 保存的文件名
                    if (fileName == null || fileName.length == 0) continue;
                    String fName = fileName[0];
                    String frName = fileRealName[0];
                    if (fName == null || fName.equals("")) continue;
                    String[] fileNames = StringUtils.split(fName, ",");
                    String[] fileRealNames = StringUtils.split(frName, ",");
                    if (fileNames != null && fileNames.length > 0) {
                        map = new HashMap<String, String[]>(fileNames.length);
                        map.put(fileNameKey, fileNames);
                        map.put(fileRealNameKey, fileRealNames);
                    }
                }
            }
        }
        return map;
    }
    
}

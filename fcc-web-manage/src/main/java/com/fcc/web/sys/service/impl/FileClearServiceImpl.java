/*
 * @(#)FileClearServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2017年3月7日
 * 修改历史 : 
 *     1. [2017年3月7日]创建文件 by 傅泉明
 */
package com.fcc.web.sys.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fcc.web.sys.config.ConfigUtil;
import com.fcc.web.sys.service.TaskService;

/**
 * 清除临时文件
 * @version 
 * @author 傅泉明
 */
@Service
public class FileClearServiceImpl implements TaskService {

    private Logger logger = Logger.getLogger(FileClearServiceImpl.class);
    
    @Override
    public void executeQueue() {
        // 上传的临时文件
        File file = new File(ConfigUtil.getUploadFileTempPath());
        if (file.exists()) {
            try {
                logger.info("清除临时文件:begin");
                FileUtils.cleanDirectory(file);
                logger.info("清除临时文件:end");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("清除临时文件:失败", e);
            }
        }
        // 导入导出的临时文件
        file = new File(ConfigUtil.getExcelImportFilePath());
        if (file.exists()) {
            try {
                logger.info("清除excelImport文件:begin");
                FileUtils.cleanDirectory(file);
                logger.info("清除excelImport文件:end");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("清除excelImport文件:失败", e);
            }
        }
        file = new File(ConfigUtil.getExcelExportFilePath());
        if (file.exists()) {
            try {
                logger.info("清除excelExport文件:begin");
                FileUtils.cleanDirectory(file);
                logger.info("清除excelExport文件:end");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("清除excelExport文件:失败", e);
            }
        }
    }

}

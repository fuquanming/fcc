/*
 * @(#)StatusCode.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月23日
 * 修改历史 : 
 *     1. [2016年12月23日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.common;

/**
 * 应用状态码
 * @version 
 * @author 傅泉明
 */
public interface StatusCode {
    /** 系统 */
    public interface Sys {
        /** 成功 sys_000 */
        String success = "sys_000";
        /** 失败 sys_001 */
        String fail = "sys_001";
        /** 修改记录时，ID为空 sys_002 */
        String emptyUpdateId = "sys_002";
        /** 删除记录时，ID为空 sys_003 */
        String emptyDeleteId = "sys_003";
        /** session过期 sys_004 */
        String sessionTimeout = "sys_004";
        /** 无权限 sys_005 */
        String noPermissions = "sys_005";
        /** 堆空间不足 sys_006 */
        String heapSpace = "sys_006";
    }
    
    public interface Treeable {
        /** 节点名称为空 treeable_000 */
        String emptyName = "treeable_000";
        /** 上级为空 treeable_001 */
        String emptyParent = "treeable_001";
        /** 不能修改根节点 treeable_002 */
        String errorRootId = "treeable_002";
        /** 修改的节点不存在 treeable_003 */
        String errorId = "treeable_003";
        /** 上级节点不能是自己！ treeable_004 */
        String errorParentOneself = "treeable_004";
		/** 编码重复 treeable_005 */
        String existCode = "treeable_005";
    }
    
    /** 上传文件 */
    public interface Import {
        /** 已上传成功！系统正在导入数据... import_000 */
        String importNow = "import_000";
        /** 系统正在执行上次导入数据，请稍后... import_001 */
        String importBusy = "import_001";
        /** 请选择上传文件 import_002 */
        String emptyFile = "import_002";
    }
    
    /** 导出文件 */
    public interface Export {
        /** 系统正在导出数据... export_000 */
        String exportNow = "export_000";
        /** 系统正在执行上次导出数据，请稍后... export_001 */
        String exportBusy = "export_001";
    }
}

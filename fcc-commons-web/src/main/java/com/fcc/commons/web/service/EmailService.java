/*
 * @(#)EmailService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月16日
 * 修改历史 : 
 *     1. [2016年12月16日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service;

import java.io.File;

/**
 * 发送邮件
 * @version 
 * @author 傅泉明
 */
public interface EmailService {
    /**
     * 邮件标题
     * @param subject
     */
    void setSubject(String subject);
    /**
     * 邮件内容
     * @param text
     */
    void setText(String text, boolean html);
    /**
     * 添加收件人地址
     * @param to
     */
    void addTo(String to);
    /**
     * 添加抄送人地址
     * @param cc
     */
    void addCc(String cc);
    /**
     * 添加暗送人地址
     * @param bcc
     */
    void addBcc(String bcc);
    /**
     * 添加附件
     * @param attachmentFilename
     * @param file
     */
    void addAttachment(String attachmentFilename, File file);
    /**
     * 嵌套图片的邮件
     * @param contentId
     * @param file
     */
    void addInline(String contentId, File file);
    /**
     * 发送邮件
     */
    void send();
}

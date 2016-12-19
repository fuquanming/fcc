/*
 * @(#)EmailServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月19日
 * 修改历史 : 
 *     1. [2016年12月19日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service.impl;

import java.io.File;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fcc.commons.web.service.EmailService;

/**
 * 邮件发送
 * @version 
 * @author 傅泉明
 */
@Service
public class EmailServiceImpl implements EmailService {

    private ThreadLocal<MimeMessageHelper> mimeMessageHelperLocal = new ThreadLocal<MimeMessageHelper>();
    
    @Resource
    private JavaMailSender javaMailSender;
    
    private MimeMessageHelper getMimeMessageHelper() {
        MimeMessageHelper messageHelper = mimeMessageHelperLocal.get();
        if (messageHelper == null) {
            // multipart模式 为true时发送附件 可以设置html格式
            try {
                messageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "utf-8");
                mimeMessageHelperLocal.set(messageHelper);
            } catch (MessagingException e) {
                e.printStackTrace();
            } 
        }
        return messageHelper;
    }
    
    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.EmailService#setSubject(java.lang.String)
     **/
    @Override
    public void setSubject(String subject) {
        try {
            getMimeMessageHelper().setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.EmailService#setText(java.lang.String)
     **/
    @Override
    public void setText(String text, boolean html) {
        try {
            getMimeMessageHelper().setText(text, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.EmailService#addTo(java.lang.String)
     **/
    @Override
    public void addTo(String to) {
        try {
            getMimeMessageHelper().addTo(to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.EmailService#addCc(java.lang.String)
     **/
    @Override
    public void addCc(String cc) {
        try {
            getMimeMessageHelper().addCc(cc);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.EmailService#addBcc(java.lang.String)
     **/
    @Override
    public void addBcc(String bcc) {
        try {
            getMimeMessageHelper().addBcc(bcc);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addAttachment(String attachmentFilename, File file) {
        try {
            getMimeMessageHelper().addAttachment(attachmentFilename, file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void addInline(String contentId, File file) {
        try {
            getMimeMessageHelper().addInline(contentId, file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void send() {
        try {
            getMimeMessageHelper().setFrom(((JavaMailSenderImpl)javaMailSender).getUsername());
            javaMailSender.send(getMimeMessageHelper().getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MimeMessageHelper messageHelper = mimeMessageHelperLocal.get();
            if (messageHelper != null) messageHelper = null;
            mimeMessageHelperLocal.set(null);
        }
    }
}

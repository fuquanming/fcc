/*
 * @(#)TemplateServiceImpl.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-web
 * 创建日期 : 2016年12月16日
 * 修改历史 : 
 *     1. [2016年12月16日]创建文件 by 傅泉明
 */
package com.fcc.commons.web.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fcc.commons.web.service.TemplateService;

import freemarker.template.Template;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;
    /**
     * //TODO 添加override说明
     * @see com.fcc.commons.web.service.TemplateService#getMailContent(java.lang.String, java.util.Map)
     **/
    @Override
    public String getMailContent(String templateName, Map<String, String> contentMap) throws Exception {
        String htmlText = "";
        //通过指定模板名获取FreeMarker模板实例     
        Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);//registerUser.html
//      Map<String, Object> map = new HashMap<String, Object>(); //FreeMarker通过Map传递动态数据
        //注意动态数据的key和模板标签中指定的属性相匹配
//      for (String c : content) {
//          map.put("content", content); 
//      }
        //解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。     
        htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, contentMap);
        return htmlText;
    }

}

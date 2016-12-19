package com.fcc.commons.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.fcc.commons.web.service.TemplateService;

public class EmailTemplateServiceImplTest {

    BeanFactory factory;
    
    public static BeanFactory getBeanFactory() {
        BeanFactory factory = new ClassPathXmlApplicationContext(
                new String[]{
                        "spring.xml"
                }
        );
        return factory;
    }
    
    @Test
    public void testGetMailContent() {
        
//        FileSystemResource
        MimeMessageHelper help = null;
//        help.addTo(to);
        
        
        factory = getBeanFactory();
        TemplateService service = factory.getBean(TemplateService.class);
        Map<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("userName", "张三");
        contentMap.put("content", "注册成功！");
        try {
            System.out.println(service.getMailContent("notify.html", contentMap));
            System.out.println("---------------");
            contentMap.put("userName", "李四");
            contentMap.put("content", "注册成功！");
            System.out.println(service.getMailContent("test.html", contentMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

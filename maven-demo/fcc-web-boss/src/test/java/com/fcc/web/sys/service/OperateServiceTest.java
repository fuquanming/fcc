package com.fcc.web.sys.service;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OperateServiceTest {
    
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
    public void testCache() {
        
    }

}

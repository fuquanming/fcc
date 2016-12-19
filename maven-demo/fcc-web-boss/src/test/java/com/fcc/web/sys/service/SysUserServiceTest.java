package com.fcc.web.sys.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SysUserServiceTest {

    BeanFactory factory;
    
    public static BeanFactory getBeanFactory() {
        BeanFactory factory = new ClassPathXmlApplicationContext(
                new String[]{
                        "spring.xml"
                }
        );
        return factory;
    }
    
    @Before
    public void initFactroy() {
//        factory = getBeanFactory(); 
    }
    
    @Test
    public void testUpdateStatus() {
//        fail("Not yet implemented");
        SysUserService sysUserService = getBeanFactory().getBean(SysUserService.class);
        sysUserService.editStatus(new String[]{"1"}, "1");
    }

}

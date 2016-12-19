package com.fcc.web.sys.service;

import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fcc.web.sys.model.Module;

public class CacheServiceTest {

    public static BeanFactory getBeanFactory() {
        BeanFactory factory = new ClassPathXmlApplicationContext(
                new String[]{
                        "spring.xml"
                }
        );
        return factory;
    }
    @Test
    public void testModuleMap() {
//        fail("Not yet implemented");
        CacheService cacheService = getBeanFactory().getBean(CacheService.class);
        Map<String, Module> map = cacheService.getModuleMap();
        TreeSet<Module> set = new TreeSet<Module>();
        set.addAll(map.values());
        
        for (Module module : set) {
            System.out.println(":" + module.getModuleId() + "," + module.getParentIds());
        }
        map = cacheService.getModuleMap();
        System.out.println("-----------");
    }

}

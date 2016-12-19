package com.fcc.web.sys.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fcc.web.sys.model.Module;
import com.fcc.web.sys.service.ModuleService;

public class ModuleServiceImplTest {

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
    public void testModuleCache() {
//        fail("Not yet implemented");
        BeanFactory beanFactory = getBeanFactory();
        
        
//        org.springframework.cache.ehcache.EhCacheCacheManager cache = (EhCacheCacheManager) beanFactory.getBean("cacheManager");
//        System.out.println("cache" + cache);
        System.out.println(beanFactory.getBean("cacheManager"));
//        System.out.println(beanFactory.getBean("lock"));
        
//        if (true) return;
        
        ModuleService moduleService = beanFactory.getBean(ModuleService.class);
        Module data = new Module();
        data.setModuleId("123");
//        data.setModuleName("123");
//        data.setModuleSort(1);
//        data.setModuleSort(1);
//        moduleService.edit(data, null);
        
        data = moduleService.getModuleById(data.getModuleId());
        System.out.println("sort=" + data.getModuleSort());
        data = moduleService.getModuleById(data.getModuleId());
        System.out.println("sort=" + data.getModuleSort());
        data.setModuleName("123");
        data.setModuleSort(data.getModuleSort() + 1);
        moduleService.edit(data, null);

        data = moduleService.getModuleById("123");
        System.out.println("sort=" + data.getModuleSort());
    }

}

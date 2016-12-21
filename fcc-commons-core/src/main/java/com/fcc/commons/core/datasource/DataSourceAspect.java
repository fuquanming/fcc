/*
 * @(#)DataSourceAspect.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-commons-core
 * 创建日期 : 2016年12月21日
 * 修改历史 : 
 *     1. [2016年12月21日]创建文件 by 傅泉明
 */
package com.fcc.commons.core.datasource;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定义数据源的AOP切面，通过该Service的事物是否只读判断是应该走读库还是写库 
 * @version 
 * @author 傅泉明
 */
//@Aspect
//@Component
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DataSourceAspect {

    private Logger logger = Logger.getLogger(DataSourceAspect.class);
    
//    @Pointcut("execution(* *..service*..*(..))")
    public void aspect() {
    }
    
//    @Before("aspect()")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String className = target.getClass().getName();
        String method = point.getSignature().getName();
        try {
            Method m = target.getClass().getMethod(method, ((MethodSignature) point.getSignature()).getMethod().getParameterTypes());
            String dataSourceKey = null;
            if (m != null && m.isAnnotationPresent(Transactional.class)) {
                if (m.getAnnotation(Transactional.class).readOnly()) {
                    // 只读
                    List<String> slaveList = DynamicDataSource.DATASOURCE_TYPE.get(DynamicDataSourceEnum.slave.getInfo());
                    int size = slaveList.size();
                    int index = 0;
                    if (size > 1) {
                        index = (int)(Math.random() * size);
                    } 
                    dataSourceKey = slaveList.get(index);
                    DynamicDataSourceHolder.setDataSourceKey(dataSourceKey);
                } else {
                    dataSourceKey = DynamicDataSource.DATASOURCE_TYPE.get(DynamicDataSourceEnum.master.getInfo()).get(0);
                    DynamicDataSourceHolder.setDataSourceKey(dataSourceKey);
                }
                logger.debug(className + "." + method + "(" + StringUtils.join(point.getArgs(), ",") + ")");
                logger.debug(Thread.currentThread().getId() + ":" + dataSourceKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

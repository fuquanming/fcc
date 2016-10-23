///*
// * @(#)ApplicationInitializer.java
// * 
// * Copyright (c) 2015 www.ssit-xm.com.cn, All Rights Reserved
// * 项目名称 : fcc-web
// * 创建日期 : 2016年10月23日
// * 修改历史 : 
// *     1. [2016年10月23日]创建文件 by 傅泉明
// */
//package com.fcc.commons.web.support.swagger;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletRegistration;
//
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.servlet.DispatcherServlet;
//
///**
// * 
// * @version 
// * @author 傅泉明
// */
//public class ApplicationInitializer implements WebApplicationInitializer {
//    @Override
//    public void onStartup(ServletContext container) {
//        System.out.println("ApplicationInitializer onStartup");
//        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet());
//        registration.setLoadOnStartup(1);
//        registration.addMapping("/*");
////        registration.addMapping("/swagger-ui.html");
////        registration.addMapping("/swagger-resources");
////        registration.addMapping("/swagger-resources/**");
////        registration.addMapping("/v2/api-docs");
////        registration.addMapping("/webjars/**");
//        
//    }
//}

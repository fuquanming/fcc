/*
 * @(#)BaseAction.java
 * 
 * Copyright (c) 2009 All Rights Reserved
 * 项目名称 : fcc-web-sys Maven Webapp
 * 创建日期 : 2016年10月16日
 * 修改历史 : 
 *     1. [2016年10月16日]创建文件 by fqm
 */
package com.fcc.commons.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcc.commons.web.util.ModelAndViewUtil;
import com.fcc.commons.web.view.Message;

/**
 * 控制器基类
 * @version v1.0
 * @author 傅泉明
 */
public abstract class BaseController {
    
    /**
     * 获取JacksonJsonView
     * @return
     */
    protected ModelAndView getModelAndView(Message message) {
        ModelAndView mav = new ModelAndView(ModelAndViewUtil.getMappingJacksonJsonView());
        mav.getModelMap().addAttribute(message);
        return mav;
    }

    /** 设置成功响应代码 */
    protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap) {
        return setSuccessModelMap(modelMap, null);
    }

    /** 设置成功响应代码 */
    protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap, Object data) {
        return setModelMap(modelMap, HttpStatus.OK, data);
    }

    /** 设置响应代码 */
    protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpStatus status) {
        return setModelMap(modelMap, status, null);
    }

    /** 设置响应代码 */
    protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, HttpStatus status, Object data) {
        modelMap.remove("void");
        if (data != null) {
            modelMap.put("data", data);
        }
        modelMap.put("httpCode", status.value());
        modelMap.put("msg", status.name());
        modelMap.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(modelMap);
    }

    /** 异常处理 */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws Exception {
        ModelMap modelMap = new ModelMap();
        if (ex instanceof IllegalArgumentException) {
            setModelMap(modelMap, HttpStatus.OK);
        } else {
            setModelMap(modelMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ex.printStackTrace();
        request.setAttribute("msg", ex.getCause());
        response.setContentType("application/json;charset=UTF-8");
        byte[] bytes = JSON.toJSONBytes(modelMap, SerializerFeature.DisableCircularReferenceDetect);
        response.getOutputStream().write(bytes);
    }
    
}

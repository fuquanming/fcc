package com.fcc.commons.web.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcc.commons.web.view.Message;

/**
 * <p>Description:ModelAndView拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public abstract class BaseInterceptor implements HandlerInterceptor {

    /** 跳转页面地址 */
	protected String goPage;
	
    private static final String FILTER_MSG = "filterMsg";
    /**
     * 拦截器输出
     * @param request
     * @param response
     * @param handlerMethod         拦截的对象
     * @param message               json输出
     * @param fileterMsg            跳转页面的信息
     * @param goPage                跳转的页面
     * @return boolean              过滤到返回true，没过滤到返回false
     * @throws ServletException
     * @throws IOException
     */
    public static boolean output(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod
            , Message message, Object fileterMsg, String goPage) throws ServletException, IOException  {
        if (handlerMethod.getMethod().isAnnotationPresent(ResponseBody.class)
                || handlerMethod.getMethod().getReturnType() == ModelAndView.class) {// json
            response.setContentType("application/json;charset=UTF-8");
            byte[] bytes = JSON.toJSONBytes(message, SerializerFeature.DisableCircularReferenceDetect);
            response.getOutputStream().write(bytes);
            return true;
        } else if (handlerMethod.getMethod().getReturnType() == String.class) {// 跳转页面
            request.getSession().setAttribute(FILTER_MSG, fileterMsg);// 记录跳转页面的来源
            request.getRequestDispatcher(goPage).forward(request, response);
            return true;
        }
        return false;
    }
    /**
     * 跳转页面
     * @param request
     * @param response
     * @param handlerMethod
     * @param fileterMsg            跳转页面记录信息
     * @param goPage                跳转页面
     * @throws ServletException
     * @throws IOException
     */
    public static void goPage(HttpServletRequest request, HttpServletResponse response
            , Object fileterMsg, String goPage) throws ServletException, IOException  {
        request.getSession().setAttribute(FILTER_MSG, fileterMsg);// 记录跳转页面的来源
        request.getRequestDispatcher(goPage).forward(request, response);
    }
    
    public String getGoPage() {
        return goPage;
    }
    public void setGoPage(String goPage) {
        this.goPage = goPage;
    }

}

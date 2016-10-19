package com.fcc.web.sys.interceptor;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.view.Message;

/**
 * <p>Description:ModelAndView拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ModelAndViewInterceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) return;
		String messageKey = "message";
		Message message = (Message) modelAndView.getModelMap().get(messageKey);
		if (message == null) return;
		// 使用ModuleAndView json 返回数据 public ModelAndView edit(Role role, HttpServletRequest request)
		// 会将 role 作为返回值输出，删除 返回为message 的其他值
		Iterator<String> keyIt = modelAndView.getModel().keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			if (!key.equals(messageKey)) keyIt.remove();
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		return true;
	}

}

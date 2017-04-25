package com.fcc.commons.web.util;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 * <p>Description:spring ModelAndView工具类</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class ModelAndViewUtil {

	/**
	 * 返回JsonView
	 * 如：{"message":{"id":null,"msg":"ok","obj":null,"success":true,"module":null,"operate":null}}
	 * 将返回 {"id":null,"msg":"ok","obj":null,"success":true,"module":null,"operate":null}
	 * @return
	 */
	public static View getMappingJacksonJsonView() {
	    MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setContentType("text/html;charset=UTF-8");
		// 只有一个对象，不返回对象名称
		view.setExtractValueFromSingleKeyModel(true);
		return view;
	}
	
	/**
	 * 返回JsonView
	 * @return
	 */
	public static View getXmlView() {
		MarshallingView view = new MarshallingView();
		view.setContentType("text/html;charset=UTF-8");
		return view;
	}
	
}

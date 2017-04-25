package com.fcc.commons.web.interceptor;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fcc.commons.web.common.StatusCode;
import com.fcc.commons.web.view.Message;

/**
 * <p>Description:Memory拦截器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class MemoryInterceptor extends BaseInterceptor {

    private Logger logger = Logger.getLogger(MemoryInterceptor.class);
    
    private int kb = 1024;
    /** 是否出现堆内存不足 */
    private boolean heapSpace = false;
    // 最大可使用内存
    private long maxMemory = 0;
    
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
	    if (object instanceof HandlerMethod) {
	        HandlerMethod handlerMethod = (HandlerMethod) object;
	        // 可使用内存
//	        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
	        // 剩余内存
	        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
	        // 最大可使用内存
	        if (maxMemory == 0) maxMemory = Runtime.getRuntime().maxMemory() / kb;
	        // 是否memory
	        boolean flag = false;
	        // 可用内存百分比
	        if (((float)freeMemory / (float)maxMemory) < 0.03) {
	            heapSpace = true;
	            flag = true;
	        }
	        // 上传文件
	        long totalFileSize = 0;
	        if (request instanceof MultipartHttpServletRequest) {
	            MultipartHttpServletRequest multipartRequest = ((MultipartHttpServletRequest) request);
	            for (Iterator<String> iterator = multipartRequest.getFileNames(); iterator.hasNext();) {
	                String fileName = iterator.next();
	                List<MultipartFile> fileList = multipartRequest.getFiles(fileName);
	                for (MultipartFile file : fileList) {
	                    if (file != null && file.isEmpty() == false) {
	                        totalFileSize += file.getSize();
	                    }
	                }
                }
	            totalFileSize = totalFileSize / kb;
                if (heapSpace) {// 16
                    if (freeMemory < totalFileSize * 16) {
                        flag = true;
                    }
                } else {// 2
                    if (freeMemory < totalFileSize * 2) {
                        flag = true;
                    }
                }
	        }
//	        flag = true;
	        if (flag) {
	            String info = "freeMemory=%d,maxMemory=%d,totalFileSize=%d,%f";
	            logger.info(String.format(info, freeMemory, maxMemory, totalFileSize, ((float)freeMemory / (float)maxMemory)));
	            Runtime.getRuntime().gc();
	            Message message = new Message();
	            message.setMsg(StatusCode.Sys.heapSpace);
	            output(request, response, handlerMethod, message, "heapSpace", goPage);
	            return false;
	        }
        }
		return true;
	}
}

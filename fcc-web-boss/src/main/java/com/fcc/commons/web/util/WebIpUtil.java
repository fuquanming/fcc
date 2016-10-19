package com.fcc.commons.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @version v1.0
 * @author 傅泉明
 */
public final class WebIpUtil {

    /**
     * 私有构造方法.
     */
    private WebIpUtil() {
    }

    /**
     * 获取客户端的真实IP地址.<br/>
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，那么取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * @param request .
     * @return IP.
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip) && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")); // 截取第一个
        }
        return ip;
    }

}

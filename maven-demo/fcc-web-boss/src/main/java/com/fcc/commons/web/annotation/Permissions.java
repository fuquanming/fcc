package com.fcc.commons.web.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 方法鉴权
 * 
 * @version 
 * @author 傅泉明
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Permissions {

    String[] value();
    
}

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
    /**
     * 确定用户是否被允许调用受此注释保护的代码
     * @return
     */
    String[] value();
    /**
     * 如果指定了多个角色，则进行权限检查的逻辑操作。 AND是默认值
     * @return
     */
    Logical logical() default Logical.AND;
}

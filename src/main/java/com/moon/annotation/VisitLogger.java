package com.moon.annotation;

import java.lang.annotation.*;

/**
 * 访问日志注解
 *
 * @author:Y.0
 * @date:2023/10/13
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLogger {

    /**
     * @return 访问页面
     */
    String value() default "";

}

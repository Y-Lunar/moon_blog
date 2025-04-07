package com.moon.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author:Y.0
 * @date:2023/10/13
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLogger {

    /**
     * @return 操作描述
     */
    String value() default "";

}

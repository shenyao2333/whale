package com.whale.provider.security.annotation;

import java.lang.annotation.*;

/**
 * @author shenyao
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**
     * 表的别名
     */
    String tableAlias() default "";

}


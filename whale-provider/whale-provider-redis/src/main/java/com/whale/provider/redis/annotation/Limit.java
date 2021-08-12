package com.whale.provider.redis.annotation;





import com.whale.provider.redis.constant.LimitType;

import java.lang.annotation.*;

/**
 * @Author: sy
 * @Date: Created by 2021/5/21 11:04
 * @description: 限流注解
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {


    /**
     * key
     */
    String key() default "";


    /**
     * 给定的时间范围 单位(秒)
     */
    int period();

    /**
     * 一定时间内最多访问次数
     */
    int count();

    /**
     * 限流的类型(用户自定义key 或者 请求ip)
     */
    LimitType limitType() default LimitType.KEY;



}

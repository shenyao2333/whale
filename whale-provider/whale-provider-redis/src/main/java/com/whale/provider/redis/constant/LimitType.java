package com.whale.provider.redis.constant;

/**
 * @Author: sy
 * @Date: Created by 2021/5/21 11:07
 * @description: 限流类型
 */
public enum LimitType {
    /**
     * 自定义key
     */
    KEY,

    /**
     * 请求者IP
     */
    IP;
}

package com.whale.generator.netty.controller;

import com.whale.provider.basices.redis.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 22:05
 * @description:
 */
@RestController
public class TestController {

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/test")
    public void test(){
        Object dsf = redisUtil.get("dsf");
        System.out.println(dsf);
    }


}

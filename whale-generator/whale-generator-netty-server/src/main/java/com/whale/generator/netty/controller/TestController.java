package com.whale.generator.netty.controller;

import com.whale.generator.netty.common.domain.MessageInfo;
import com.whale.generator.netty.common.service.MessageInfoService;
import com.whale.provider.basices.redis.RedisUtil;
import org.springframework.cache.annotation.Cacheable;
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
    @Resource
    private MessageInfoService messageInfoService;

    @GetMapping("/test")
    public void test(){
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(":--");
        messageInfoService.save(messageInfo);
        System.out.println(messageInfo.getId());
    }


    @GetMapping("/test2")
    @Cacheable(value = "test#12" ,key = "1")
    public void sdf(){
        System.out.println("进来了");
    }



}

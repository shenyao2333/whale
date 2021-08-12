package com.whale.generator.netty.controller;

import com.whale.generator.netty.common.domain.MessageInfo;
import com.whale.generator.netty.common.service.FriendService;
import com.whale.generator.netty.common.service.MessageInfoService;
import com.whale.provider.basices.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 22:05
 * @description:
 */
@RestController
@AllArgsConstructor
public class TestController {


    private final  RedisUtil redisUtil;
    private final MessageInfoService messageInfoService;
    private final FriendService friendService;

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


    @GetMapping("/test3")
    public Object friendIdByUserId(){
        Boolean friend = friendService.isFriend(3, 1);
       // Set<Integer> friendIdByUserId = friendService.getFriendIdByUserId(3);
        return friend;
    }

}

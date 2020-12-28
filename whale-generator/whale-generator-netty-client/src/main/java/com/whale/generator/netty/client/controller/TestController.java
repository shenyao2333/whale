package com.whale.generator.netty.client.controller;

import com.whale.generator.netty.client.config.NettyClient;
import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.common.constant.SysConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sy
 * @date Created in 2020.10.21 22:11
 * @description
 */
@RestController
@RequestMapping("/msg")
public class TestController {

    @Resource
    private NettyClient nettyClient;
    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/send")
    public void send(String content){
        Msg.Base msg = new Msg.Base().toBuilder()
                .setCmd(Cmd.Command.NORMAL)
                .setSendUserId("3")
                .setAccepterId("1")
                .setSendTime(System.currentTimeMillis())
                .setContent(content).build();
        nettyClient.sendMsg(msg);
    }


    @GetMapping("/sendAuth")
    public void sendAuth(String content){
        Msg.Base msg = new Msg.Base().toBuilder()
                .setToken("ca2830d3-59f2-4fe0-ba18-0eaef252e987")
                .setCmd(Cmd.Command.AUTH)
                .setContent("登录请求")
                .setSendUserId("3")
                .setSendTime(System.currentTimeMillis())
                .build();
        nettyClient.sendMsg(msg);
    }

    @GetMapping("/test")
    public void saf(){
        String token = "63e5c835-ccc6-41c4-a032-f2a8d007ac51";
        boolean b = redisUtil.hasKey(SysConstant.tokenBegin + token);
        System.out.println(b);

    }


}

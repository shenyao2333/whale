package com.whale.generator.netty.client.controller;

import com.whale.generator.netty.client.config.NettyClient;
import com.whale.generator.netty.common.protocol.Command;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.common.constant.SysConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import com.whale.generator.netty.common.protocol.MsgBase;
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
        MsgBase.Msg msg = new MsgBase.Msg().toBuilder()
                .setCmd(Command.CommandType.NORMAL)
                .setSendUserId("654321")
                .setAccepterId("123456")
                .setSendTime(System.currentTimeMillis())
                .setContent(content).build();
        nettyClient.sendMsg(msg);
    }


    @GetMapping("/sendAuth")
    public void sendAuth(String content){
        MsgBase.Msg msg = new MsgBase.Msg().toBuilder()
                .setToken("b2cd3ed9-6a86-4336-8da9-ab343a05cf58")
                .setCmd(Command.CommandType.AUTH)
                .setContent("登录请求")
                .setSendUserId("654321")
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

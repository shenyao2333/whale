package com.whale.generator.netty.client.controller;

import com.whale.generator.netty.client.config.NettyClient;
import com.whale.generator.netty.common.protocol.Message;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/send")
    public void send(String content){
        Message.Msg msg = new Message.Msg().toBuilder().setContent(content).build();
        nettyClient.sendMsg(msg);
    }


}

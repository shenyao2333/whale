package com.whale.generator.netty.service;

import com.whale.generator.netty.common.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/10 21:50
 * @description: 认证连接
 */
public class LoginAuthRespHandler extends SimpleChannelInboundHandler {

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<String,Boolean>();
    private String[] whitekList =  {"127.0.0.1","192.168.1.104"};

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Message message =  (Message)msg;

    }
}

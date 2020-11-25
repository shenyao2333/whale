package com.whale.generator.netty.service;

import com.google.protobuf.Parser;
import com.whale.generator.netty.common.protocol.Message;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.common.constant.SysConstant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 20:36
 * @description: 认证处理
 */
@ChannelHandler.Sharable
@Slf4j
public class AuthServerHandler  extends ChannelInboundHandlerAdapter{

    @Resource
    private RedisUtil redisUtil;




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        Message.Msg msg = (Message.Msg)message;
        if (msg.getMsgType().equals(Message.Msg.MessageType.AUTH)){

            log.info("认证消息：{}", msg.getContent());
            String token = msg.getToken();
            Object o = redisUtil.get(SysConstant.tokenBegin + token);
            if (o==null){

            }



            Message.Msg msgs = new Message.Msg().toBuilder().setContent("收到收到，请保持联络！").build();
            ctx.writeAndFlush(msgs);
            Parser<Message.Msg> parserForType = msg.getParserForType();
            String s = parserForType.toString();

        }
    }

}

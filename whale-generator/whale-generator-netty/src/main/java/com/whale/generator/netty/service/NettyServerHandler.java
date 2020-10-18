package com.whale.generator.netty.service;

import com.whale.generator.netty.constant.MsgTypeConstants;
import com.whale.generator.netty.protocol.protobuf.MessageBase;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sy
 * @date Created in 2020.10.18 16:34
 * @description
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageBase.Message> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageBase.Message message) throws Exception {

        if (message.getCmd().equals(MessageBase.Message.CommandType.HEARTBEAT_REQUEST)){
            log.info("收到客户端发来的心跳消息：{}", message.toString());
            channelHandlerContext.writeAndFlush(MsgTypeConstants.HEARTBEAT_REQUEST);
        }else if (message.getCmd().equals(MessageBase.Message.CommandType.NORMAL)) {
            log.info("收到客户端的业务消息：{}",message.toString());
        }

    }
}

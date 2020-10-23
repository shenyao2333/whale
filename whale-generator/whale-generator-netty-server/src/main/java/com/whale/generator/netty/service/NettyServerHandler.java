package com.whale.generator.netty.service;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.whale.generator.netty.common.protocol.Message;
import com.whale.generator.netty.constant.MsgTypeConstants;
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
public class NettyServerHandler extends SimpleChannelInboundHandler<Message.Msg> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message.Msg msg ) throws Exception {
        if (msg.getMsgType().equals(Message.Msg.MessageType.HEARTBEAT_REQUEST)){
            log.info("收到客户端发来的心跳消息：{}", msg.toString());
            Message.Msg msgs = new Message.Msg().toBuilder().setContent("收到收到，请保持联络！").build();
            channelHandlerContext.writeAndFlush(msgs);
        }else if (msg.getMsgType().equals(Message.Msg.MessageType.NORMAL)) {
            log.info("收到客户端的业务消息：{}",msg.toString());
            Message.Msg build = Message.Msg.newBuilder().setContent("消息已收到，我们会尽快处理的！").build();
            channelHandlerContext.writeAndFlush(build);
        }else {
            log.error("未知信息"+msg.toString());
        }

    }


}

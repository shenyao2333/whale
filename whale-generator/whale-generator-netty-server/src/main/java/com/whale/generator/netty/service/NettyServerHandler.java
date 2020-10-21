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
        if (msg.getMsgType().equals(MsgTypeConstants.HEARTBEAT_REQUEST)){
            log.info("收到客户端发来的心跳消息：{}", msg.toString());
            channelHandlerContext.writeAndFlush(MsgTypeConstants.HEARTBEAT_REQUEST);
        }else if (msg.getMsgType().equals(MsgTypeConstants.NORMAL)) {
            log.info("收到客户端的业务消息：{}",msg.toString());
        }else {
            log.error("未知信息"+msg.toString());
        }

    }


}

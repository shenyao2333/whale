package com.whale.generator.netty.client.config;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author sy
 * @date: 2020/10/19 10:40
 * @description
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageBase.Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageBase.Message message) throws Exception {

    }
}

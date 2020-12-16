package com.whale.generator.netty.client.config;

import com.whale.generator.netty.common.protocol.MsgBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sy
 * @date: 2020/10/19 10:40
 * @description
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<MsgBase.Msg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgBase.Msg msg) throws Exception {
        log.info("客户端收到消息：{}-- , {}",msg.getContent(),msg.getMsgId());
    }


    /**
     * 处理异常, 一般将实现异常处理逻辑的Handler放在ChannelPipeline的最后
     * 这样确保所有入站消息都总是被处理，无论它们发生在什么位置，下面只是简单的关闭Channel并打印异常信息
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

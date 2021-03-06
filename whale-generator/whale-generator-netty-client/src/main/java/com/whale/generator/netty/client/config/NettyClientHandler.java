package com.whale.generator.netty.client.config;

import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sy
 * @date: 2020/10/19 10:40
 * @description
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Msg.Base> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Msg.Base msg) throws Exception {
        if (msg.getCmd() != Cmd.Command.HEARTBEAT_RESPONSE){
            System.out.println(msg.getCmd() );
            String cMsgId = msg.getCMsgId();
            log.info("客户端收到消息：内容：{}, id：{}，cid：{}",msg.getContent(),msg.getMsgId(),cMsgId);
        }
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

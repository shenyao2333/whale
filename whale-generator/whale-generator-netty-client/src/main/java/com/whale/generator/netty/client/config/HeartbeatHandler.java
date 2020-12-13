package com.whale.generator.netty.client.config;

import com.whale.generator.netty.common.protocol.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.whale.generator.netty.common.protocol.MsgBase;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 20:36
 * @description: 客户端心跳
 */
@Slf4j
@Component
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {


    @Resource
    private NettyClient nettyClient;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("已经60s未发生读写操作，发送心跳消息");
                //发送心跳消息，并在发送失败时关闭该连接
                ctx.writeAndFlush(new MsgBase.Msg().toBuilder().setSendTime(System.currentTimeMillis()).setContent("心跳消息").setCmd(Command.CommandType.HEARTBEAT_REQUEST).build());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("服务连接失败---》");
        //如果运行过程中服务端挂了,执行重连机制
        //如果运行过程中服务端挂了,执行重连机制
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> nettyClient.start(), 10L, TimeUnit.SECONDS);
        super.channelInactive(ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("捕获的异常：{}",cause.getMessage());
        ctx.channel().close();
    }
}

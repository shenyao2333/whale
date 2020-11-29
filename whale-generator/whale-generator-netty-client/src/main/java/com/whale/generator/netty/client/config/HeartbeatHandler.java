package com.whale.generator.netty.client.config;

import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.MsgBase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author pjmike
 * @create 2018-10-25 17:15
 */
@Component
@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {





    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("已经10s没有发送消息给服务端");
                //向服务端送心跳包
                MsgBase.Msg msg = new MsgBase.Msg().toBuilder().setSendTime(System.currentTimeMillis()).setContent("心跳消息").setCmd(Command.CommandType.HEARTBEAT_REQUEST).build();
                //发送心跳消息，并在发送失败时关闭该连接
                ctx.writeAndFlush(msg);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("服务连接失败---》");
        //如果运行过程中服务端挂了,执行重连机制
       // EventLoop eventLoop = ctx.channel().eventLoop();
        //eventLoop.schedule(() -> nettyClient.start(), 10L, TimeUnit.SECONDS);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("捕获的异常：{}",cause.getMessage());
        ctx.channel().close();
    }
}

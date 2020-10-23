package com.whale.generator.netty.service;

import com.whale.generator.netty.common.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author sy
 * @date Created in 2020.10.18 17:04
 * @description
 */
@Slf4j
public class ServerIdleStateHandler extends IdleStateHandler {
    /**
     * 设置空闲检测时间为 30s
     */
    private static final int READER_IDLE_TIME = 300;

    public ServerIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接加入---->"+ctx.name());
        Message.Msg msg = new Message.Msg().toBuilder().setContent("123").build();
        ctx.writeAndFlush(msg);
        super.channelActive(ctx);
    }



    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        Message.Msg msg = new Message.Msg().toBuilder().setContent("123").build();
        ctx.writeAndFlush(msg);
        log.info("{} 秒内没有读取到数据,关闭连接", READER_IDLE_TIME);
        ctx.channel().close();
    }
}

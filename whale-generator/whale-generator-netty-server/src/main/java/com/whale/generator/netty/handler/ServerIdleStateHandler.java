package com.whale.generator.netty.handler;

import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.utils.MsgUtil;
import io.netty.channel.Channel;
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
     * 设置空闲检测时间为 300s
     */
    private static final int READER_IDLE_TIME = 300;




    public ServerIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    /**
     * 建立通道后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接加入---->"+ctx.channel().remoteAddress());
        Msg.Base msg = MsgUtil.sysMsg("连接成功！但需要认证成功才能通信。");
        ctx.writeAndFlush(msg);
    }


    /**
     * 心跳检测调用
     * @param ctx
     * @param evt 事件： 未发生读，  未发生写 ，  未发生读写
     * @throws Exception
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        Msg.Base msg = MsgUtil.sysMsg("常时间未发送消息，已关闭连接，如需使用请重新连接！");
        ctx.writeAndFlush(msg);
        Channel channel = ctx.channel();
        String userId = ChannelManage.getUserId(channel);
        if (userId !=null){
            ChannelManage.undock(userId);
        }
        log.info("{} 秒内没有读取到数据,关闭连接", READER_IDLE_TIME);
        ctx.channel().close();

    }

    /**
     * 断开连接后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info(channel.remoteAddress() +" 下线了");
    }




}

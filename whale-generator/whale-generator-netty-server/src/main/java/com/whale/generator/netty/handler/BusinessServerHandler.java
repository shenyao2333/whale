package com.whale.generator.netty.handler;

import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.MsgBase;
import com.whale.generator.netty.common.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author sy
 * @date Created in 2020.10.18 16:34
 * @description
 */
@ChannelHandler.Sharable
@Slf4j
public class BusinessServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {
        MsgBase.Msg msg = (MsgBase.Msg)msgObj;
        Channel channel = ctx.channel();
        if (!ChannelManage.hasUser(channel)){
            log.error("未登录信息。。。");
            MsgBase.Msg restMsg = MsgUtil.sysMsg("请先登录后才能发送消息！");
            ctx.writeAndFlush(restMsg);
            return;
        }

        if (msg.getCmd()== Command.CommandType.NORMAL){

        }



    }

}

package com.whale.generator.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.CommandNormal;
import com.whale.generator.netty.common.utils.MsgUtil;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.common.constant.SysConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.whale.generator.netty.common.protocol.MsgBase;
import javax.annotation.Resource;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 20:36
 * @description: 认证处理
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class AuthServerHandler extends ChannelInboundHandlerAdapter{

    @Resource
    private RedisUtil redisUtil;



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        MsgBase.Msg msg = (MsgBase.Msg)message;
        log.info("收到信息->"+ msg.getContent());
        if (msg.getCmd().equals(Command.CommandType.AUTH)){
            log.info("认证消息：{}", msg.getContent());
            String token = msg.getToken();
            if (StrUtil.isBlank(token)||!redisUtil.hasKey(SysConstant.tokenBegin + token)){
                MsgBase.Msg build = new MsgBase.Msg().toBuilder()
                        .setContent("请先进行登录！")
                        .setCmd(Command.CommandType.SYSTEM)
                        .setSendTime(System.currentTimeMillis())
                        .build();
                ctx.writeAndFlush(build);
                return;
            }
            ChannelManage.online(ctx.channel(),msg.getSendUserId());
            MsgBase.Msg backMsg = MsgUtil.sysMsg("连接成功");
            ctx.writeAndFlush(backMsg);
        }

        Channel channel = ctx.channel();
        if (!ChannelManage.hasUser(channel)){
            log.error("未登录信息。。。");
            MsgBase.Msg restMsg = MsgUtil.sysMsg("请先登录后才能发送消息！");
            ctx.writeAndFlush(restMsg);
            ctx.close();
            return;
        }
        ctx.fireChannelRead(message);
    }

}

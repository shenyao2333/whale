package com.whale.generator.netty.handler;

import cn.hutool.core.lang.Snowflake;
import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.FriendApplyService;
import com.whale.generator.netty.common.utils.MsgUtil;
import com.whale.provider.common.utils.SnowflakeId;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/3 21:08
 * @description: 好友申请处理器
 */
@RequiredArgsConstructor
public class FriendApplyHandler extends ChannelInboundHandlerAdapter {


    private final FriendApplyService friendApplyService;


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {
        Msg.Base msg = (Msg.Base)msgObj;
        Channel channel = ctx.channel();
        String sendUserId = ChannelManage.getUserId(channel);
        String accepterId = msg.getAccepterId();
        Boolean line = ChannelManage.isLine(accepterId);
        if (msg.getCmd().equals(Cmd.Command.BULL_SPONSOR)){
            Long msgId = SnowflakeId.getId();
            String status = "-";
            if (line){
                status="1";
                channel.writeAndFlush(MsgUtil.friendSponsorMsg(msgId,msg.getContent(),msg.getCMsgId(),sendUserId,accepterId));
            };
            friendApplyService.sponsor(msg,msgId,status, sendUserId);
        }
        // 添加结果通知
        else if (msg.getCmd().equals(Cmd.Command.BULL_RESULT)) {
            String msgId = msg.getMsgId();
            String content = msg.getContent();
            if (line){
                MsgUtil.friendResult(msgId,content,msg.getMsgStatus());
                friendApplyService.result(msg);
            }
        }
    }




}

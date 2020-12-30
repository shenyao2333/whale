package com.whale.generator.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.BusinessMsgService;
import com.whale.generator.netty.common.service.ChangeMsgService;
import com.whale.generator.netty.common.service.MessageUnreadStatusService;
import com.whale.generator.netty.common.utils.MsgUtil;
import com.whale.provider.common.utils.SnowflakeId;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sy
 * @date Created in 2020.10.18 16:34
 * @description
 */
@Slf4j
@ChannelHandler.Sharable
@Component
@RequiredArgsConstructor
public class ChangeServerHandler extends ChannelInboundHandlerAdapter {

    private final BusinessMsgService businessMsgService;
    private final ChangeMsgService changeMsgService;
    private final MessageUnreadStatusService messageUnreadStatusService;


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {
        Msg.Base msg = (Msg.Base)msgObj;
        String sendUserId = msg.getSendUserId();
        String accepterId = msg.getAccepterId();
        Boolean line = ChannelManage.isLine(accepterId);
        if (msg.getCmd()== Cmd.Command.MESSAGE_CHANGE){
            String msgId = msg.getMsgId();
            if (StrUtil.isBlank(msgId)){
                this.errorReply(ctx,"消息id信息有误！");
                return;
            }
            Msg.StatusType msgStatus = msg.getMsgStatus();
            if (line){
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                Msg.Base sendMsg = MsgUtil.changeMsg(sendUserId, accepterId, msgId, msgStatus);
                sendChanel.writeAndFlush(sendMsg);
            }else {
                messageUnreadStatusService.savaData(msg);
            }
            changeMsgService.updateStatus(msg);
        }
    }

    protected void errorReply(ChannelHandlerContext ctx,String content){
        Msg.Base backMsg = MsgUtil.sysMsg(content);
        ctx.channel().writeAndFlush(backMsg);
    }

}

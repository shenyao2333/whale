package com.whale.generator.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.whale.generator.netty.common.protocol.Cmd;
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

import javax.annotation.Resource;
import com.whale.generator.netty.common.protocol.Msg;
/**
 * @author sy
 * @date Created in 2020.10.18 16:34
 * @description
 */
@Slf4j
@ChannelHandler.Sharable
@Component
@RequiredArgsConstructor
public class BusinessServerHandler extends ChannelInboundHandlerAdapter {

    private final BusinessMsgService businessMsgService;
    private final ChangeMsgService changeMsgService;

    private final MessageUnreadStatusService messageUnreadStatusService;


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {
        Msg.Base msg = (Msg.Base)msgObj;

        //普通消息转发
        if (msg.getCmd()== Cmd.Command.NORMAL){
            log.info("收到转发信息-->"+msg.getContent());
            String sendUserId = msg.getSendUserId();
            String content = msg.getContent();
            String accepterId = msg.getAccepterId();
            Boolean line = ChannelManage.isLine(accepterId);
            if(StrUtil.isBlank(accepterId)){
                this.errorReply(ctx,"消息接收人不能为空！");
                return;
            }
            String status = "-";
            Long id = SnowflakeId.getId();
            if (line){
                status = "0";
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                Msg.Base sendMsg = MsgUtil.forwardMsg(id,sendUserId, accepterId, content);
                sendChanel.writeAndFlush(sendMsg);
            }
            Long aLong = businessMsgService.saveMsg(id,msg,status);
            Msg.Base backMsg = MsgUtil.sysMsg(aLong ,msg.getCMsgId() );
            ctx.channel().writeAndFlush(backMsg);
            return;
        }
        ctx.fireChannelRead(msgObj);
    }


    protected boolean asdf(){

    }

    protected void errorReply(ChannelHandlerContext ctx,String content){
        Msg.Base backMsg = MsgUtil.sysMsg(content);
        ctx.channel().writeAndFlush(backMsg);
    }

}

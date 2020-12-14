package com.whale.generator.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.MsgStatus;
import com.whale.generator.netty.common.service.BusinessMsgService;
import com.whale.generator.netty.common.service.ChangeMsgService;
import com.whale.generator.netty.common.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.whale.generator.netty.common.protocol.MsgBase;
import javax.annotation.Resource;

/**
 * @author sy
 * @date Created in 2020.10.18 16:34
 * @description
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class BusinessServerHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private BusinessMsgService businessMsgService;
    @Resource
    private ChangeMsgService changeMsgService;


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {

        MsgBase.Msg msg = (MsgBase.Msg)msgObj;
        log.info("2收到信息-->"+msg.getContent());
        Channel channel = ctx.channel();
        if (!ChannelManage.hasUser(channel)){
            log.error("未登录信息。。。");
            MsgBase.Msg restMsg = MsgUtil.sysMsg("请先登录后才能发送消息！");
            ctx.writeAndFlush(restMsg);
            return;
        }
        String sendUserId = msg.getSendUserId();
        String content = msg.getContent();
        String accepterId = msg.getAccepterId();
        Boolean line = ChannelManage.isLine(accepterId);
        if (msg.getCmd()== Command.CommandType.NORMAL){
            Integer msgId = businessMsgService.saveMsg(msg);
            if (line){
                if(StrUtil.isBlank(accepterId)){
                    MsgBase.Msg backMsg = MsgUtil.sysMsg("消息接收人不能为空！");
                    ctx.channel().writeAndFlush(backMsg);
                    return;
                }
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                MsgBase.Msg sendMsg = MsgUtil.forwardMsg(sendUserId, accepterId, content);
                sendChanel.writeAndFlush(sendMsg);
            }
            MsgBase.Msg backMsg = MsgUtil.sysMsg(msgId + "",msg.getMsgId() );
            ctx.channel().writeAndFlush(backMsg);
        }else if (msg.getCmd()== Command.CommandType.MESSAGE_CHANGE){
            String msgId = msg.getMsgId();
            if (StrUtil.isBlank(msgId)){
                MsgBase.Msg errMsg = MsgUtil.sysMsg("消息id信息有误！");
                ctx.channel().writeAndFlush(errMsg);
                return;
            }
            MsgStatus.StatusType msgStatus = msg.getMsgStatus();
            if (line){
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                MsgBase.Msg sendMsg = MsgUtil.changeMsg(sendUserId, accepterId, msgId, msgStatus);
                sendChanel.writeAndFlush(sendMsg);
            }
            changeMsgService.updateStatus(msg);
        }



    }

}

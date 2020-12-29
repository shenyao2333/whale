package com.whale.generator.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.service.BusinessMsgService;
import com.whale.generator.netty.common.service.ChangeMsgService;
import com.whale.generator.netty.common.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
public class BusinessServerHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private BusinessMsgService businessMsgService;
    @Resource
    private ChangeMsgService changeMsgService;


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msgObj ) throws Exception {
        Msg.Base msg = (Msg.Base)msgObj;
        log.info("收到信息-->"+msg.getContent());
        String sendUserId = msg.getSendUserId();
        String content = msg.getContent();
        String accepterId = msg.getAccepterId();

        //普通消息转发
        if (msg.getCmd()== Cmd.Command.NORMAL){
            Boolean line = ChannelManage.isLine(accepterId);

            if (line){
                if(StrUtil.isBlank(accepterId)){
                    Msg.Base backMsg = MsgUtil.sysMsg("消息接收人不能为空！");
                    ctx.channel().writeAndFlush(backMsg);
                    return;
                }
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                Msg.Base sendMsg = MsgUtil.forwardMsg(sendUserId, accepterId, content);
                sendChanel.writeAndFlush(sendMsg);
            }
            Integer msgId = businessMsgService.saveMsg(msg);
            Msg.Base backMsg = MsgUtil.sysMsg(msgId + "",msg.getMsgId() );
            ctx.channel().writeAndFlush(backMsg);
        }else if (msg.getCmd()== Cmd.Command.MESSAGE_CHANGE){
            String msgId = msg.getMsgId();
            if (StrUtil.isBlank(msgId)){
                Msg.Base errMsg = MsgUtil.sysMsg("消息id信息有误！");
                ctx.channel().writeAndFlush(errMsg);
                return;
            }
            Msg.StatusType msgStatus = msg.getMsgStatus();
            if (line){
                Channel sendChanel = ChannelManage.getChannelByUserId(accepterId);
                Msg.Base sendMsg = MsgUtil.changeMsg(sendUserId, accepterId, msgId, msgStatus);
                sendChanel.writeAndFlush(sendMsg);
            }
            changeMsgService.updateStatus(msg);
        }



    }

}

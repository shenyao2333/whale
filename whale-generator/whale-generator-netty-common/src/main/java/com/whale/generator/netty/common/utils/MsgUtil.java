package com.whale.generator.netty.common.utils;

import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 0:11
 * @description: 消息快速构建
 */
public class MsgUtil {

    /**
     * 构建系统消息
     * @param content
     * @return
     */
    public static Msg.Base  sysMsg(String content){
        return buildMsg("","",content, Cmd.Command.SYSTEM,"","",null);
    }

    /**
     * 构建系统消息
     * @param content
     * @return
     */
    public static Msg.Base  sysMsg(String msgId,String content){
        return buildMsg(msgId+"","",content, Cmd.Command.SYSTEM,"","",null);
    }

    /**
     * 回复业务消息
     * @param
     * @return
     */
    public static Msg.Base  sysMsg(Long msgId,String  cmsgId){
        return buildMsg(msgId+"",cmsgId+"","消息发送成功", Cmd.Command.SYSTEM,"","",null);
    }

    /**
     * 构建系统心跳消息
     * @return
     */
    public static Msg.Base serverBeatMsg(){
        return buildMsg("","","服务端正常心跳信息。",Cmd.Command.HEARTBEAT_RESPONSE,"","",null);
    }


    /**
     *
     * @param msgId 消息id
     * @param content 申请备注
     * @param cmsgId  客户端id
     * @param sendUserId 发送人的
     * @param accepterId 接收人的
     * @return
     */
    public static Msg.Base friendSponsorMsg(Long msgId,String content,String cmsgId ,String sendUserId ,String accepterId){
        return buildMsg(msgId+"",cmsgId,content,Cmd.Command.BULL_ASK,sendUserId,accepterId,null);
    }

    /**
     * 发送好友结果通知
     * @param msgId
     * @param content
     */
    public static Msg.Base friendResult(String msgId, String content, Msg.StatusType status) {
        return buildMsg(msgId,"",content,Cmd.Command.BULL_RESULT,"","",status);
    }


    /**
     * 普通业务信息
     * @param sendUserId
     * @param accepterId
     * @param content
     * @return
     */
    public static Msg.Base forwardMsg(Long msgId, String sendUserId, String accepterId ,String content ){
        return new  Msg.Base().toBuilder()
                .setMsgId(msgId+"")
                .setSendUserId(sendUserId)
                .setAccepterId(accepterId)
                .setContent(content)
                .setCmd(Cmd.Command.NORMAL)
                .setSendTime(System.currentTimeMillis())
                .build();
    }


    public static Msg.Base changeMsg(String sendUserId, String accepterId, String msgIds, Msg.StatusType msgStatus){
        return new Msg.Base().toBuilder()
                .setSendUserId(sendUserId)
                .setAccepterId(accepterId)
                .setContent("消息状态改变！")
                .setMsgStatus(msgStatus)
                .setMsgId(msgIds)
                .setCmd(Cmd.Command.MESSAGE_CHANGE)
                .setSendTime(System.currentTimeMillis())
                .build();
    }




    private static  Msg.Base  buildMsg(String msgId, String cmsgId , String content, Cmd.Command cmd, String sendUserId, String accepterId, Msg.StatusType statusType){
        return new  Msg.Base().toBuilder()
                .setCMsgId(cmsgId)
                .setMsgId(msgId)
                .setContent(content)
                .setCmd(cmd)
                .setSendTime(System.currentTimeMillis())
                .setAccepterId(accepterId)
                .setSendUserId(sendUserId)
                .setMsgStatus(statusType)
                .build();
    }


}

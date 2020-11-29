package com.whale.generator.netty.common.utils;

import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.MsgBase;
import com.whale.generator.netty.common.protocol.MsgStatus;

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
    public static  MsgBase.Msg  sysMsg(String content){
        return buildMsg("",content,Command.CommandType.SYSTEM);
    }

    /**
     * 构建系统消息
     * @param content
     * @return
     */
    public static  MsgBase.Msg  sysMsg(String msgId,String content){
        return buildMsg(msgId,content,Command.CommandType.SYSTEM);
    }


    /**
     * 构建系统心跳消息
     * @return
     */
    public static  MsgBase.Msg serverBeatMsg(){
        return buildMsg(null,"服务端正常心跳信息。",Command.CommandType.HEARTBEAT_RESPONSE);
    }


    /**
     * 普通业务信息
     * @param sendUserId
     * @param accepterId
     * @param content
     * @return
     */
    public static MsgBase.Msg forwardMsg(String sendUserId, String accepterId ,String content ){
        return new MsgBase.Msg().toBuilder()
                .setSendUserId(sendUserId)
                .setAccepterId(accepterId)
                .setContent(content)
                .setCmd(Command.CommandType.NORMAL)
                .setSendTime(System.currentTimeMillis())
                .build();
    }


    public static  MsgBase.Msg changeMsg(String sendUserId, String accepterId, String msgIds, MsgStatus.StatusType type){
        return new MsgBase.Msg().toBuilder()
                .setSendUserId(sendUserId)
                .setAccepterId(accepterId)
                .setContent("消息状态改变！")
                .setMsgStatus(type)
                .setMsgId(msgIds)
                .setCmd(Command.CommandType.MESSAGE_CHANGE)
                .setSendTime(System.currentTimeMillis())
                .build();
    }




    private static  MsgBase.Msg  buildMsg(String msgId,String content, Command.CommandType type){
        return new MsgBase.Msg().toBuilder()
                .setMsgId(msgId)
                .setContent(content)
                .setCmd(type)
                .setSendTime(System.currentTimeMillis())
                .build();
    }

}

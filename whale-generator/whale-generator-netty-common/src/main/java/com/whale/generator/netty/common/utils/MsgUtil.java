package com.whale.generator.netty.common.utils;

import com.whale.generator.netty.common.protocol.Command;
import com.whale.generator.netty.common.protocol.MsgBase;

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
        return buildMsg(content,Command.CommandType.SYSTEM);
    }


    /**
     * 构建系统心跳消息
     * @return
     */
    public static  MsgBase.Msg serverBeatMsg(){
        return buildMsg("服务端正常心跳信息。",Command.CommandType.HEARTBEAT_RESPONSE);
    }



    private static  MsgBase.Msg  buildMsg(String content, Command.CommandType type){
        return new MsgBase.Msg().toBuilder()
                .setContent(content)
                .setCmd(type)
                .setSendTime(System.currentTimeMillis())
                .build();
    }

}

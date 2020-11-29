package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.protocol.MsgBase;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 12:01
 * @description: 消息改变状态处理
 */
public interface ChangeMsgService {


    /**
     * 消息状态改变
     * @param msg
     */
    void updateStatus(MsgBase.Msg msg);
}

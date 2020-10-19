package com.whale.generator.netty.common.constant;

/**
 * @author sy
 * @date Created in 2020.10.18 21:53
 * @description 消息类型
 */
public interface MsgTypeConstants {
    /**
     * 常规业务消息
     */
    Byte NORMAL =1;
    /**
     * 客户端心跳消息
     */
    Byte HEARTBEAT_REQUEST = 1;
    /**
     * 服务端心跳消息
     */
    Byte HEARTBEAT_RESPONSE = 2;
    /**
     * 消息状态改变
     */
    Byte MESSAGE_CHANGE = 3;

}

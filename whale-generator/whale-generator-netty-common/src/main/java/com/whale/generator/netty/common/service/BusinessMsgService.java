package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.protocol.Msg;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 11:49
 * @description: 业务信息处理逻辑
 */
public interface BusinessMsgService {


    /**
     * 保存普通业务信息
     * @param msg
     * @param status 状态
     */
    Long  saveMsg(Long msgId , Msg.Base msg,String status);


}

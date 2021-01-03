package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.FriendApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.generator.netty.common.protocol.Msg;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/3 21:11
 * @description: ${description}
 */
public interface FriendApplyService extends IService<FriendApply>{


    /**
     * 好友申请
     * @param msg
     * @param msgId
     * @param status
     */
    void sponsor(Msg.Base msg, Long msgId, String status ,String sendUserId);

}

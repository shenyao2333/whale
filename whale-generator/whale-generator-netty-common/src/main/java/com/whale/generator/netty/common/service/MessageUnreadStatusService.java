package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.MessageUnreadStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.generator.netty.common.protocol.Msg;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/30 21:17
 * @description: ${description}
 */
public interface MessageUnreadStatusService extends IService<MessageUnreadStatus>{
    void savaData(Msg.Base msg);
}

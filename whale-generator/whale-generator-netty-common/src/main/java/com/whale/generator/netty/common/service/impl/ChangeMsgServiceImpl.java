package com.whale.generator.netty.common.service.impl;

import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.ChangeMsgService;
import com.whale.generator.netty.common.service.MessageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 12:15
 * @description:
 */
@Service
@RequiredArgsConstructor
public class ChangeMsgServiceImpl implements ChangeMsgService {

    private final   MessageInfoService messageInfoService;

    @Override
    public void updateStatus(Msg.Base msg) {
        String msgId = msg.getMsgId();
        String[] split = msgId.split(",");
        int number = msg.getMsgStatus().getNumber();
        messageInfoService.batchUpdStatus(split,number);
    }
}

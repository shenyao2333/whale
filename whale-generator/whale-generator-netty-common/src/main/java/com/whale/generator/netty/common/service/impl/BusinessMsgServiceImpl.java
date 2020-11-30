package com.whale.generator.netty.common.service.impl;
import java.util.Date;

import com.whale.generator.netty.common.domain.MessageInfo;
import com.whale.generator.netty.common.protocol.MsgBase;
import com.whale.generator.netty.common.service.BusinessMsgService;
import com.whale.generator.netty.common.service.MessageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 11:50
 * @description: 业务消息实现
 */
@Service
@RequiredArgsConstructor
public class BusinessMsgServiceImpl implements BusinessMsgService {


    private final MessageInfoService messageInfoService ;



    @Override
    public Integer saveMsg(MsgBase.Msg msg) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setSendUserId(Integer.parseInt(msg.getSendUserId()));
        messageInfo.setAccepterId(Integer.parseInt(msg.getAccepterId()));
        messageInfo.setMsgStatus("0");
        messageInfo.setSendType(msg.getSendType().getNumber()+"");
        messageInfo.setContent(msg.getContent());
        messageInfo.setCreated(new Date());
        messageInfo.setUpdated(new Date());
        messageInfoService.save(messageInfo);
        return messageInfo.getId();
    }




}

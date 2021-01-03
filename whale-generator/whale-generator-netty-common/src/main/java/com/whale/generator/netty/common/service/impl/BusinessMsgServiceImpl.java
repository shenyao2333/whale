package com.whale.generator.netty.common.service.impl;
import java.util.Date;

import com.whale.generator.netty.common.domain.MessageInfo;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.BusinessMsgService;
import com.whale.generator.netty.common.service.MessageInfoService;
import com.whale.provider.common.utils.SnowflakeId;
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
    public Long saveMsg(Long msgId, Msg.Base msg,String stauts) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(msgId);
        messageInfo.setSendUserId(Integer.parseInt(msg.getSendUserId()));
        messageInfo.setAccepterId(Integer.parseInt(msg.getAccepterId()));
        messageInfo.setMsgStatus(stauts);
        messageInfo.setContent(msg.getContent());
        messageInfo.setCreated(new Date());
        messageInfo.setUpdated(new Date());
        messageInfo.setDel("0");
        messageInfo.setType("0");
        messageInfoService.save(messageInfo);
        return msgId;
    }




}

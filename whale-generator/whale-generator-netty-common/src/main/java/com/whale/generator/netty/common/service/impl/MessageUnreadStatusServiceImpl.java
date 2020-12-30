package com.whale.generator.netty.common.service.impl;
import java.util.ArrayList;
import java.util.Date;

import com.whale.generator.netty.common.protocol.Msg;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.domain.MessageUnreadStatus;
import com.whale.generator.netty.common.mapper.MessageUnreadStatusMapper;
import com.whale.generator.netty.common.service.MessageUnreadStatusService;
/**
 * @Author: shenyao
 * @Date: Created by 2020/12/30 21:17
 * @description: ${description}
 */
@Service
public class MessageUnreadStatusServiceImpl extends ServiceImpl<MessageUnreadStatusMapper, MessageUnreadStatus> implements MessageUnreadStatusService{




    @Override
    public void savaData(Msg.Base msg) {
        String msgList = msg.getMsgId();
        String[] split = msgList.split(",");
        ArrayList<MessageUnreadStatus> messageUnreadStatusArrayList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            MessageUnreadStatus messageUnreadStatus = new MessageUnreadStatus();
            messageUnreadStatus.setMsgId(Long.parseLong(split[i]));
            messageUnreadStatus.setCmsgId("");
            messageUnreadStatus.setCreated(new Date());
            messageUnreadStatus.setSendUserId(0);
            messageUnreadStatus.setAccepterId(0);
            messageUnreadStatus.setMsgStaus("");
            messageUnreadStatusArrayList.add(messageUnreadStatus);
        }
        this.saveBatch(messageUnreadStatusArrayList);
    }
}

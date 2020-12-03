package com.whale.generator.rocketmq.service;

import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/3 21:42
 * @description:
 */
@Service
public class TestService {


    @Resource
    private MessageChannel output;

    public void saf(){
        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageConst.PROPERTY_TAGS, "tagStr");
        Message message = MessageBuilder.createMessage("s", new MessageHeaders(headers));
        output.send(message);
    }

}

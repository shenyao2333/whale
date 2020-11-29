package com.whale.generator.netty.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.domain.MessageInfo;
import com.whale.generator.netty.common.mapper.MessageInfoMapper;
import com.whale.generator.netty.common.service.MessageInfoService;

import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 1:25
 * @description: ${description}
 */
@Service
@RequiredArgsConstructor
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements MessageInfoService {
    private final  MessageInfoMapper messageInfoMapper;

    @Override
    public int updateMsgStatusById(String updatedMsgStatus, Integer id) {
        return messageInfoMapper.updateMsgStatusById(updatedMsgStatus,id,new Date());
    }
}


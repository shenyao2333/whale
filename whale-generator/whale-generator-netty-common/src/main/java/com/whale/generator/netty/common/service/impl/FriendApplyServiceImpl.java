package com.whale.generator.netty.common.service.impl;
import java.util.Date;

import com.whale.generator.netty.common.protocol.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.mapper.FriendApplyMapper;
import com.whale.generator.netty.common.domain.FriendApply;
import com.whale.generator.netty.common.service.FriendApplyService;
/**
 * @Author: shenyao
 * @Date: Created by 2021/1/3 21:11
 * @description: ${description}
 */
@Service
@RequiredArgsConstructor
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements FriendApplyService{


    private final FriendApplyMapper friendApplyMapper;


    @Override
    public void sponsor(Msg.Base msg, Long msgId, String status,String sendUserId) {
        FriendApply friendApply = new FriendApply();
        friendApply.setId(msgId);
        friendApply.setAccepterUserId(Long.parseLong(msg.getAccepterId()));
        friendApply.setApplyUserId(Long.parseLong(sendUserId));
        friendApply.setCreated(new Date());
        friendApply.setApplyStatus(status);
        friendApply.setApplyContent(msg.getContent());
        friendApply.setRejectContent("");
        this.save(friendApply);
    }
}

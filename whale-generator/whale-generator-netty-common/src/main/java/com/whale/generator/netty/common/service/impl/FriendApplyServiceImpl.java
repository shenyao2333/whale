package com.whale.generator.netty.common.service.impl;
import java.util.ArrayList;
import java.util.Date;

import com.whale.generator.netty.common.domain.Friend;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.FriendService;
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
    private final FriendService friendService;
   // private final SysUserService sysUserService;


    @Override
    public void sponsor(Msg.Base msg, Long msgId, String status,String sendUserId) {
        FriendApply friendApply = new FriendApply();
        friendApply.setAccepterUserId(Integer.parseInt(msg.getAccepterId()));
        friendApply.setApplyUserId(Integer.parseInt(sendUserId));
        friendApply.setCreated(new Date());
        friendApply.setApplyStatus(status);
        friendApply.setApplyContent(msg.getContent());
        friendApply.setRejectContent("");
        this.save(friendApply);
    }

    @Override
    public void result(Msg.Base msg) {
        String msgId = msg.getMsgId();
        FriendApply apply = this.getById(msgId);
        if (apply==null){
            return;
        }
        Msg.StatusType msgStatus = msg.getMsgStatus();
        FriendApply updApply = new FriendApply();
        updApply.setUpdated(new Date());
        updApply.setId(Integer.parseInt(msgId));
        if (msgStatus== Msg.StatusType.READ){
            updApply.setApplyStatus("3");
        }else if (msgStatus== Msg.StatusType.REMOVE){
            updApply.setApplyStatus("2");
            updApply.setRejectContent(msg.getContent());
        }
        this.updateById(updApply);
    }


    private void insertionFriend(FriendApply  apply){
        ArrayList<Friend> friends = new ArrayList<>();
        Integer accepterUserId = apply.getAccepterUserId();
        Integer applyUserId = apply.getApplyUserId();

        Friend applyUser = friendService.getById(applyUserId);

        Friend applyFriend = new Friend();
        applyFriend.setUserId(applyUser.getUserId());
        applyFriend.setFriendUserId(accepterUserId);
        applyFriend.setGroupId(0);
       // applyFriend.setRemark(applyUser.get);

    }


}

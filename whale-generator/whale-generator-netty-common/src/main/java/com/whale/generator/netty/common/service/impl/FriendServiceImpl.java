package com.whale.generator.netty.common.service.impl;

import com.whale.generator.netty.common.constant.NettyConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.mapper.FriendMapper;
import com.whale.generator.netty.common.domain.Friend;
import com.whale.generator.netty.common.service.FriendService;

import javax.annotation.Resource;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/4 21:14
 * @description: 好友功能处理
 */
@Service
@RequiredArgsConstructor
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    private final FriendMapper friendMapper;

    @Resource
    private FriendService friendService;


    @Override
    @Cacheable(value = NettyConstant.FRIEND_LIST_ID + "#86400", key = "#userId" )
    public Set<Integer> getFriendIdByUserId(Integer userId) {
        return friendMapper.selectByUserId(userId);
    }



    @Override
    public Boolean isFriend(Integer userId, Integer friendId) {
        Set<Integer> friendIdList = friendService.getFriendIdByUserId(userId);
        return  friendIdList.contains(friendId);
    }


}


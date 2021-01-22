package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.Friend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/4 21:14
 * @description: 好友处理
 */
public interface FriendService extends IService<Friend> {

    /**
     * 获取好友id列表
     *
     * @param userId
     * @return
     */
    Set<Integer> getFriendIdByUserId(Integer userId);


    /**
     * 是否是好友
     * @param userId
     * @param friendId 待验证好友id
     * @return
     */
    Boolean isFriend(Integer userId,Integer friendId);


}


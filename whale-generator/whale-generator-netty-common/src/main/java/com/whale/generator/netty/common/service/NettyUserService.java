package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.NettyUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface NettyUserService extends IService<NettyUser> {


    /**
     * 批量获取用户信息
     *
     * @return
     */
    List<NettyUser> getByUserIds(List<Integer> userIds);

}


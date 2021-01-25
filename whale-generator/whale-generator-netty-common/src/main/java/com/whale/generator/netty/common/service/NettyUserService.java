package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.NettyUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/22 20:20
 * @description: ${description}
 */
public interface NettyUserService extends IService<NettyUser>{

    List<NettyUser> getByUserIds(List<Integer> userIds);
}

package com.whale.generator.netty.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.domain.NettyUser;
import com.whale.generator.netty.common.mapper.NettyUserMapper;
import com.whale.generator.netty.common.service.NettyUserService;
/**
 * @Author: shenyao
 * @Date: Created by 2021/1/22 20:20
 * @description: ${description}
 */
@Service
@RequiredArgsConstructor
public class NettyUserServiceImpl extends ServiceImpl<NettyUserMapper, NettyUser> implements NettyUserService{

    private final NettyUserMapper nettyUserMapper;


    @Override
    public List<NettyUser> getByUserIds(List<Integer> userIds) {
       // return nettyUserMapper.;
        return null;
    }


}

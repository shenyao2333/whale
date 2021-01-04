package com.whale.generator.netty.common.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.generator.netty.common.mapper.FriendMapper;
import com.whale.generator.netty.common.domain.Friend;
import com.whale.generator.netty.common.service.FriendService;
/**
 * @Author: shenyao
 * @Date: Created by 2021/1/4 21:14
 * @description: ${description}
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService{

}

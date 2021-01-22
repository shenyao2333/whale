package com.whale.generator.netty.common.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale.generator.netty.common.domain.Friend;

public interface FriendMapper extends BaseMapper<Friend> {


    Set<Integer> selectByUserId(@Param("userId")Integer userId);




}
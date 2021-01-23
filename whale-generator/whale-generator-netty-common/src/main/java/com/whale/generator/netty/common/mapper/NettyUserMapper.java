package com.whale.generator.netty.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale.generator.netty.common.domain.NettyUser;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface NettyUserMapper extends BaseMapper<NettyUser> {
    List<NettyUser> getByUserIds(@Param("userIds") List<Integer> userIds);
}
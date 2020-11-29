package com.whale.generator.netty.common.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale.generator.netty.common.domain.MessageInfo;

import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 1:28
 * @description: ${description}
 */
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {


    int updateMsgStatusById(@Param("updatedMsgStatus")String updatedMsgStatus, @Param("id")Integer id, @Param("time") Date time);




}
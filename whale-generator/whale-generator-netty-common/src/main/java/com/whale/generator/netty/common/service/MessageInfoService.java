package com.whale.generator.netty.common.service;

import com.whale.generator.netty.common.domain.MessageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 1:25
 * @description: ${description}
 */
public interface MessageInfoService extends IService<MessageInfo> {


    /**
     * 改变消息状态
     * @param updatedMsgStatus
     * @param id
     * @return
     */
    int updateMsgStatusById(String updatedMsgStatus, Integer id);


    /**
     * 批量修改消息状态
     * @param split
     * @param number
     */
    int batchUpdStatus(String[] split, int number);

}


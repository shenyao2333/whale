package com.whale.provider.kafka.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author: sy
 * @Date: Created by 2021/7/27 18:11
 * @description:
 */
@Data
public class KafkaMessage {
    /**
     * 消息id
     */
    private Long messageId;
    /**
     * 发送时间
     */
    private Date sendDate;
    /**
     * 消息体
     */
    private Object body;
    /**
     * 其他备注
     */
    private String comment;


}

package com.whale.provider.kafka.config;

import com.whale.provider.kafka.constant.KafkaTopicConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

/**
 * @Author: sy
 * @Date: Created by 2021/7/28 10:07
 * @description:
 */
public class KafkaTopicConfig {


    /**
     *  修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
     *  创建一个分区为3，两个副本为2的topic， 副本的数量不能超过broker的数量，否则创建主题时会失败。
     * @return topic
     */
    @Bean
    public NewTopic updateTopic() {
        return new NewTopic(KafkaTopicConstant.LOG,1, (short) 1);
    }





}

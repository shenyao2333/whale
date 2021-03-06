package com.whale.generator.rocketmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/1 22:15
 * @description:
 */
@EnableDiscoveryClient
@EnableAsync
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.whale", exclude  = {SecurityAutoConfiguration.class})
@MapperScan("com.whale.**.mapper")
@EnableBinding({ Source.class, Sink.class })
public class WhaleGeneratorRocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleGeneratorRocketmqApplication.class, args);
    }

}

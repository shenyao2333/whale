package com.whale.business.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: sy
 * @Date: Created by 2021/6/11 9:49
 * @description: 工作流启动类
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.whale")
@MapperScan(basePackages = {"com.whale.**.mapper"})
public class WhaleBusinessWorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleBusinessWorkflowApplication.class, args);
    }

}

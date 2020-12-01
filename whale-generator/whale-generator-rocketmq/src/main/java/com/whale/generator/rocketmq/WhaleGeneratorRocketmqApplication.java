package com.whale.generator.rocketmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/1 22:15
 * @description:
 */
@EnableDiscoveryClient
@EnableAsync
@EnableSwagger2
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.whale", exclude  = {SecurityAutoConfiguration.class})
@MapperScan("com.whale.**.mapper")
public class WhaleGeneratorRocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleGeneratorRocketmqApplication.class, args);
    }

}

package com.whale.business.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/22 21:19
 * @description:
 */
@EnableDiscoveryClient
@EnableAsync
@EnableSwagger2
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.whale")
@MapperScan(basePackages = {"com.whale.**.mapper"})
public class WhaleBusinessSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleBusinessSystemApplication.class, args);
    }

}

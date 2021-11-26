package com.whale.business.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @Author: shenyao
 * @Date: Created by 2021/3/7 19:23
 * @description:
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.whale")
@MapperScan(basePackages = {"com.whale.**.mapper"})
public class WhaleBusinessOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleBusinessOrderApplication.class, args);
    }

}

package com.whale.business.order;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: shenyao
 * @Date: Created by 2021/3/7 19:23
 * @description:
 */
@EnableDiscoveryClient
@EnableAsync
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.whale")
@MapperScan(basePackages = {"com.whale.**.mapper"})
@EnableSwagger2
@EnableFeignClients("com.whale")
@EnableSwaggerBootstrapUI
public class WhaleBusinessOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleBusinessOrderApplication.class, args);
    }

}

package com.whale.generator.netty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sy
 * @date Created in 2020.10.18 22:01
 * @description
 */
@EnableDiscoveryClient
@EnableAsync
@EnableSwagger2
@EnableCaching
@MapperScan(basePackages = "com.whale.*.mapper")
@SpringBootApplication
public class WhaleGeneratorNettyApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WhaleGeneratorNettyApplication.class, args);
    }


}

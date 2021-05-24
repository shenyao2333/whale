package com.whale.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author sy
 * @date Created in 2020.10.7 20:11
 * @description
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.whale",exclude= {DataSourceAutoConfiguration.class})
public class WhaleGatewayApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WhaleGatewayApplication.class, args);
    }

}

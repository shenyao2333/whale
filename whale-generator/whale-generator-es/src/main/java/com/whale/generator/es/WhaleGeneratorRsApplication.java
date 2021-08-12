package com.whale.generator.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 9:42
 * @description:
 */

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.whale",exclude = DataSourceAutoConfiguration.class)
public class WhaleGeneratorRsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WhaleGeneratorRsApplication.class, args);
    }
}

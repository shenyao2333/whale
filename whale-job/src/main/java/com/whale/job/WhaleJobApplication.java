package com.whale.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sy
 * @date: 2021/3/11 15:29
 * @description
 */
@SpringBootApplication(scanBasePackages = "com.whale")
public class WhaleJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhaleJobApplication.class, args);
    }



}

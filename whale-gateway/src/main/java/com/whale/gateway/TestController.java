package com.whale.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sy
 * @Date: Created by 2021-11-22-22:12
 * @description:
 */
@RefreshScope
@RestController
public class TestController {

    @Value("${config.name}")
    private String testName;

    @GetMapping("/test")
    public String test(){
        return this.testName;
    }


}

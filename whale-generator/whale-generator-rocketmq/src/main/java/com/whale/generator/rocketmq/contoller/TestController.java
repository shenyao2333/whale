package com.whale.generator.rocketmq.contoller;

import com.whale.generator.rocketmq.service.TestService;
import com.whale.provider.basices.web.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/3 21:42
 * @description:
 */
@RestController
public class TestController {

    @Resource
    private   TestService testService;

    @GetMapping("/test")
    public R sdfa(){
        testService.saf();
        return R.ok();
    }


}

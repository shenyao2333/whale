package com.whale.business.order.controller;

import com.whale.provider.basices.web.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shenyao
 * @Date: Created by 2021/3/7 21:54
 * @description:
 */
@RestController
@RequestMapping("/test")
public class TestController {



    @GetMapping("/test")
    public R test1(){
        return R.ok("订单服务！");
    }




}

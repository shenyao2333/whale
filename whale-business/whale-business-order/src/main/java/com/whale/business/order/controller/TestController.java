package com.whale.business.order.controller;

import com.whale.business.order.feign.system.TestFeign;
import com.whale.provider.basices.web.R;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TestController {

    private final TestFeign testFeign;

    @GetMapping("/test")
    public R test1(){
        return R.ok("订单服务！");
    }

    @GetMapping("/testFei")
    public R testFei(){
        R<Object> test = testFeign.getTest();
        return R.ok(test.getData());
    }


}

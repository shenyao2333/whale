package com.whale.business.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sy
 * @date: 2021/3/3 13:37
 * @description
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/test1")
    public void sdf(){
        System.out.println("进来了---");
    }


}
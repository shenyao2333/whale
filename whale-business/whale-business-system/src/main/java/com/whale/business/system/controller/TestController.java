package com.whale.business.system.controller;

import com.whale.provider.basices.web.R;
import com.whale.provider.security.domain.WhaleUser;
import com.whale.provider.security.utils.SecurityUtil;
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
    public R<Integer> sdf(){
        System.out.println("进来了---");
        return R.ok(32);
    }

    @GetMapping("/test2")
    public R<WhaleUser> test2(){
        WhaleUser user = SecurityUtil.getUser();
        return R.ok(user);
    }



}

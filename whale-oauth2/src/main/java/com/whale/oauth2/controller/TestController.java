package com.whale.oauth2.controller;

import com.whale.api.oauth2.domain.WhaleUser;
import com.whale.api.oauth2.dubbo.service.UserAuthService;
import com.whale.provider.basices.web.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/25 21:26
 * @description:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test2")
@Api
public class TestController {

    private final UserAuthService userAuthService;


    @GetMapping("/test")
    @ResponseBody
    public R test(){
        System.out.println("----进来---");
        return R.ok();
    }



    @GetMapping("/token")
    public R token(String token){
        SecurityContext context = SecurityContextHolder.getContext();


        Authentication authentication = context.getAuthentication();

        Object principal = authentication.getPrincipal();



        System.out.println(principal);
        return R.ok();
    }



}

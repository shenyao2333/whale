package com.whale.oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.whale.provider.basices.web.R;
import com.whale.provider.common.utils.RestTemplateUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/25 21:26
 * @description:
 */
@Api
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {


    private final RestTemplateUtil restTemplateUtil;
    private final RestTemplate restTemplate;
    @GetMapping("/test")
    public R test(){
        //Object forObject = restTemplate.getForObject("http://127.0.0.1:8000/test/test2", JSONObject.class);
        //System.out.println(forObject);
        //return R.ok(forObject);
        JSONObject jsonObject = restTemplateUtil.doGet("http://127.0.0.1:8000/whale-oauth2/test/test2",null);
        System.out.println("----进来---");
        return R.ok(jsonObject);

    }


    @GetMapping("/test2")
    public R test2(){

        HashMap<String, Object> map = new HashMap<>();
        map.put("1",true);

        map.put("2",2);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(2);
        objects.add(344);
        map.put("3",objects );
        return R.ok(map);
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

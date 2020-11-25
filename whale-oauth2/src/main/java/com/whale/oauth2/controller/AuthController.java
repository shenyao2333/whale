package com.whale.oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.whale.oauth2.domain.dto.LoginDto;
import com.whale.provider.basices.domain.WhaleUser;
import com.whale.provider.basices.utils.SecurityUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import com.whale.provider.common.utils.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:47
 * @description:
 */
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final RestTemplateUtil restTemplateUtil;


    @PostMapping("/test")
    public R test(){
        return R.ok();
    }


    @GetMapping("/user")
    public R<WhaleUser> user(){
        WhaleUser user = SecurityUtil.getUser();
        return R.ok(user);
    }




    @PostMapping("/auth/login")
    public R login(@RequestBody @Valid LoginDto loginDto){
        HashMap<String, String> headMap = new HashMap<String, String>(1){
            {
                put("Authorization","Basic d2hhbGU6MTIzNDU2");
            }
        };
        HashMap<String, String> paramsMap = new HashMap<String, String>(1){
            {
                put("username",loginDto.getUserName());
                put("password",loginDto.getPassword());
                put("grant_type","password");
                put("scope","server");
            }
        };
        JSONObject jsonObject = restTemplateUtil.doPost("http://localhost:8000/oauth/token", headMap, paramsMap, null);
        if ((boolean)jsonObject.get("status")){
            return R.ok(jsonObject);
        }
       throw new GrabException( 2002,(String)jsonObject.get("msg"));

    }

}

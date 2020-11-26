package com.whale.oauth2.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.whale.oauth2.domain.dto.LoginDto;
import com.whale.provider.basices.domain.WhaleUser;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.basices.utils.SecurityUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import com.whale.provider.common.utils.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:47
 * @description:
 */

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RestTemplateUtil restTemplateUtil;
    private final TokenStore tokenStore;
    private final RedisUtil redisUtil;

    @GetMapping("/user")
    public R<WhaleUser> user(){
        Object o = redisUtil.hasKey("whale:auth:3629ca84-6b53-4b2e-89e1-d365178e6319");
        System.out.println(o);
        System.out.println(o);
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


    @GetMapping("/auth/logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader){
        if (StrUtil.isBlank(authHeader)) {
            return R.fail("退出失败，token 为空");
        }
        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return R.fail("退出失败，token 无效");
        }
        try {
            // 清空access token
            tokenStore.removeAccessToken(accessToken);
            // 清空 refresh token
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            tokenStore.removeRefreshToken(refreshToken);
        }catch (Exception e){
            return R.ok();
        }
        return R.ok();
    }

}

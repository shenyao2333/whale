package com.whale.oauth2.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.whale.oauth2.domain.dto.LoginDto;
import com.whale.oauth2.service.impl.WhaleUserDetailService;
import com.whale.provider.security.domian.WhaleUser;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.security.utils.SecurityUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import com.whale.provider.common.utils.RestTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:47
 * @description:
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "用户登录管理")
public class AuthController {

    private final RestTemplateUtil restTemplateUtil;
    private final TokenStore tokenStore;
    private final RedisUtil redisUtil;
    private final WhaleUserDetailService sysUserService;

    @GetMapping("/user")
    @ApiOperation(value = "获取用户信息")
    public R<WhaleUser> user(){
        WhaleUser user = SecurityUtil.getUser();
        return R.ok(user);
    }

    @GetMapping("/upd")
    @ApiOperation(value = "获取用户信息")
    public R<WhaleUser> upd(){
        UserDetails shenyao = sysUserService.loadUserByUsername("shenyao");
        return R.ok(null);
    }





    @PostMapping("/auth/login")
    @ApiOperation(value = "用户登录")
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
    @ApiOperation(value = "登录退出")
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

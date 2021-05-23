package com.whale.oauth2.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.whale.oauth2.domain.dto.LoginDto;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import com.whale.provider.common.utils.RestTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;
import java.util.HashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:47
 * @description:
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "认证-登录管理")
@RequestMapping("/auth")
public class AuthController {

    private final RestTemplateUtil restTemplateUtil;
    private final TokenStore tokenStore;


    @PostMapping("/login")
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
        if (jsonObject.get("access_token")!=null){
            return R.ok(jsonObject);
        }
       throw new GrabException(3002,(String)jsonObject.get("msg"));
    }


    @GetMapping("/logout")
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
        // 清空access token
        tokenStore.removeAccessToken(accessToken);
        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return R.ok("退出成功！");
    }


    public static void main(String[] args) {
        String whale = new BCryptPasswordEncoder().encode("123456");

        System.out.println(whale);

        String namePwd="whale:whale";
        byte[] bytes = namePwd.getBytes();
        String basic = Base64.getEncoder().encodeToString(bytes);
        System.out.println(basic);
    }

}

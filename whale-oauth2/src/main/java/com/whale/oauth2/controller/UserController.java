package com.whale.oauth2.controller;

import com.whale.provider.basices.web.R;
import com.whale.provider.security.domain.WhaleUser;
import com.whale.provider.security.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sy
 * @Date: Created by 2021/5/25 9:56
 * @description:
 */
@RestController
@RequestMapping("/user")
public class UserController {




    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public R info(){
        WhaleUser user = SecurityUtil.getUser();
        return R.ok(user);
    }



}

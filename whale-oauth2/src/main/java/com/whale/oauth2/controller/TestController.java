package com.whale.oauth2.controller;

import com.whale.provider.basices.web.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
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


    @GetMapping("/test")
    @ResponseBody
    public R test(){
        return R.ok();
    }



}

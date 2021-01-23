package com.whale.business.system.controller;


import com.whale.business.system.domain.vo.OssSTSVo;
import com.whale.business.system.service.AliyunSecretKeyService;
import com.whale.provider.basices.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Author: shenyao
 * @Date: Created by 2020/12/22 21:48
 * @description: 密钥处理
 */
@RestController
@RequestMapping("/sys/secretKey")
@RequiredArgsConstructor
@Api(tags = "密钥-阿里云")
public class SecretKeyController {

    @Resource
    private  AliyunSecretKeyService aliyunSecretKeyService;

    @GetMapping("/getOssSTS")
    @ApiOperation(value = "获取STS临时授权访问OSS")
    public R<OssSTSVo> getOssSTS(){
        OssSTSVo vo = aliyunSecretKeyService.getOssSTS();
        return R.ok(vo);
    }


    @GetMapping("/test")
    public R<OssSTSVo> test(){
        return R.ok("成功");
    }






}

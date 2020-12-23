package com.whale.business.system.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whale.business.system.domain.vo.OssSTSVo;
import com.whale.business.system.service.AliyunSecretKeyService;
import com.whale.business.system.service.SecretKeyService;
import com.whale.provider.basices.web.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/22 21:48
 * @description: 密钥处理
 */
@RestController
@RequestMapping("/sys/secretKey")
@RequiredArgsConstructor
public class SecretKeyController {

    private final SecretKeyService secretKeyService;
    private final AliyunSecretKeyService aliyunSecretKeyService;

    @GetMapping("/getOssSTS")
    @ApiOperation(value = "获取STS临时授权访问OSS")
    public R<OssSTSVo> getOssSTS(){
        OssSTSVo vo = aliyunSecretKeyService.getOssSTS();
        return R.ok(vo);
    }






}

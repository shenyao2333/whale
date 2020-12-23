package com.whale.business.system.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whale.business.system.domain.vo.OssSTSVo;
import com.whale.business.system.service.SecretKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/23 20:57
 * @description:
 */
@Service
@RequiredArgsConstructor
public class SecretKeyServiceImpl implements SecretKeyService {


    @Override
    public OssSTSVo getOssSTS() {
        return null;
    }



}

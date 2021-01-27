package com.whale.business.system.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whale.business.system.domain.vo.OssSTSVo;
import com.whale.business.system.service.AliyunSecretKeyService;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/23 20:59
 * @description:
 */
@Service
@RequiredArgsConstructor
public class AliyunSecretKeyServiceImpl implements AliyunSecretKeyService {

    @Value("${oss.accessKeyId}")
    private String accessKeyId ;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret ;
    @Value("${oss.roleArn}")
    private String roleArn ;
    @Value("${oss.roleSessionName}")
    private String roleSessionName;
    private final RedisUtil redisUtil;


    @Override
    public OssSTSVo getOssSts() {

        String stsKey = "aliyunSecret:sts";
        Object o = redisUtil.get(stsKey);
        if (o!=null){
            OssSTSVo vo = (OssSTSVo)o;
            vo.setExpiration(redisUtil.getExpire(stsKey));
            return vo;
        }
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            String endpoint = "sts.cn-shenzhen.aliyuncs.com";
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(2800L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = response.getCredentials();
            OssSTSVo ossStsVo = new OssSTSVo();
            ossStsVo.setAccessKey(credentials.getAccessKeyId());
            ossStsVo.setSecurityToken(credentials.getSecurityToken());
            ossStsVo.setAccessKeySecret(credentials.getAccessKeySecret());
            ossStsVo.setExpiration(2800L);
            redisUtil.set(stsKey,ossStsVo,2700);
            return ossStsVo;
        } catch (ClientException e) {
            throw new GrabException(6023,e.getErrMsg());
        }
    }

}

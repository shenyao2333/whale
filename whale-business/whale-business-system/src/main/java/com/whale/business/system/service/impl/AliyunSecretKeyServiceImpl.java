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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


    private final String endpoint = "sts.cn-shenzhen.aliyuncs.com";
    private final RedisUtil redisUtil;
    private String stsKey="aliyunSecret:sts";


    @Override
    public OssSTSVo getOssSTS() {

        System.out.println("进来了");
        Object o = redisUtil.get(stsKey);
        if (o!=null){
            return  (OssSTSVo)o;
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
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(2000L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = response.getCredentials();
            OssSTSVo ossSTSVo = new OssSTSVo();
            ossSTSVo.setAccessKey(credentials.getAccessKeyId());
            ossSTSVo.setSecurityToken(credentials.getSecurityToken());
            ossSTSVo.setAccessKeySecret(credentials.getAccessKeySecret());
            String expiration = credentials.getExpiration();
            ossSTSVo.setExpiration(Long.parseLong(expiration));
            redisUtil.set(stsKey,ossSTSVo,1800);
            return ossSTSVo;
        } catch (ClientException e) {
            throw new GrabException(6023,e.getErrMsg());
        }

    }

}

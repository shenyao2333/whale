package com.whale.oauth2.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whale.oauth2.domain.OssSTSVo;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/24 21:19
 * @description:
 */
@Api
@RestController
@RequestMapping
public class ApiController {

    @Resource
    private RedisUtil redisUtil;


    @Value("${oss.accessKeyId}")
    private String accessKeyId ;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret ;
    @Value("${oss.roleArn}")
    private String roleArn ;
    @Value("${oss.roleSessionName}")
    private String roleSessionName;
    private final String endpoint = "sts.cn-shenzhen.aliyuncs.com";
    private String stsKey="aliyunSecret:sts";

    @GetMapping("/getOssSTS")
    @ApiOperation(value = "获取STS临时授权访问OSS")
    public R<OssSTSVo> getOssSTS(){
        Object o = redisUtil.get(stsKey);
        if (o!=null){
            OssSTSVo vo = (OssSTSVo)o;
            vo.setExpiration(redisUtil.getExpire(stsKey));
            return R.ok (vo);
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
            request.setDurationSeconds(2800L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = response.getCredentials();
            OssSTSVo ossSTSVo = new OssSTSVo();
            ossSTSVo.setAccessKey(credentials.getAccessKeyId());
            ossSTSVo.setSecurityToken(credentials.getSecurityToken());
            ossSTSVo.setAccessKeySecret(credentials.getAccessKeySecret());
            System.out.println(new Date());

            String expiration = credentials.getExpiration();
            System.out.println(expiration);
            ossSTSVo.setExpiration(2800L);
            redisUtil.set(stsKey,ossSTSVo,2700);
            return R.ok(ossSTSVo);
        } catch (ClientException e) {
            throw new GrabException(6023,e.getErrMsg());
        }
    }
}

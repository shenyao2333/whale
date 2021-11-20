package com.whale.provider.security.config;

import cn.hutool.core.codec.Base64;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.security.component.WhaleUserAuthenticationConverter;
import com.whale.provider.security.domain.WhaleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: shenyao
 * @Date: Created by 2021/5/23 22:13
 * @description: 向认证中心获取用户信息时，返回的数据都变成字符串了，所以重新定义loadAuthentication方法逻辑
 */
@Primary
@Component
@RequiredArgsConstructor
public class RemoteTokenService extends RemoteTokenServices {

    private final RestTemplate restTemplate;
    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final DefaultAccessTokenConverter defaultAccessTokenConverter;
    private final DiscoveryClient discoveryClient;
    private final Random random = new Random();

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws  InvalidTokenException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap();
        formData.add("token", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.getAuthorizationHeader(oAuth2ClientProperties.getClientId(), oAuth2ClientProperties.getClientSecret()));
        headers.set("Accept","application/json");
        Map<String, Object> map = this.postForMap( formData, headers);
        if (map.containsKey("status")&& !(Boolean)map.get("status")){
            this.logger.debug("check_token returned error: " + map.get("msg"));
            throw new InvalidTokenException(accessToken);
        }else if (map.containsKey("error")) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("check_token returned error: " + map.get("error"));
            }
            throw new InvalidTokenException(accessToken);
        } else if (map.containsKey("active") && !"true".equals(String.valueOf(map.get("active")))) {
            this.logger.debug("check_token returned active attribute: " + map.get("active"));
            throw new InvalidTokenException(accessToken);
        } else {
            return defaultAccessTokenConverter.extractAuthentication(map);
        }
    }


    private String getAuthorizationHeader(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            this.logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }

        String creds = String.format("%s:%s", clientId, clientSecret);

        try {
            return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    private Map<String, Object> postForMap( MultiValueMap<String, String> formData, HttpHeaders headers) {

        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        return restTemplate.exchange(this.getPath(), HttpMethod.POST, new HttpEntity(formData, headers), Map.class, new Object[0]).getBody();
    }


    /**
     *
     * @return http://127.0.0.1:8000/whale-oauth2/oauth/check_token
     */
    private String getPath(){
        List<ServiceInstance> instances = discoveryClient.getInstances("whale-gateway");
        if (instances==null||instances.size()<1){
            throw new GrabException("认证服务异常");
        }
        int nextInt = random.nextInt(instances.size());
        ServiceInstance serve = instances.get(nextInt);
        return "http://127.0.0.1:"+serve.getPort() + "/whale-oauth2/oauth/check_token";

    }




}

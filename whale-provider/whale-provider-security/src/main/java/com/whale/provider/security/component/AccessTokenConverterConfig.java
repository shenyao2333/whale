package com.whale.provider.security.component;

import com.whale.provider.security.component.WhaleUserAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: sy
 * @Date: Created by 2021/5/25 14:35
 * @description: 定义转化 authentication.getPrincipal() 获取到的数据
 */
@Component
public class AccessTokenConverterConfig {


    @Resource
    private WhaleUserAuthenticationConverter whaleUserAuthenticationConverter;

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter(){
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(whaleUserAuthenticationConverter);
        return accessTokenConverter;
    }

}

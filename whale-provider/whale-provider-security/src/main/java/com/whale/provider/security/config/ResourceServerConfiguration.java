package com.whale.provider.security.config;


import cn.hutool.core.convert.Convert;
import com.whale.provider.security.component.WhaleUserAuthenticationConverter;
import com.whale.provider.security.exception.CustomAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sy
 * @date Created in 2020.9.27 0:06
 * @description spring security oauth 的http配置。
 */
@Slf4j
@Configuration
@RefreshScope
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private PermitProps permitProps;

    @Resource
    private WhaleUserAuthenticationConverter whaleUserAuthenticationConverter;

    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Value("${security.oauth2.resourceId}")
    private String resourceId;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置校验token方式
     * @param
     * @throws Exception
     */
   @Override
   public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
       DefaultAccessTokenConverter defaultAccessTokenConverter=new DefaultAccessTokenConverter();
       defaultAccessTokenConverter.setUserTokenConverter(whaleUserAuthenticationConverter);
       defaultAccessTokenConverter.setUserTokenConverter(whaleUserAuthenticationConverter);
       resources.authenticationEntryPoint(customAuthenticationEntryPoint);
       resources.resourceId(resourceId);
       super.configure(resources);
   }


    /**
     * 对匹配的资源进行放行
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] urls = Convert.toStrArray(permitProps.getIgnoreUrls());
        log.info("忽略路径有："+permitProps.getIgnoreUrls());
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers(urls).permitAll();
        http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
    }








}

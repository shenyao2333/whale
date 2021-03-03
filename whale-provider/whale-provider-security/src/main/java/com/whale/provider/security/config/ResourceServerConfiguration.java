package com.whale.provider.security.config;


import cn.hutool.core.convert.Convert;
import com.whale.provider.security.component.WhaleUserAuthenticationConverter;
import com.whale.provider.security.exception.CustomAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * @author sy
 * @date Created in 2020.9.27 0:06
 * @description spring security oauth 的http配置。
 */
@Slf4j
@Configuration
@RefreshScope
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
    private RemoteTokenServices remoteTokenServices;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 配置校验token方式
     * @param
     * @throws Exception
     */
   @Override
   public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
       DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
       accessTokenConverter.setUserTokenConverter(whaleUserAuthenticationConverter);
       remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
       remoteTokenServices.setRestTemplate(restTemplate);
       resources.tokenServices(remoteTokenServices);
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
        http.authorizeRequests().antMatchers(urls).permitAll();

    }




}

package com.whale.provider.security.config;

import cn.hutool.core.convert.Convert;

import com.whale.provider.security.component.WhaleUserAuthenticationConverter;
import com.whale.provider.security.exception.CustomAuthenticationEntryPoint;
import com.whale.provider.security.handler.AuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author sy
 * @date Created in 2020.9.27 0:06
 * @description spring security oauth 的http配置。
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private PermitProps permitProps;

    @Resource
    private WhaleUserAuthenticationConverter whaleUserAuthenticationConverter;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Resource
    private RestTemplate restTemplate;

    //@Value("${security.oauth2.resourceId}")
    //private String resourceId;


    /**
     * 配置校验token方式
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(whaleUserAuthenticationConverter);
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setAccessTokenConverter(accessTokenConverter);
        tokenServices.setRestTemplate(restTemplate);
        resources.tokenServices(tokenServices);
        resources.authenticationEntryPoint(customAuthenticationEntryPoint);
       // resources.resourceId(resourceId);
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

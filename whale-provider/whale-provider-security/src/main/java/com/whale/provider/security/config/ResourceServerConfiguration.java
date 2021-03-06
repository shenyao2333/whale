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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter  {

    @Resource
    private PermitProps permitProps;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Value("${security.custom.resourceId}")
    private String resourceId;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RemoteTokenService remoteTokenService;
    @Resource
    private DefaultAccessTokenConverter defaultAccessTokenConverter;

    /**
     * 配置校验token方式
     *
     */
   @Override
   public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
       remoteTokenService.setAccessTokenConverter(defaultAccessTokenConverter);
       remoteTokenService.setRestTemplate(restTemplate);
       resources.tokenServices(remoteTokenService);
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
        List<String> ignoreUrls = permitProps.getIgnoreUrls();
        ArrayList<String> urlList = new ArrayList<>(10);
        ignoreUrls.forEach( item ->{
            if (item.contains(",")){
                String[] split = item.split(",");
                urlList.addAll(Arrays.asList(split));
            }else {
                urlList.add(item);
            }
        });
        String[] urls = Convert.toStrArray(urlList);
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers(urls).permitAll();
        http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

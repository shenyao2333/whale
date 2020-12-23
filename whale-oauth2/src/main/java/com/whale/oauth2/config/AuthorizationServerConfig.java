package com.whale.oauth2.config;


import com.whale.oauth2.handler.ExceptionTranslator;
import com.whale.oauth2.service.impl.WhaleUserDetailService;
import com.whale.provider.basices.domain.WhaleUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sy
 * @date Created in 2020.9.15 21:52
 * @description 资源服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter  implements WebMvcConfigurer {

    @Resource
    private  RedisConnectionFactory redisConnectionFactory;

    @Resource
    private  DataSource dataSource;

    @Resource
    private  WhaleUserDetailService userDetailService;

    //@Resource
    //private  AuthenticationEntryPoint authenticationEntryPoint;


    /**
     * 密码认证
     */
    @Resource
    private   AuthenticationManager authenticationManager;


    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        ((RedisTokenStore) tokenStore).setPrefix("whale:");
        return tokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 设置令牌
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                //允许 GET、POST 请求获取 token，即访问端点：oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenEnhancer(tokenEnhancer())
                .userDetailsService(userDetailService)
                .reuseRefreshTokens(true)
                .exceptionTranslator(new ExceptionTranslator())
        ;
    }



    /**
     * token增强，添加oauth2默认返回的数据
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            WhaleUser userDetails =(WhaleUser) authentication.getUserAuthentication().getPrincipal();
            Map<String, Object> info = new HashMap<String, Object>(3){
                {
                    put("userName", userDetails.getUsername());
                    put("userId",userDetails.getUserId());
                    put("avatar",userDetails.getAvatar());
                    put("status",true);
                }
            };
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        };

    }



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
        clients.withClientDetails(jdbcClientDetails());
    }
    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(dataSource);
    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()//允许表单认证
                //.accessDeniedHandler(authenticationEntryPoint)
        ;

    }



}

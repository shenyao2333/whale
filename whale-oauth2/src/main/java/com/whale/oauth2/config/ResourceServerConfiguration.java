package com.whale.oauth2.config;

import com.whale.oauth2.filter.WhaleFilter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sy
 * @date Created in 2020.9.27 0:06
 * @description 资源服务器配置
 */
@Configuration
@EnableResourceServer
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter  implements WebMvcConfigurer {


    private final WhaleFilter filter;

    private final AuthenticationEntryPoint authenticationEntryPoint;


    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http
                // CRSF禁用，因为不使用session
                .csrf().disable()
                //Basic登录
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                //开放的资源不用授权
                .antMatchers("/oauth/**","/login").permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
        ;
    }




}

package com.whale.provider.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author sy
 * @date Created in 2020.9.27 0:02
 * @description 资源服务器配置
 */
@Configuration
@EnableWebSecurity
/*@Order(1)*/
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }






    /**
     * 配置忽略的URL
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/auth/login","/test","/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**","/doc.html");
    }



}

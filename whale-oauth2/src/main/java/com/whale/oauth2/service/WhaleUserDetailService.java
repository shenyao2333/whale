package com.whale.oauth2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/5 22:02
 * @description:
 */
public interface WhaleUserDetailService extends UserDetailsService {

    /**
     * 手机号登录逻辑
     * @param mobile
     * @param code
     * @return
     */
    UserDetails loadUserByMobile(String mobile,String code);


}

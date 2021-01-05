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
     * 根据手机号查询
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);


    /**
     * QQ 登录
     * @param qqOpenid
     * @return
     */
    UserDetails loginByQQ(String qqOpenid);


    /**
     * 微信登录
     * @param wxOpenid
     * @return
     */
    UserDetails loginByWX(String wxOpenid);
}

package com.whale.api.order.oauth2.dubbo.service;

import com.whale.api.order.oauth2.domain.WhaleUser2;

/**
 * @author sy
 * @date: 2021/3/10 17:18
 * @description oauth2认证处理
 */
public interface UserAuthService {



    WhaleUser2 getUserInfoByToken(String token);




}

package com.whale.api.oauth2.dubbo.service;

import com.whale.api.oauth2.domain.WhaleUser;

/**
 * @author sy
 * @date: 2021/3/10 17:18
 * @description oauth2认证处理
 */
public interface UserAuthService {



    WhaleUser getUserInfoByToken(String token);




}

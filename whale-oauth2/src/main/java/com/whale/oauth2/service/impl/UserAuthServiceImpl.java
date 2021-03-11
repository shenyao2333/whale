package com.whale.oauth2.service.impl;

import com.whale.api.order.oauth2.domain.WhaleUser2;
import com.whale.api.order.oauth2.dubbo.service.UserAuthService;
import lombok.RequiredArgsConstructor;

/**
 * @author sy
 * @date: 2021/3/10 17:29
 * @description
 */
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {


    @Override
    public WhaleUser2 getUserInfoByToken(String value) {
        return null;
    }


}

package com.whale.oauth2.service.impl;

import com.whale.api.oauth2.domain.WhaleUser;
import com.whale.api.oauth2.dubbo.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;

/**
 * @author sy
 * @date: 2021/3/10 17:29
 * @description
 */
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {




    @Override
    public WhaleUser  getUserInfoByToken(String value) {
        return null;

    }


}

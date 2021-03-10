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

    private final ResourceServerTokenServices resourceServerTokenServices;
    private final AccessTokenConverter accessTokenConverter;


    @Override
    public WhaleUser  getUserInfoByToken(String value) {


        OAuth2AccessToken token = this.resourceServerTokenServices.readAccessToken(value);

        if (token == null) {

            throw new InvalidTokenException("Token was not recognised");
        } else if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        } else {
            OAuth2Authentication authentication = this.resourceServerTokenServices.loadAuthentication(token.getValue());
            Map<String, ?> response = this.accessTokenConverter.convertAccessToken(token, authentication);
            System.out.println(response);
        }
        return null;

    }


}

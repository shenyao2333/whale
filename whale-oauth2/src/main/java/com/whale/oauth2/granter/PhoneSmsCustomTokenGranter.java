package com.whale.oauth2.granter;

import com.whale.oauth2.service.WhaleUserDetailService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sy
 * @date: 2021/3/4 16:48
 * @description
 */
public class PhoneSmsCustomTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE="phone_code";
    private final  WhaleUserDetailService whaleUserDetailService;

    public PhoneSmsCustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, WhaleUserDetailService whaleUserDetailService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.whaleUserDetailService= whaleUserDetailService;
    }


    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String phone = parameters.get("phone");
        String code = parameters.get("code");
        UserDetails user = this.whaleUserDetailService.loadUserByMobile(phone,code);
        AbstractAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        userAuth.setDetails(parameters);
        OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(oAuth2Request, userAuth);

    }





}

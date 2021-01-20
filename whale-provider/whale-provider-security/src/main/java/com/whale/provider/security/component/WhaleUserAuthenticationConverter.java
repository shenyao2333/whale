
package com.whale.provider.security.component;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义 UserAuthenticationConverter
 * @author Aijm
 * @since 2019/7/28
 */
@Service
public class WhaleUserAuthenticationConverter extends DefaultUserAuthenticationConverter {


    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey("user_name")) {
            Object principal = map.get("user_name");
           //Object obj = map.get(AdditionalToken.USER_ID);
           //Long userId = Long.valueOf(String.valueOf(obj));
           //Object objType = map.get(AdditionalToken.USER_TYPE);
           //Object tenantId = map.get(AdditionalToken.TENANT_ID);
            //WhaleUser user = new WhaleUser(1,"WhaleUser","","");
            return new UsernamePasswordAuthenticationToken(null, "N/A", AuthorityUtils.NO_AUTHORITIES);
        } else {
            return null;
        }
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get("authorities");
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String)authorities);
        } else if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection)authorities));
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
    }
}